package com.icontrols.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.icontrols.test.service.SendTestLogService;
import com.icontrols.test.service.UserService;
import com.icontrols.test.util.ArtikUtils;
import com.icontrols.test.util.IparkUtils;

/**
 * Handles requests for the application home page.
 */
@EnableScheduling
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
	SendTestLogService sendTestLogService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	Boolean stop = true;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String start(Locale locale, Model model) throws Exception {
		logger.info("[home]");
		stop = true;
		return "home";
	}

	@RequestMapping(value = "home")
	public String home(Locale locale, Model model) {
		logger.info("[home]");
		stop = true;
		return "home";
	}

	@RequestMapping(value = "deviceDetail")
	public String deviceDetail(HttpSession session, Model model, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int state, @RequestParam(value = "name") String name,
			@RequestParam(value = "cmpCode") String cmpCode) throws Exception {
		stop = true;
		logger.info("[deviceDetail]");
		model.addAttribute("dId", dId);
		model.addAttribute("state", state);
		model.addAttribute("name", name);
		model.addAttribute("cmpCode", cmpCode);

		return "deviceDetail";
	}

	@RequestMapping(value = "getDeviceState")
	@ResponseBody
	public String getDeviceState(HttpSession session, @RequestParam(value = "dId") String dId) throws Exception {
		stop = true;
		return ArtikUtils.getDeviceStateString(session, dId);
	}

	@RequestMapping(value = "getDidsByGId")
	@ResponseBody
	public List<Device> getDidsByGId(HttpSession session, Model model, @RequestParam(value = "dId") String dId)
			throws Exception {
		stop = true;
		List<String> groupDids = deviceService.getDeviceGroupDids(session.getAttribute("userLoginInfo").toString(),
				dId);
		List<Device> device = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		List<Device> finalDevice = new ArrayList<Device>();
		for (Device d : device) {
			for (String s : groupDids) {
				if (d.getdId().equals(s)) {
					finalDevice.add(d);
				}
			}
		}

		model.addAttribute("deviceList", finalDevice);
		return finalDevice;
	}

	/*
	 * move to join.jsp
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("joinPage")
	public ModelAndView joinPage() {
		stop = true;
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
		stop = true;
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
		stop = true;
		logger.info("[deviceList]");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("deviceList");
		return mav;
	}

	@RequestMapping("success")
	public ModelAndView success(HttpSession session, Model model) throws Exception {
		stop = true;
		logger.info("[success]");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("success");
		String uId = session.getAttribute("userLoginInfo").toString();
		List<Device> deviceList = deviceService.getDeviceById(uId);
		List<Device> groupList = new ArrayList<Device>();
		for (Device d : deviceList) {
			if (d.getCmpCode() == 4) {
				groupList.add(d);
			}
		}
		for (Device group : groupList) {
			List<String> groupMembers = deviceService.getDeviceGroupDids(uId, group.getdId());
			int stateCnt = 0;
			for (String dId : groupMembers) {
				int deviceState = deviceService.getDeviceStateByDId(dId, uId);
				if (deviceState != 0) {
					stateCnt++;
				}
			}
			if (stateCnt == 0) {
				deviceService.updateDeviceState(0, group.getdId(), uId);
			} else {
				deviceService.updateDeviceState(1, group.getdId(), uId);
			}
		}
		
		
		List<Device> finalList = deviceService.getDeviceById(uId);
		model.addAttribute("deviceList", finalList);

		return mav;
	}

	@RequestMapping("addDevice")
	public ModelAndView addDevice(Model model) {
		stop = true;
		logger.info("[addDevice]");
		ModelAndView mav = new ModelAndView();
		List<ConnectedCompany> connectedCompnayList = connectedCompanyService.getConnectedCompany();
		// 단지 SVR
		connectedCompnayList.remove(0);
		// Philips Hue SVR - TEST VER
		connectedCompnayList.remove(0);

		model.addAttribute("connectedCompanyList", connectedCompnayList);
		mav.setViewName("addDevice");
		return mav;
	}

	@RequestMapping("selectCompany")
	public String selectCompany(@RequestParam(value = "cmpCode") int cmpCode) {
		stop = true;
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
	public String deleteDevice(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "cmpCode") int cmpCode) throws Exception {

		String uId = session.getAttribute("userLoginInfo").toString();
		stop = true;
		logger.info("[deleteDevice]");
		if (cmpCode == 1 && deviceService.getSubscriptionCnt(dId) == 1) {
			logger.info("[deleteDevice] {}", deviceService.getSubscriptionIdByDId(dId));
			ArtikUtils.deleteSubscription(session, deviceService.getSubscriptionIdByDId(dId));
		}

		if (cmpCode == 4) {
			logger.info("[groupDelete] {}", dId);
			deviceService.deleteGroupDevice(dId);
		}

		List<String> gIds = deviceService.getGIdBydId(dId);
		if (gIds != null && gIds.size() != 0) {
			for (String gId : deviceService.getGIdBydId(dId)) {
				List<String> dIds = deviceService.getDeviceGroupDids(uId, gId);
				if (dIds.size() == 1) {
					deviceService.deleteDevice(uId, gId);
				}
			}
		}

		deviceService.deleteDeviceFromGroup(dId, uId);
		deviceService.deleteDevice(uId, dId);

		return "redirect:/success";
	}

	@RequestMapping(value = "sendActionTest")
	@ResponseBody
	public void sendActionTest(HttpSession session, @RequestParam(value = "dId") String dId,
			@RequestParam(value = "state") int currentState, @RequestParam(value = "cmpCode") int cmpCode)
			throws Exception {

		logger.info("[sendActionTest]");

		String uId = session.getAttribute("userLoginInfo").toString();
		SendTestLog sendTestLog = null;
		String action = "";
		if (currentState == 0) {
			action = "setOn";
		} else {
			action = "setOff";
		}

		if (cmpCode == 1) {
			sendTestLog = ArtikUtils.Action(session, dId, action, "", 0);
		} else if (cmpCode == 0) {
			sendTestLog = IparkUtils.sendAction(action, uId, dId,
					session.getAttribute("IPARK_ACCESS_TOKEN").toString());
			int state = 0;
			if (sendTestLog.getIparkState().equals("on")) {
				state = 1;
			} else if (sendTestLog.getIparkState().equals("off")) {
				state = 0;
			}
			// List<String> groupList = deviceService.getGIdBydId(dId);
			// if (groupList != null) {
			// for (String s : groupList) {
			// logger.info("{}", s);
			// deviceService.updateGroupState(0, s);
			// int oNCnt = 0;
			// for (String dIds :
			// deviceService.getDeviceGroupDids(deviceService.getUIdByDId(s),
			// s)) {
			// logger.info("{}", dIds);
			// if (deviceService.getDeviceStateByDId(dIds, uId) == 0) {
			// oNCnt++;
			// }
			// }
			// logger.info("{}", oNCnt);
			// if (state == 0 && oNCnt == 1) {
			// logger.info("setOff{}", s);
			// deviceService.updateGroupState(state, s);
			// } else {
			// deviceService.updateGroupState(1, s);
			// }
			// }
			// }

			deviceService.updateDeviceState(state, dId, uId);

		} else if (cmpCode == 4) {

			List<String> groupDIds = deviceService.getDeviceGroupDids(uId, dId);

			for (String s : groupDIds) {

				Integer deviceType = deviceService.getDeviceCmpCode(s, uId);
				logger.info("{} : {}", deviceType, s);

				if (deviceType == 1) {
					sendTestLog = ArtikUtils.Action(session, s, action, "", 0);
				} else if (deviceType == 0) {
					sendTestLog = IparkUtils.sendAction(action, uId, s,
							session.getAttribute("IPARK_ACCESS_TOKEN").toString());

					int state = 0;
					if (sendTestLog.getIparkState().equals("on")) {
						state = 1;
					} else if (sendTestLog.getIparkState().equals("off")) {
						state = 0;
					}
					deviceService.updateDeviceState(state, s, uId);
				}
				Thread.sleep(2000);
			}
		}

	}

	@RequestMapping("/allOff")
	@ResponseBody
	public void allOff(HttpSession session) throws Exception {

		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		String uId = session.getAttribute("userLoginInfo").toString();
		for (Device d : deviceList) {
			logger.info("all Off {}", d.getName());
			SendTestLog sendTestLog = null;
			if (d.getCmpCode() != 4) {
				if (d.getState() == 1) {
					if (d.getCmpCode() == 1) {
						sendTestLog = ArtikUtils.Action(session, d.getdId(), "setOff", "", 0);
					} else if (d.getCmpCode() == 0) {
						sendTestLog = IparkUtils.sendAction("setOff", uId, d.getdId(),
								session.getAttribute("IPARK_ACCESS_TOKEN").toString());
						int action = 0;
						if(sendTestLog.getIparkState().equals("on")){
							action = 1;
						}
						deviceService.updateDeviceState(action, d.getdId(), uId);
					}
					sendTestLogService.insertSendTestLog(sendTestLog);
				}
				Thread.sleep(2000);
			}
		}
	}

	@RequestMapping("/allOn")
	@ResponseBody
	public void allOn(HttpSession session) throws Exception {

		List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		String uId = session.getAttribute("userLoginInfo").toString();
		for (Device d : deviceList) {
			SendTestLog sendTestLog = null;
			if (d.getCmpCode() != 4) {
				logger.info("allOn {}", d.getName());
				if (d.getState() == 0) {
					if (d.getCmpCode() == 1) {
						sendTestLog = ArtikUtils.Action(session, d.getdId(), "setOn", "", 0);
						
					} else if (d.getCmpCode() == 0) {
						sendTestLog = IparkUtils.sendAction("setOn", uId, d.getdId(),
								session.getAttribute("IPARK_ACCESS_TOKEN").toString());
						int action = 0;
						if(sendTestLog.getIparkState().equals("on")){
							action = 1;
						}
						deviceService.updateDeviceState(action, d.getdId(), uId);
					}
					sendTestLogService.insertSendTestLog(sendTestLog);
				}
				Thread.sleep(2000);
			}
		}
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
			HttpSession session, Model model) throws Exception {

		stop = true;
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

			// AccessToken을 이미 받은 경우 ACCESS_TOKEN 과 ARTIK_USER_ID 을 세션에 저장
			if (accessTokenService.getAccessTokenById(uId) != null
					&& !accessTokenService.getAccessTokenById(uId).equals("")) {
				session.setAttribute("ACCESS_TOKEN", accessTokenService.getAccessTokenById(uId));
				session.setAttribute("ARTIK_USER_ID",
						artikUserProfileService.getUserIdById(session.getAttribute("userLoginInfo").toString()));
				logger.info("[login] get ACCESS_TOKEN : {}", accessTokenService.getAccessTokenById(uId));
			}

			session.setAttribute("IPARK_ACCESS_TOKEN", iparkAccessTokenService.getIparkAccessTokenById(uId));

			logger.info("[login] get IPARK_ACCESS_TOKEN : {}", iparkAccessTokenService.getIparkAccessTokenById(uId));

			List<Device> deviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
			List<Device> artikDevice = new ArrayList<Device>();
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
					} else if (d.getCmpCode() == 4) {
						finalDevice.add(d);
					}
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

			List<Device> groupList = deviceService.getGroupByUId(uId);
			if (groupList != null) {
				for (Device d : groupList) {
					deviceService.updateGroupState(0, d.getdId());
					for (String dId : deviceService.getDeviceGroupDids(uId, d.getdId())) {
						logger.info("[login] {}", deviceService.getDeviceStateByDId(dId, uId));
						if (deviceService.getDeviceStateByDId(dId, uId) == 1) {
							deviceService.updateGroupState(1, d.getdId());
						}
					}
				}
			}

			return "redirect:/success";

		} else {
			logger.info("[LOGIN FAIL]");
			return "redirect:/";

		}

	}

	@RequestMapping("logout")
	public ModelAndView logout(HttpSession session, Model model) {
		stop = true;
		ModelAndView mv = new ModelAndView("home", "error_message", "로그인 후 이용바랍니다:D");
		session.invalidate();
		return mv;
	}

	@RequestMapping("refresh")
	public String refresh(HttpSession session) throws Exception {
		stop = true;

		logger.info("[refresh]");

		String uId = session.getAttribute("userLoginInfo").toString();
		List<Device> deviceList = deviceService.getDeviceById(uId);
		List<Device> artikDevice = new ArrayList<Device>();
		List<Device> groupList = new ArrayList<Device>();
		if (deviceList.size() != 0) {
			for (Device d : deviceList) {
				if (d.getCmpCode() == 0) {
					int state = IparkUtils.getState(d, session.getAttribute("IPARK_ACCESS_TOKEN").toString());
					if (IparkUtils.stateChangeFlag == 1) {
						deviceService.updateDeviceState(state, d.getdId(), uId);
					}
				} else if (d.getCmpCode() == 1) {
					artikDevice.add(d);
				} else if (d.getCmpCode() == 4) {
					groupList.add(d);
				}
			}

			if (session.getAttribute("ACCESS_TOKEN") != null && artikDevice.size() != 0) {
				List<Device> result = ArtikUtils.getDeviceState(session, artikDevice);
				if (ArtikUtils.stateChangeFlag == 1) {
					for (Device d : result) {
						deviceService.updateDeviceState(d.getState(), d.getdId(), uId);
					}
				}
			}
		}

		if (groupList != null && groupList.size() != 0) {
			for (Device d : groupList) {
				deviceService.updateGroupState(0, d.getdId());
				for (String dId : deviceService.getDeviceGroupDids(uId, d.getdId())) {
					if (deviceService.getDeviceStateByDId(dId, uId) == 1) {
						deviceService.updateGroupState(1, d.getdId());
					}
				}
			}
		}

		return "redirect:/success";
	}

	@RequestMapping("createGroup")
	public ModelAndView createGroup(HttpSession session, Model model) {
		stop = true;
		ModelAndView mav = new ModelAndView();
		mav.setViewName("createGroup");
		String uId = session.getAttribute("userLoginInfo").toString();
		List<Device> deviceList = deviceService.getDeviceById(uId);
		List<Device> finalDevice = new ArrayList<Device>();
		for (Device d : deviceList) {
			if (d.getCmpCode() != 4) {
				finalDevice.add(d);
			}
		}
		model.addAttribute("deviceList", finalDevice);

		return mav;
	}

	@RequestMapping("thread")
	public String thread(HttpSession session) throws Exception {

		stop = true;
		String userId = session.getAttribute("userLoginInfo").toString();
		logger.info("{} thread", userId);
		List<Device> oldList = deviceService.getDeviceById(userId);
		List<Device> newList = new ArrayList<Device>();
		stop = false;
		while (!stop) {

			try {
				newList.clear();
				newList = deviceService.getDeviceById(userId);
				if (oldList.size() != 0 && newList.size() != 0) {

					if (oldList.size() == newList.size()) {
						for (int j = 0; j < oldList.size(); j++) {
							for (int i = 0; i < newList.size(); i++) {

								if (oldList.get(j).getCmpCode() != 4
										&& oldList.get(j).getdId().equals(newList.get(i).getdId())) {
									// logger.info("{} thread", userId);
									// logger.info(" {}'s old state {} ",
									// oldList.get(j).getName(),
									// oldList.get(j).getState());
									// logger.info(" {}'s new state {}",
									// newList.get(j).getName(),
									// newList.get(j).getState());
									// logger.info(
									// "==============================================================================");
									if (oldList.get(j).getState() != newList.get(i).getState()) {
										logger.info("[DB change]");
										oldList.clear();
										oldList.addAll(newList);
										stop = true;

									}
								}
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(500);
		}

		return "redirect:/success";
	}

	@Scheduled(cron = "0 0 11 * * ?") // 매일 오후3시에 AccessToken Validate 검사
	public void scheduleRun() throws Exception {
		logger.info("Token Check Scheduler START");

		List<AccessToken> accessTokenList = accessTokenService.getAllAccessToken();
		for (AccessToken at : accessTokenList) {
			logger.info(" {} 's old token {}", at.getuId(), at.getAccess_token());

			if (Long.parseLong(at.getIssuedTime()) + Long.parseLong(at.getExpires_in()) > System.currentTimeMillis()
					+ 86500) {
				AccessToken accessToken = ArtikUtils.refreshAccessToken(at.getRefresh_token());
				accessToken.setuId(at.getuId());
				accessTokenService.updateAccessToken(accessToken);
				logger.info(" {} 's new token {}", at.getuId(), accessToken.getAccess_token());
			}
		}

		List<IparkAccessToken> iparkAccessTokenList = iparkAccessTokenService.getAllIparkAccessToken();
		for (IparkAccessToken iat : iparkAccessTokenList) {
			logger.info(" {} 's old token {}", iat.getuId(), iat.getAccessToken());
			if (Long.parseLong(iat.getIssuedTime()) + Long.parseLong(iat.getExpiresIn()) > System.currentTimeMillis()
					+ 86500) {
				IparkAccessToken iparkAccessToken = IparkUtils.getIparkAccessToken(iat.getuId());
				iparkAccessToken.setuId(iat.getuId());
				iparkAccessTokenService.updateIparkAccessToken(iparkAccessToken);
				logger.info(" {} 's new token {}", iat.getuId(), iparkAccessToken.getAccessToken());
			}
		}
	}
}
