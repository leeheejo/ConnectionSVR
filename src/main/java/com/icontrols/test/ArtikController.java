package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.domain.ArtikUserProfile;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceGroup;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.service.ArtikUserProfileService;
import com.icontrols.test.service.ConnectedCompanyService;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.SendTestLogService;
import com.icontrols.test.util.*;

/*
 * restAPI reference
 * https://developer.artik.cloud/api-console/
 * */
@Controller
public class ArtikController {

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

	@RequestMapping("artikLogin")
	public ModelAndView artikLogin(Model model, HttpSession session) throws Exception {

		logger.info("[artikLogin]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("deviceList");

		/*
		 * error :Invalid redirect_uri callbackUrl :
		 * https://developer.artik.cloud/dashboard/applications의 app info-auth
		 * redirect url과 일치해야함
		 */

		String oauthUrl = "https://accounts.artik.cloud/authorize?" + "client_id=" + NetworkInfo.ARTIK_CLIENT_ID
				+ "&response_type=code&redirect_uri=" + NetworkInfo.ARTIK_CALLBACK_URL;

		model.addAttribute("oauthUrl", oauthUrl);

		logger.info("[artikLogin] oauthUrl : {}", oauthUrl);
		logger.info("[artikLogin] accessToken : {}", session.getAttribute("ACCESS_TOKEN"));

		return mav;
	}

	// get AccessToken

	@RequestMapping(value = "callback", method = RequestMethod.GET)
	public String getArtikClientAccessToken(HttpServletRequest request, HttpSession session) throws Exception {

		logger.info("[callback]");
		String code = request.getParameter("code");
		logger.info("[callback] code : {}", code);

		AccessToken accessToken = ArtikUtils.getArtikAccessToken(code);
		accessToken.setuId((String) session.getAttribute("userLoginInfo"));

		// 중복체크
		if (accessTokenService.tokenCheck(accessToken.getuId()) > 0) {
			logger.info("[callback] update AccessToken");
			accessTokenService.updateAccessToken(accessToken);
		} else {
			logger.info("[callback] insert AccessToken");
			accessTokenService.insertAccessToken(accessToken);
		}

		// accessToken session 설정
		session.setAttribute("ACCESS_TOKEN", accessToken.getAccess_token());

		// 중복체크
		int userProfileChk = artikUserProfileService.artikUserCheck((String) session.getAttribute("userLoginInfo"));
		ArtikUserProfile artikUserProfile = ArtikUtils.getArtikUserInfo(session);

		// Insert or Update Artik User Profile
		if (userProfileChk > 0) {
			logger.info("[getArtikUserInfo] update Artik User Profile}");
			artikUserProfileService.updateArtikUserProfile(artikUserProfile);
		} else {
			logger.info("[getArtikUserInfo] insert Artik User Profile}");
			artikUserProfileService.insertArtikUserProfile(artikUserProfile);
		}

		session.setAttribute("ARTIK_USER_ID", artikUserProfile.getId());

		return "callback";

	}

	@RequestMapping(value = "callback", method = RequestMethod.POST)
	public String getArtikClientSubscription(HttpServletRequest request, HttpSession session) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String responseData = br.readLine();
		logger.info("[callback] {}", responseData);
		JSONObject obj = new JSONObject(responseData);

		if (obj.length() == 3) { // subscription 생성시 validate 로
									// 넘어가야함
			// {"aid":"cbd3e38e12344b22a8c76cd3789b0e0e","subscriptionId":"0d3dd7d79f664087a649540d19f11663",
			// "nonce":"bc051ecea5e44deeb03e53057e4e63a8"}
			ArtikUtils.validateSubscription(obj.getString("subscriptionId"), obj.getString("aid"),
					obj.getString("nonce"));

		} else { // subscripiton query id
					// msg 요청해야함
			// id msg ID
			// {"id":"5336bf1ef2804dd882c315d32675ab4f","ts":1494137879113,"type":"action","subscriptionId":"0d3dd7d79f664087a649540d19f11663","subscriptionQuery":{"uid":"58e8794672f848f5bf65dfd6267ff9b9","ddid":"f9da7204a9644b99a92ae2da56c48df8"},"startDate":"1494137878872","endDate":"1494137878872","count":1}
			logger.info("{}", obj.getString("id"));
			String dId = obj.getJSONObject("subscriptionQuery").getString("ddid");
			String accessToken = accessTokenService.getAccessTokenById(deviceService.getUIdByDId(dId));
			String action = ArtikUtils.getNotification(accessToken, obj.getString("id"));
			logger.info("{}", action);
			int state = 0;
			if (action.equals("setOn") || action.equals("setColorRGB")) {
				state = 1;
			}
			List<String> groupList = deviceService.getGIdBydId(dId);
			logger.info("{}", groupList.toString());
			if (groupList != null) {
				for (String s : groupList) {
					deviceService.updateGroupState(0, s);
					for (String dIds : deviceService.getDeviceGroupDids(deviceService.getUIdByDId(s), s)) {

						if (state == 1 && deviceService.getDeviceStateByDId(s, deviceService.getUIdByDId(dIds)) != 1) {
							logger.info("Group {} update", s);
							deviceService.updateGroupState(state, s);
						}
					}
				}
			}

			deviceService.updateDeviceStateSubscription(state, dId);
		}

		return "success";

	}

