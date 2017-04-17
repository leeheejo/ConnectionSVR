package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.domain.ArtikUserProfile;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceList;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.service.ArtikUserProfileService;
import com.icontrols.test.service.ConnectedCompanyService;
import com.icontrols.test.service.DeviceListService;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.SendTestLogService;
import com.icontrols.test.util.*;

/*
 * restAPI reference
 * https://developer.artik.cloud/api-console/
 * */
@Controller
public class ArtikController {

	/* heejo's cloud info private static */

	// private static String clientId = "cfecebedc26b4690bf9f883d425200a5";
	// private static String clientSecret = "88fb76ff979543979b3f7b4728b317f2";

	/* inbo's cloud info */
	private static String clientId = "cbd3e38e12344b22a8c76cd3789b0e0e";
	private static String clientSecret = "fafe61a1191940989a86d9e69e1e3d66";

	private static String callbackUrl = "https://localhost:8443/test/callback";

	private static final Logger logger = LoggerFactory.getLogger(ArtikController.class);

	@Autowired
	AccessTokenService accessTokenService;
	@Autowired
	ArtikUserProfileService artikUserProfileService;
	@Autowired
	SendTestLogService sendTestLogService;
	@Autowired
	ConnectedCompanyService connectedCompanyService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	DeviceListService deviceListService;

	@RequestMapping("artikLogin")
	public ModelAndView artikLogin(Model model, HttpSession session) throws Exception {

		logger.info("[artikLogin]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("deviceList");
		/*
		 * error :Invalid redirect_uri callbackUrl :
		 * https://developer.artik.cloud/dashboard/applications의 app info-auth
		 * redirect url과 일치해야함
		 * 
		 * response_type = token 이면 access_token을 바로 받을 수 있으나 서버단에서 확인 안되고 보안에
		 * 약함
		 */

		String oauthUrl = "https://accounts.artik.cloud/authorize?" + "client_id=" + clientId
				+ "&response_type=code&redirect_uri=" + callbackUrl;

		model.addAttribute("oauthUrl", oauthUrl);

		logger.info("[artikLogin] oauthUrl : {}", oauthUrl);

		return mav;
	}

	// get AccessToken
	@RequestMapping(value = "callback")
	public String getArtikClientAccessToken(HttpServletRequest request, HttpSession session) throws Exception {

		logger.info("[callback]");
		String code = request.getParameter("code");
		logger.info("[callback] code : {}", code);
		// ARTIK 규칙 client_id:client_secret 을 Base64로 인코딩해 헤더에 넣어야함
		String userpass = clientId + ":" + clientSecret;
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));

		// httpPost 통신
		URL url = new URL("https://accounts.artik.cloud/token");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Host", "accounts.artik.cloud");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// Parameter
		String param = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + callbackUrl;
		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response
		int responseCode = con.getResponseCode();
		logger.info("[callback] responseCode : {}", responseCode + con.getResponseMessage());

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[callback] responseData : {}", responseData);
		br.close();

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		// AccessToken.class 와 response json 의 parameter명이 일치해야 매핑가능
		AccessToken accessToken = mapper.readValue(responseData, AccessToken.class);
		accessToken.setuId(session.getAttribute("userLoginInfo").toString());

		// 중복체크
		int tokenChk = accessTokenService.tokenCheck(accessToken.getuId());

		if (tokenChk > 0) {
			logger.info("[callback] update AccessToken");
			accessTokenService.updateAccessToken(accessToken);
		} else {
			logger.info("[callback] insert AccessToken");
			accessTokenService.insertAccessToken(accessToken);
		}
		// accessToken session 설정
		session.setAttribute("ACCESS_TOKEN", accessToken.getAccess_token());

		getArtikUserInfo(session);

