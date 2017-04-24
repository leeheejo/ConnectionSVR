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
import com.icontrols.test.service.PhilipsHueBridgeService;
import com.icontrols.test.util.IparkUtils;

@Controller
public class PhilipsHueController {

	private static final Logger logger = LoggerFactory.getLogger(PhilipsHueController.class);
	@Autowired
	PhilipsHueBridgeService philipsHueBridgeService;

	@RequestMapping("philipsHueLogin")
	public String philipsHueLogin(HttpSession session) {

		return "philipsHueBridgeIp";
	}

	@RequestMapping("bridgeIp")
	public String setBridgeIp(Model model, HttpSession session, @RequestParam(value = "bridgeIp") String bridgeIp) throws Exception {

		// philipsHueBridgeService.insertPhilipsHueBridge(bridgeIp,
		// session.getAttribute("userLoginInfo").toString());
		// httpGET Ελ½Ε
		URL url = new URL("http://192.168.101.20/api/TGKOH73g4LBDRwZIW0tipRSzagbkZLT7h7Q7AOBw/lights");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getArtikDeviceList] responseCode : {}", responseCode + con.getResponseMessage());

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikDeviceList] responseData : {}", responseData);
		br.close();

		List<Device> deviceList = parsingDeviceList(responseData, session.getAttribute("userLoginInfo").toString());
		model.addAttribute("philipsHueDeviceList", deviceList);
		
		return "philipsHueDeviceList";
	}

	public static List<Device> parsingDeviceList(String jsonMsg, String uId) {

		List<Device> deviceList = new ArrayList<Device>();

		JSONObject obj = new JSONObject(jsonMsg);

		for (int i = 0; i < obj.length(); i++) {
			String s = (i+1)+"";
			JSONObject device = obj.getJSONObject(s);
			String dtId = device.getString("modelid");
			String name = device.getString("name");
			String dId = s;

			Device d = new Device(uId, dId, name, dtId, 2);
			deviceList.add(d);
		}

		return deviceList;
	}

}