	@RequestMapping("/getArtikDeviceList")
	public String getArtikDeviceList(HttpSession session, Model model) throws Exception {

		logger.info("[getArtikDeviceList]");

		List<Device> artikDeviceList = ArtikUtils.getArtikDeviceList(session);
		logger.info("[getArtikDeviceList] {} ", artikDeviceList.toString());
		List<Device> userDeviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		List<Device> newDeviceList = new ArrayList<Device>();

		for (Device dl : artikDeviceList) {
			int flag = 0;
			for (Device d : userDeviceList) {
				if (dl.getdId().equals(d.getdId())) {
					flag = 1;
				}
			}
			if (flag == 0) {

				newDeviceList.add(dl);
			}

		}
		model.addAttribute("artikDeviceList", newDeviceList);

		return "deviceList";
	}

	@RequestMapping("/getDeviceListAjax")
	public @ResponseBody Object getDeviceListAjax(HttpSession session, Model model) throws Exception {

		List<Device> finalList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());

		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("data", finalList);

		Object result = mp;

		return result;
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

	@RequestMapping("/sendActionRGB")
	public String sendActionRGB(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int state, @RequestParam(value = "name") String name,
			@RequestParam(value = "actionR", required = false) String R,
			@RequestParam(value = "actionG", required = false) String G,
			@RequestParam(value = "actionB", required = false) String B) throws Exception {

		logger.info("[sendActionTest]");
		SendTestLog sendTestLog;

		if (R.equals("") || Integer.parseInt(R) > 255)
			R = "0";
		if (G.equals("") || Integer.parseInt(G) > 255)
			G = "0";
		if (B.equals("") || Integer.parseInt(B) > 255)
			B = "0";
		sendTestLog = ArtikUtils.Action(session, dId, "setColorRGB", R + ";" + G + ";" + B);

		sendTestLogService.insertSendTestLog(sendTestLog);

		Thread.sleep(2000);
		// return "redirect:/deviceDetail?dId=" + dId + "&name=" + name +
		// "&state=" + state;
		return "redirect:/success";

	}

	// SEND ACTION ONLY ON/OFF

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
			@RequestParam(value = "name") String name, @RequestParam(value = "dtId") String dtId,
			@RequestParam(value = "cmpCode") int cmpCode) throws Exception {

		logger.info("[insertDevice]");

		String subscriptionId = "";
		if (cmpCode == 1 && deviceService.getSubscriptionIdByDId(dId) != null
				&& !deviceService.getSubscriptionIdByDId(dId).equals("")) {
			subscriptionId = deviceService.getSubscriptionIdByDId(dId);
		} else {
			subscriptionId = ArtikUtils.createSubscription(session, session.getAttribute("userLoginInfo").toString(),
					dId);
		}

		Device device = new Device(session.getAttribute("userLoginInfo").toString(), dId, name, dtId, cmpCode,
				subscriptionId);
		device.setState(ArtikUtils.getDeviceState(session, dId));

		deviceService.insertDevice(device);

		logger.info("[insertDevice] name : {}, state : {}", name, device.getState());

		return "redirect:/success";
	}

	@RequestMapping("/colorLoop")
	public String colorLoop(HttpSession session) throws Exception {
		logger.info("[colorloop]");
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		for (Device d : deviceList) {
			if (d.getDtId().equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)) {
				logger.info("{}", d.getName());
				ArtikUtils.Action(session, d.getdId(), "setEffect", "");
				Thread.sleep(2000);
			}
		}

		return "redirect:/success";
	}

	@RequestMapping("/modalAction")
	public String modalAction(HttpSession session, @RequestParam(value = "modal_name") String name,
			@RequestParam(value = "modal_cmpCode") int cmpCode) throws Exception {

		logger.info("modalAction {}{}", name, cmpCode);
		String uId = session.getAttribute("userLoginInfo").toString();
		String dId = deviceService.getDIdByName(uId, name, cmpCode);
		PhilipsHueUtils.sendAction(session, "setOff", dId);
		return "redirect:/success";
	}

	@RequestMapping("/addNewDevice")
	public String addNewDevice(HttpSession session, @RequestParam(value = "dtId") String dtId,
			@RequestParam(value = "name") String name) throws Exception {

		ArtikUtils.addNewArtikDevice(session, name, dtId);

		return "deviceList";
	}

	@RequestMapping("/insertGroup")
	public String insertGroup(HttpSession session, @RequestParam(value = "dIds") String dIds,
			@RequestParam(value = "name") String name) throws Exception {

		logger.info("[insertGroup]");
		Device device = new Device(session.getAttribute("userLoginInfo").toString(), name, name, 0, "group", 4);

		String[] groupDIds = dIds.split(";");
		int flag = 0;
		for (String s : groupDIds) {
			DeviceGroup dg = new DeviceGroup(session.getAttribute("userLoginInfo").toString(), s, name);
			deviceService.insertDeviceGroup(dg);
			
			if (deviceService.getDeviceStateByDId(s, session.getAttribute("userLoginInfo").toString()) == 1)
				flag = 1;
		}
		if(flag == 1) device.setState(1);
		deviceService.insertDevice(device);
		return "redirect:/success";
	}

}