		return "callback";

	}

	@RequestMapping("/getArtikDeviceList")
	public String getArtikDeviceList(HttpSession session, Model model) throws Exception {

		logger.info("[getArtikDeviceList]");

		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
		String userId = (String) session.getAttribute("ARTIK_USER_ID");

		logger.info("[getArtikDeviceList] ACCESS_TOKEN : {}, ARTIK_USER_ID : {}", accessToken, userId);

		// httpGET 통신
		URL url = new URL(
				"https://api.artik.cloud/v1.1/users/" + userId + "/devices?count=100&includeProperties=false");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getArtikDeviceList] responseCode : {}", responseCode + con.getResponseMessage());

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikDeviceList] responseData : {}", responseData);

		// parsing response Data
		List<DeviceList> artikDeviceList = ArtikUtils.parsinDeviceList(responseData,
				session.getAttribute("userLoginInfo").toString());
		List<Device> userDeviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		List<DeviceList> newDeviceList = new ArrayList();

		for (DeviceList dl : artikDeviceList) {
			int flag = 0;
			for (Device d : userDeviceList) {
				if (dl.getId().equals(d.getdId())) {
					flag = 1;
				}
			}
			if (flag == 0) {
				newDeviceList.add(dl);
			}

		}
		model.addAttribute("artikDeviceList", newDeviceList);

		// //insert deviceList
		// for (int i = 0; i < artikDeviceList.size(); i++) {
		// deviceListService.inserDeviceList(artikDeviceList.get(i));
		// }

		logger.info("getArtikDeviceList-sb : {}", responseData);
		br.close();

		return "deviceList";
	}

	/*
	 * send Action
	 * 
	 * @RequestParam dId (String) device's ID
	 * 
	 * @RequestParamu statr (int) device's current State
	 * 
	 * @return String "redirect:/success"
	 */

	// @RequestMapping("/sendActionTest")
	// public String sendActionTest(HttpSession session, @RequestParam(value =
	// "dId") String dId,
	// @RequestParam(value = "state") int currentState, @RequestParam(value =
	// "R", required = false) String R,
	// @RequestParam(value = "G", required = false) String G,
	// @RequestParam(value = "B", required = false) String B) throws Exception {
	@RequestMapping("/sendActionTest")
	public String sendActionTest(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int currentState) throws Exception {
		logger.info("[sendActionTest]");
		String dtId = deviceService.getDeviceTypeId(dId, session.getAttribute("userLoginInfo").toString());

		if (!dtId.equals("main_light")) {
			// if (dtId.equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)
			// && (!R.equals("") || !G.equals("") || !B.equals(""))) {
			// if (R.equals("") || Integer.parseInt(R) > 255)
			// R = "0";
			// if (G.equals("") || Integer.parseInt(G) > 255)
			// G = "0";
			// if (B.equals("") || Integer.parseInt(B) > 255)
			// B = "0";
			// Action(session, dId, "setColorRGB", R + ";" + G + ";" + B);
			//
			// } else
			if (dtId.equals("main_light")) {
				String action = "";
				if (currentState == 0) {
					action = "setOn";
				} else {
					action = "setOff";
				}
				IparkUtils.Action("control", action, dId, session.getAttribute("userLoginInfo").toString());
			} else {
				String action = "";
				if (currentState == 0) {
					action = "setOn";
				} else {
					action = "setOff";
				}
				Action(session, dId, action, "");
			}
		} else {
			String action = "";
			if (currentState == 0) {
				action = "setOn";
			} else {
				action = "setOff";
			}
			IparkUtils.sendAction(action, session.getAttribute("userLoginInfo").toString(), dId);
		}

		return "redirect:/success";

	}

	/*
	 * Insert Device
	 * 
	 * @RequestParam dId (String) device's ID
	 * 
	 * @RequestParamu name (String) device's name
	 * 
	 * @RequestParamu dtId (String) device type's ID
	 * 
	 * @return String "redirect:/success"
	 */

	@RequestMapping("/insertDevice")
	public String insertDevice(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "name") String name, @RequestParam(value = "dtId") String dtId) throws Exception {

		Device device = new Device(session.getAttribute("userLoginInfo").toString(), dId, name, dtId);
		deviceService.insertDevice(device);

		logger.info("[insertDevice] dId : {}, name : {}", dId, name);

		return "redirect:/success";
	}

	@RequestMapping("/allOff")
	public String allOff(HttpSession session) throws Exception {

		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		for (Device d : deviceList) {
			logger.info("{}", d.getName());
			if (d.getDtId().equals("main_light")) {
				IparkUtils.sendAction("setOff", session.getAttribute("userLoginInfo").toString(), d.getdId());
			} else {
				Action(session, d.getdId(), "setOff", "");
			}
			Thread.sleep(4000);
		}
		return "redirect:/success";
	}

	@RequestMapping("/allOn")
	public String allOn(HttpSession session) throws Exception {
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());

		for (Device d : deviceList) {
			logger.info("{}", d.getName());
			if (d.getDtId().equals("main_light")) {
				IparkUtils.sendAction("setOn", session.getAttribute("userLoginInfo").toString(), d.getdId());
			} else {
				Action(session, d.getdId(), "setOn", "");
			}
			Thread.sleep(4000);
		}

		return "redirect:/success";
	}

	@RequestMapping("/colorLoop")
	public String colorLoop(HttpSession session) throws Exception {
		logger.info("[colorloop]");
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		for (Device d : deviceList) {
			if (d.getDtId().equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)) {
				logger.info("{}", d.getName());
				Action(session, d.getdId(), "setEffect", "");
				Thread.sleep(2000);
			}
		}

		return "redirect:/success";
	}

	@RequestMapping("/addNewDevice")
	public String addNewDevice(HttpSession session, @RequestParam(value = "dtId") String dtId,
			@RequestParam(value = "name") String name) throws Exception {

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/devices");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		int manifestVersion = getManifestVersion(session, dtId);
		// Param
		String param = "{\"uid\": \"" + session.getAttribute("ARTIK_USER_ID").toString() + "\",\"dtid\": \"" + dtId
				+ "\",  \"name\": \"" + name + "\",\"manifestVersion\":" + manifestVersion
				+ ",\"manifestVersionPolicy\": \"LATEST\"}";

		logger.info("[addNewDevice] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[addNewDevice] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[addNewDevice] responseData : {}", responseData);
		br.close();

		return "deviceList";
	}

	@RequestMapping("/createSubscription")
	public String createSubscription(HttpSession session) throws Exception {

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Param
		String param = "{\"uid\": \"" + session.getAttribute("ARTIK_USER_ID").toString() + "\",\"callbackUrl\": \""
				+ callbackUrl + "\",  \"messageType\": \"message\"}";

		logger.info("[createSubscription] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[createSubscription] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[createSubscription] responseData : {}", responseData);
		br.close();

		return "deviceList";
	}

	public void getArtikUserInfo(HttpSession session) throws Exception {

		logger.info("[getArtikUserInfo]");
		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

		// HttpGET 통신
		URL url = new URL("https://api.artik.cloud/v1.1/users/self");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getArtikUserInfo] responseCode : {}", responseCode + con.getResponseMessage());

		// Respose Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikUserInfo] responseData : {}", responseData);
		br.close();

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = mapper.readValue(responseData, HashMap.class);
		logger.info("data : {}", data.get("data"));
		@SuppressWarnings("unchecked")
		HashMap<String, Object> userData = (HashMap<String, Object>) data.get("data");

		// 중복체크
		int userProfileChk = artikUserProfileService.artikUserCheck(session.getAttribute("userLoginInfo").toString());

		ArtikUserProfile artikUserProfile = new ArtikUserProfile(session.getAttribute("userLoginInfo").toString(),
				userData.get("id").toString(), userData.get("name").toString(), userData.get("email").toString(),
				userData.get("fullName").toString(), userData.get("saIdentity").toString(),
				userData.get("accountType").toString(), userData.get("createdOn").toString(),
				userData.get("modifiedOn").toString());

		// Insert or Update Artik User Profile
		if (userProfileChk > 0) {
			logger.info("[getArtikUserInfo] update Artik User Profile}");
			artikUserProfileService.updateArtikUserProfile(artikUserProfile);
		} else {
			logger.info("[getArtikUserInfo] insert Artik User Profile}");
			artikUserProfileService.insertArtikUserProfile(artikUserProfile);
		}

		session.setAttribute("ARTIK_USER_ID", userData.get("id").toString());
	}

	/*
	 * Send Action Method
	 * 
	 * @RequestParam action (String) setOn, setOff, setColorRGB, setEffect
	 * 
	 * @RequestParam dId (String) device ID
	 * 
	 * @RequestParam rgb (String) rgbColor for setColorRGB
	 * 
	 * @return void
	 */

	public void Action(HttpSession session, String dId, String action, String rgb) throws Exception {
		logger.info("[ACTION]");

		URL url = new URL("https://api.artik.cloud/v1.1/messages");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String authorizationHeader = "bearer " + session.getAttribute("ACCESS_TOKEN").toString();
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		String param = "";

		if (action.equals("setOn")) {
			logger.info("[Action] : setOn");
			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setOn\",\"parameters\": {}}]}}";

		} else if (action.equals("setOff")) {
			logger.info("[Action] setOff");
			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setOff\",\"parameters\": {}}]}}";

		} else if (action.equals("setColorRGB")) {
			logger.info("[Action] setColorRGB");
			int R = Integer.parseInt(rgb.split(";")[0]);
			int G = Integer.parseInt(rgb.split(";")[1]);
			int B = Integer.parseInt(rgb.split(";")[2]);

			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setColorRGB\",\"parameters\": {\"colorRGB\":{\"blue\":"
					+ B + ",\"green\":" + G + ",\"red\":" + R + "}}}]}}";

		} else if (action.equals("setEffect")) {
			logger.info("[Action] setEffect");
			logger.info("[Action] dId : {}", dId);
			param = "{ \"data\": {\"actions\": [{\"name\": \"setEffect\",\"parameters\": { \"effect\": \"colorloop\"}}]},\"ddid\": \""
					+ dId + "\",\"ts\": " + System.currentTimeMillis() + ",\"type\": \"action\"}";

		}
		logger.info("[Action] {}", param);
		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[Action] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[Action] responseData : {}", responseData);
		br.close();

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(session.getAttribute("userLoginInfo").toString(), 1, dId, param,
				responseCode + con.getResponseMessage(), new Date(System.currentTimeMillis()));
		sendTestLogService.insertSendTestLog(sendTestLog);
		logger.info("[Action] insert SEND_TEST_LOG");

	}

	public int getManifestVersion(HttpSession session, String dtId) throws Exception {

		int result = 0;

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getManifestVersion] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getManifestVersion] responseData : {}", responseData);
		br.close();

		JSONObject obj = new JSONObject(responseData);
		logger.info("[getManifestVersion] obj : {}", obj);
		JSONObject data = obj.getJSONObject("data");
		JSONArray devices = data.getJSONArray("versions");
		logger.info("[getManifestVersion] devices : {}", devices.get(0));

		result = Integer.parseInt(devices.get(devices.length() - 1).toString());

		return result;
	}

}
