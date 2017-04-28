package com.icontrols.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	@RequestMapping(value = "callback")
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

	@RequestMapping("/getArtikDeviceList")
	public String getArtikDeviceList(HttpSession session, Model model) throws Exception {

		logger.info("[getArtikDeviceList]");

		List<Device> artikDeviceList = ArtikUtils.getArtikDeviceList(session);
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
	public List<Device> getDeviceListAjax(HttpSession session, Model model) throws Exception {

		logger.info("[getArtikDeviceList]");

		List<Device> artikDeviceList = ArtikUtils.getArtikDeviceList(session);
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
//		model.addAttribute("artikDeviceList", newDeviceList);

		return newDeviceList ;
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
	//
	// logger.info("[sendActionTest]");
	// String dtId = deviceService.getDeviceTypeId(dId,
	// session.getAttribute("userLoginInfo").toString());
	// SendTestLog sendTestLog;
	//
	// if (!dtId.equals("main_light")) {
	// if (dtId.equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)
	// && (!R.equals("") || !G.equals("") || !B.equals(""))) {
	// if (R.equals("") || Integer.parseInt(R) > 255)
	// R = "0";
	// if (G.equals("") || Integer.parseInt(G) > 255)
	// G = "0";
	// if (B.equals("") || Integer.parseInt(B) > 255)
	// B = "0";
	// sendTestLog = ArtikUtils.Action(session, dId, "setColorRGB", R + ";" + G
	// + ";" + B);
	//
	// } else if (dtId.equals("main_light")) {
	// String action = "";
	// if (currentState == 0) {
	// action = "setOn";
	// } else {
	// action = "setOff";
	// }
	// sendTestLog = IparkUtils.sendAction(action,
	// session.getAttribute("userLoginInfo").toString(), dId);
	// } else {
	// String action = "";
	// if (currentState == 0) {
	// action = "setOn";
	// } else {
	// action = "setOff";
	// }
	// sendTestLog = ArtikUtils.Action(session, dId, action, "");
	// }
	// } else {
	// String action = "";
	// if (currentState == 0) {
	// action = "setOn";
	// } else {
	// action = "setOff";
	// }
	// sendTestLog = IparkUtils.sendAction(action,
	// session.getAttribute("userLoginInfo").toString(), dId);
	// }
	//
	// sendTestLogService.insertSendTestLog(sendTestLog);
	//
	// return "redirect:/success";
	//
	// }

	// SEND ACTION ONLY ON/OFF

	@RequestMapping("/sendActionTest")
	public String sendActionTest(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int currentState, @RequestParam(value = "cmpCode") int cmpCode)
			throws Exception {
		logger.info("[sendActionTest]");
		String dtId = deviceService.getDeviceTypeId(dId, session.getAttribute("userLoginInfo").toString());
		SendTestLog sendTestLog = null;
		String action = "";
		if (currentState == 0) {
			action = "setOn";
		} else {
			action = "setOff";
		}

		if (cmpCode == 1) {
			sendTestLog = ArtikUtils.Action(session, dId, action, "");
		} else if (cmpCode == 0) {
			sendTestLog = IparkUtils.sendAction(action, session.getAttribute("userLoginInfo").toString(), dId,
					session.getAttribute("IPARK_ACCESS_TOKEN").toString());
		} else if (cmpCode == 2) {
			
			sendTestLog = PhilipsHueUtils.sendAction(session, action, dId);
		}

		sendTestLogService.insertSendTestLog(sendTestLog);

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
			@RequestParam(value = "name") String name, @RequestParam(value = "dtId") String dtId,
			@RequestParam(value = "cmpCode") int cmpCode) throws Exception {

		logger.info("[insertDevice]");
		Device device = new Device(session.getAttribute("userLoginInfo").toString(), dId, name, dtId, cmpCode);
		deviceService.insertDevice(device);

		logger.info("[insertDevice] dId : {}, name : {}", dId, name);

		return "redirect:/success";
	}

	@RequestMapping("/allOff")
	public String allOff(HttpSession session) throws Exception {
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		String uId = session.getAttribute("userLoginInfo").toString();
		for (Device d : deviceList) {
			logger.info("{}", d.getName());
			SendTestLog sendTestLog = null;

			if (d.getState() == 1) {
				if (d.getCmpCode() == 1) {
					sendTestLog = ArtikUtils.Action(session, d.getdId(), "setOff", "");
				} else if (d.getCmpCode() == 0) {
					sendTestLog = IparkUtils.sendAction("setOff", uId, d.getdId(),
							session.getAttribute("IPARK_ACCESS_TOKEN").toString());
				} else if (d.getCmpCode() == 2) {
					sendTestLog = PhilipsHueUtils.sendAction(session,"setOff", d.getdId());
				}
				sendTestLogService.insertSendTestLog(sendTestLog);
			}
		}
		return "redirect:/success";
	}

	@RequestMapping("/allOn")
	public String allOn(HttpSession session) throws Exception {
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		String uId = session.getAttribute("userLoginInfo").toString();
		for (Device d : deviceList) {
			SendTestLog sendTestLog = null;
			logger.info("{}", d.getName());
			if (d.getState() == 0) {
				if (d.getCmpCode() == 1) {
					sendTestLog = ArtikUtils.Action(session, d.getdId(), "setOn", "");
				} else if (d.getCmpCode() == 0) {
					sendTestLog = IparkUtils.sendAction("setOn", uId, d.getdId(),
							session.getAttribute("IPARK_ACCESS_TOKEN").toString());
				} else if (d.getCmpCode() == 2) {
					sendTestLog = PhilipsHueUtils.sendAction(session, "setOn",d.getdId());
				}
				sendTestLogService.insertSendTestLog(sendTestLog);
			}
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
				ArtikUtils.Action(session, d.getdId(), "setEffect", "");
				Thread.sleep(2000);
			}
		}

		return "redirect:/success";
	}

	@RequestMapping("/modalAction")
	public String modalAction(HttpSession session,@RequestParam(value="modal_name") String name, @RequestParam(value ="modal_cmpCode") int cmpCode) throws Exception {
		
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

	@RequestMapping("/createSubscription")
	public String createSubscription(HttpSession session) throws Exception {

		ArtikUtils.createSubscription(session);

		return "deviceList";
	}

}
