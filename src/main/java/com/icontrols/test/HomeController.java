package com.icontrols.test;

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

import com.icontrols.test.domain.ConnectedCompany;
import com.icontrols.test.domain.Device;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.service.ArtikUserProfileService;
import com.icontrols.test.service.ConnectedCompanyService;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.UserService;
import com.icontrols.test.util.ArtikUtils;

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

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		logger.info("[HOME]");

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
			@RequestParam(value = "uEmail") String uEmail) {

		logger.info("[join]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		map.put("uPwd", uPwd);
		map.put("uEmail", uEmail);

		UserService.insertUser(map);
		return mav;
	}

	/*
	 * move to login.jsp
	 * 
	 * @return ModelAndView
	 */
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
		
		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		
		for (Device d : deviceList) {
			String dtId = deviceService.getDeviceTypeId(d.getdId(), session.getAttribute("userLoginInfo").toString());
			Integer state = ArtikUtils.getDeviceState(session, d.getdId(), dtId);
			if (state != null) {
				logger.info("[success] state {}", state);
				deviceService.updateDeviceState(state, d.getdId());
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
		model.addAttribute("connectedCompanyList", connectedCompnayList);
		mav.setViewName("addDevice");
		return mav;
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
			List<Device> deviceList = deviceService.getDeviceById(uId);
			model.addAttribute("deviceList", deviceList);

			// AccessToken을 이미 받은 경우 ACCESS_TOKEN 과 ARTIK_USER_ID 을 세션에 저장
			if (accessTokenService.getAccessTokenById(uId) != null) {
				session.setAttribute("ACCESS_TOKEN", accessTokenService.getAccessTokenById(uId));
				session.setAttribute("ARTIK_USER_ID",
						artikUserProfileService.getUserIdById(session.getAttribute("userLoginInfo").toString()));
				logger.info("[login] get ACCESS_TOKEN : {}", accessTokenService.getAccessTokenById(uId));
			}
			return "success";
		} else {
			logger.info("[LOGIN FAIL]");
			return "redirect:/";

		}

	}

}
