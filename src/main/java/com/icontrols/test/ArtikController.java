package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

	private static String callbackUrl = "http://localhost:8080/test/callback";

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
		model.addAttribute("artikDeviceList", artikDeviceList);

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

	@RequestMapping("/sendActionTest")
	public String sendActionTest(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int currentState, @RequestParam(value = "R", required = false) String R,
			@RequestParam(value = "G", required = false) String G,
			@RequestParam(value = "B", required = false) String B) throws Exception {

		logger.info("[sendActionTest]");
		if (!R.equals("") || !G.equals("") || !B.equals("")) {
			if (R.equals("") || Integer.parseInt(R) > 255) {
				R = "0";
			}
			if (G.equals("") || Integer.parseInt(G) > 255) {
				G = "0";
			}
			if (B.equals("") || Integer.parseInt(B) > 255) {
				B = "0";
			}
			sendActionForColor(session, dId, Integer.parseInt(R), Integer.parseInt(G), Integer.parseInt(B));
		}
		sendAction(session, currentState, dId);

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
			sendAction(session, 1, d.getdId());
		}
		return "redirect:/success";
	}

	@RequestMapping("/allOn")
	public String allOn(HttpSession session) throws Exception {
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		for (Device d : deviceList) {
			sendAction(session, 0, d.getdId());
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
	 * @RequestParam flag (int) setOn = 0, setOff = 1
	 * 
	 * @RequestParamu Pwd (String) destination device ID
	 * 
	 * @return void
	 */
	public void sendAction(HttpSession session, int flag, String dId) throws Exception {
		logger.info("[sendActionTest]");
		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
		logger.info("[sendActionTest] ACCESS_TOKEN : {}", accessToken);

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/messages");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		String action = "";
		// Parameter
		if (flag == 1) { // Device On
			action = "setOff";
		} else { // currentState == 0 Device Off
			action = "setOn";
		}

		// Action : setOn
		String param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
				+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"" + action + "\",\"parameters\": {}}]}}";

		logger.info("[sendActionTest] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[sendActionTest] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[sendActionTest] responseData : {}", responseData);
		br.close();

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(session.getAttribute("userLoginInfo").toString(), 1, dId, param,
				responseCode + con.getResponseMessage(), new Date(System.currentTimeMillis()));
		sendTestLogService.insertSendTestLog(sendTestLog);
		logger.info("[sendActionTest] insert SEND_TEST_LOG");

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = mapper.readValue(responseData, HashMap.class);
		logger.info("data : {}", data.get("data"));
		@SuppressWarnings("unchecked")
		HashMap<String, Object> userData = (HashMap<String, Object>) data.get("data");
		logger.info("data : {}", userData.get("mid").toString());

	}

	public void sendActionForColor(HttpSession session, String dId, int R, int G, int B) throws Exception {
		logger.info("[sendActionTest]");
		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
		logger.info("[sendActionTest] ACCESS_TOKEN : {}", accessToken);

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/messages");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		// Action : setOn
		String param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
				+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setColorRGB\",\"parameters\": {\"colorRGB\":{\"blue\":"
				+ B + ",\"green\":" + G + ",\"red\":" + R + "}}}]}}";

		logger.info("[sendActionTest] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[sendActionTest] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[sendActionTest] responseData : {}", responseData);
		br.close();

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(session.getAttribute("userLoginInfo").toString(), 1, dId, param,
				responseCode + con.getResponseMessage(), new Date(System.currentTimeMillis()));
		sendTestLogService.insertSendTestLog(sendTestLog);
		logger.info("[sendActionTest] insert SEND_TEST_LOG");

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = mapper.readValue(responseData, HashMap.class);
		logger.info("data : {}", data.get("data"));
		@SuppressWarnings("unchecked")
		HashMap<String, Object> userData = (HashMap<String, Object>) data.get("data");
		logger.info("data : {}", userData.get("mid").toString());

	}

	public int getManifestVersion(HttpSession session, String dtId) throws Exception {
		int result = 0;

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/devicetypes/"+ dtId +"/availablemanifestversions");
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
		
		result = Integer.parseInt(devices.get(0).toString());
		
		return result;
	}
}
