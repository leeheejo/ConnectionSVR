package com.icontrols.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.domain.ConnectedCompany;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.IparkAccessToken;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.service.ArtikUserProfileService;
import com.icontrols.test.service.ConnectedCompanyService;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.IparkAccessTokenService;
import com.icontrols.test.service.PhilipsHueBridgeService;
import com.icontrols.test.service.SendTestLogService;
import com.icontrols.test.service.UserService;
import com.icontrols.test.util.ArtikUtils;
import com.icontrols.test.util.IparkUtils;
import com.icontrols.test.util.PhilipsHueUtils;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	UserService UserService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	ConnectedCompanyService connectedCompanyService;
	@Autowired
	AccessTokenService accessTokenService;
	@Autowired
	ArtikUserProfileService artikUserProfileService;
	@Autowired
	IparkAccessTokenService iparkAccessTokenService;
	@Autowired
	PhilipsHueBridgeService philipsHueBridgeService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String start(Locale locale, Model model) {

		logger.info("[HOME]");

		return "home";
	}

	@RequestMapping(value = "home")
	public String home(Locale locale, Model model) {

		return "home";
	}

	/*
	 * move to join.jsp
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("joinPage")
	public ModelAndView joinPage() {
		logger.info("[joinPage]");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("join");
		return mav;
	}

	/*
	 * insert DB and move to home.jsp
	 * 
	 * @RequestParam uId (String) user's ID
	 * 
	 * @RequestParam Pwd (String) user's Password
	 * 
	 * @RequestParam uPhone (String) user's PhoneNumber
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("join")
	public ModelAndView join(@RequestParam(value = "uId") String uId, @RequestParam(value = "uPwd") String uPwd,
			@RequestParam(value = "uEmail") String uEmail) throws Exception {

		logger.info("[join]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");

		IparkAccessToken IparkAccessToken = IparkUtils.getIparkAccessToken(uId);
		iparkAccessTokenService.insertIparkAccessToken(IparkAccessToken);

		Device defaultDevice = IparkUtils.getIparkInfo(IparkAccessToken.getAccessToken(), uId);

		defaultDevice.setCmpCode(0);
		deviceService.insertDevice(defaultDevice);
		UserService.insertUser(uId, uPwd, uEmail);
		return mav;
	}

	@RequestMapping("deviceList")
	public ModelAndView deviceList() {
		logger.info("[deviceList]");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("deviceList");
		return mav;
	}

	@RequestMapping("refresh")
	public String refresh() {
		logger.info("[refresh]");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("refresh");
		return "redirect:/success";
	}

	@RequestMapping("success")
	public ModelAndView success(HttpSession session, Model model) throws Exception {
		logger.info("[success]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("success");
		String uId = session.getAttribute("userLoginInfo").toString();
		logger.info("[success]userLoginInfo : {}", session.getAttribute("userLoginInfo").toString());

		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		List<Device> artikDevice = new ArrayList<Device>();
		List<Device> hueDevice = new ArrayList<Device>();
		List<Device> finalDevice = new ArrayList<Device>();
		if (deviceList.size() != 0) {
			for (Device d : deviceList) {
				if (d.getCmpCode() == 0) {
					int state = IparkUtils.getState(d, session.getAttribute("IPARK_ACCESS_TOKEN").toString());
					if (IparkUtils.stateChangeFlag == 1) {
						deviceService.updateDeviceState(state, d.getdId(), uId);
					}
					finalDevice.add(d);
				} else if (d.getCmpCode() == 1) {
					artikDevice.add(d);
				} else if (d.getCmpCode() == 2) {
					hueDevice.add(d);
				}
			}

			if (hueDevice.size() != 0) {
				List<Device> hueResult = PhilipsHueUtils.getDeviceState(session, hueDevice);
				if (PhilipsHueUtils.stateChangeFlag == 1) {
					for (Device d : hueResult) {
						deviceService.updateDeviceState(d.getState(), d.getdId(), uId);
					}
				}
				finalDevice.addAll(hueResult);
			}

			if (session.getAttribute("ACCESS_TOKEN") != null && artikDevice.size() != 0) {
				List<Device> result = ArtikUtils.getDeviceState(session, artikDevice);
				if (ArtikUtils.stateChangeFlag == 1) {
					for (Device d : result) {
						deviceService.updateDeviceState(d.getState(), d.getdId(), uId);
					}
				}
				finalDevice.addAll(result);
			}
		}

		model.addAttribute("deviceList", deviceList);

		return mav;
	}

	@RequestMapping("addDevice")
	public ModelAndView addDevice(Model model) {
		logger.info("[addDevice]");
		ModelAndView mav = new ModelAndView();
		List<ConnectedCompany> connectedCompnayList = connectedCompanyService.getConnectedCompany();
		//단지 SVR
		connectedCompnayList.remove(0);
		//Philips Hue SVR - TEST VER
		connectedCompnayList.remove(0);
		
		model.addAttribute("connectedCompanyList", connectedCompnayList);
		mav.setViewName("addDevice");
		return mav;
	}

	@RequestMapping("selectCompany")
	public String selectCompany(@RequestParam(value = "cmpCode") int cmpCode) {
		logger.info("[addDevice]");
		String s = "";

		if (cmpCode == 1) {
			s = "redirect:/artikLogin";
		} else if (cmpCode == 2) {
			s = "redirect:/philipsHueLogin";
		}

		return s;
	}

	@RequestMapping("deleteDevice")
	public String deleteDevice(HttpSession session, @RequestParam(value = "dId") String dId) {
		logger.info("[deleteDevice]");
		deviceService.deleteDevice(session.getAttribute("userLoginInfo").toString(), dId);

		return "redirect:/success";
	}

	/*
	 * selectDB for login and if(correct) move to success.jsp else move to
	 * fail.jsp
	 * 
	 * @RequestParam uId (String) user's ID
	 * 
	 * @RequestParamu Pwd (String) user's Password
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("login")
	public String login(@RequestParam(value = "uId") String uId, @RequestParam(value = "uPwd") String uPwd,
			HttpSession session, Model model) {
		logger.info("[login]");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		map.put("uPwd", uPwd);
		/*
		 * to check user's Id and user's pwd
		 * 
		 * @return success : return 1 / fail : return 0
		 */
		int loginCheck = UserService.loginCheck(map);
		
		if (loginCheck == 1) {
			session.setAttribute("userLoginInfo", uId);
			logger.info("[login]login success : {}", session.getAttribute("userLoginInfo").toString());

			List<Device> deviceList = deviceService.getDeviceById(uId);
			model.addAttribute("deviceList", deviceList);

			// AccessToken을 이미 받은 경우 ACCESS_TOKEN 과 ARTIK_USER_ID 을 세션에 저장
			if (accessTokenService.getAccessTokenById(uId) != null
					&& !accessTokenService.getAccessTokenById(uId).equals("")) {
				session.setAttribute("ACCESS_TOKEN", accessTokenService.getAccessTokenById(uId));
				session.setAttribute("ARTIK_USER_ID",
						artikUserProfileService.getUserIdById(session.getAttribute("userLoginInfo").toString()));
				logger.info("[login] get ACCESS_TOKEN : {}", accessTokenService.getAccessTokenById(uId));
			}

			if (philipsHueBridgeService
					.getPhilipsHueBridgeById(session.getAttribute("userLoginInfo").toString()) != null) {
				session.setAttribute("PHILIPS_HUE_BRIDGE_IP", philipsHueBridgeService
						.getPhilipsHueBridgeById(session.getAttribute("userLoginInfo").toString()));
				session.setAttribute("PHILIPS_HUE_USERNAME", philipsHueBridgeService
						.getPhilipsHueUsernameById(session.getAttribute("userLoginInfo").toString()));
			}
			logger.info("[login] get IPARK_ACCESS_TOKEN : {}", session.getAttribute("PHILIPS_HUE_BRIDGE_IP"));
			session.setAttribute("IPARK_ACCESS_TOKEN", iparkAccessTokenService.getIparkAccessTokenById(uId));

			logger.info("[login] get IPARK_ACCESS_TOKEN : {}", iparkAccessTokenService.getIparkAccessTokenById(uId));

			return "redirect:/success";

		} else {
			logger.info("[LOGIN FAIL]");
			return "redirect:/";

		}

	}

	@RequestMapping("logout")
	public ModelAndView logout(HttpSession session, Model model) {
		ModelAndView mv = new ModelAndView("home", "error_message", "로그인 후 이용바랍니다:D");
		session.invalidate();

		return mv;
	}

}
