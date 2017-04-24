package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceList;
import com.icontrols.test.service.DeviceService;
import com.icontrols.test.service.PhilipsHueBridgeService;
import com.icontrols.test.util.IparkUtils;
import com.icontrols.test.util.PhilipsHueUtils;

@Controller
public class PhilipsHueController {

	private static final Logger logger = LoggerFactory.getLogger(PhilipsHueController.class);
	@Autowired
	PhilipsHueBridgeService philipsHueBridgeService;
	@Autowired
	DeviceService deviceService;

	@RequestMapping("philipsHueLogin")
	public String philipsHueLogin(HttpSession session) {

		return "philipsHueBridgeIp";
	}

	@RequestMapping("bridgeIp")
	public String setBridgeIp(Model model, HttpSession session, @RequestParam(value = "bridgeIp") String bridgeIp)
			throws Exception {
		String uId = session.getAttribute("userLoginInfo").toString();
		if (philipsHueBridgeService.getPhilipsHueBridgeById(uId) != null) {
			philipsHueBridgeService.updatePhilipsHueBridge(bridgeIp, uId);
		} else {
			if (!philipsHueBridgeService.getPhilipsHueBridgeById(uId).equals(bridgeIp)) {
				philipsHueBridgeService.insertPhilipsHueBridge(bridgeIp, uId);
			}
		}

		String PhilipsHueURL = "http://" + session.getAttribute("PHILIPS_HUE_BRIDGE_IP").toString();
		List<Device> deviceList = PhilipsHueUtils.getDeviceList(PhilipsHueURL,
				session.getAttribute("userLoginInfo").toString());
		List<Device> userDeviceList = deviceService.getDeviceById(session.getAttribute("userLoginInfo").toString());
		List<Device> newDeviceList = new ArrayList();

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
