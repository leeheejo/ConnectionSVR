package com.icontrols.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.icontrols.test.domain.Device;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.PhilipsHueBridgeService;
import com.icontrols.test.util.PhilipsHueUtils;

@Controller
public class PhilipsHueController {

	private static final Logger logger = LoggerFactory.getLogger(PhilipsHueController.class);
	@Autowired
	PhilipsHueBridgeService philipsHueBridgeService;
	@Autowired
	DeviceService deviceService;

	@RequestMapping("philipsHueLogin")
	public String philipsHueLogin(HttpSession session, Model model) throws Exception {

		if (session.getAttribute("PHILIPS_HUE_BRIDGE_IP") == null) {
			return "philipsHueBridgeIp";
		} else {
			List<Device> deviceList = PhilipsHueUtils.getDeviceList(session);
			List<Device> userDeviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
			List<Device> newDeviceList = new ArrayList<Device>();

			for (Device dl : deviceList) {
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

			model.addAttribute("philipsHueDeviceList", newDeviceList);
			
			return "philipsHueDeviceList";
		}

	}

	@RequestMapping("bridgeIp")
	public String setBridgeIp(Model model, HttpSession session, @RequestParam(value = "bridgeIp") String bridgeIp)
			throws Exception {
		if (bridgeIp != null && !bridgeIp.equals("")) {
		
			String uId = session.getAttribute("userLoginInfo").toString();
			if (philipsHueBridgeService.getPhilipsHueBridgeById(uId) != null) {
				philipsHueBridgeService.updatePhilipsHueBridge(bridgeIp, uId);
			} else {
				if (!philipsHueBridgeService.getPhilipsHueBridgeById(uId).equals(bridgeIp)) {
					philipsHueBridgeService.insertPhilipsHueBridge(bridgeIp, uId);
				}
			}

			return "redirect:/philipsHueLogin";

		} else {
			return "philipsHueBridgeIp";
		}

	}

}
