package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.PhilipsHueBridge;
import com.icontrols.test.domain.SendTestLog;

public class PhilipsHueUtils {

	private static final Logger logger = LoggerFactory.getLogger(PhilipsHueUtils.class);
	// private static String PhilipsHueURL = "http://192.168.101.20";
	// private static String username =
	// "TGKOH73g4LBDRwZIW0tipRSzagbkZLT7h7Q7AOBw";
	public static int stateChangeFlag;

	public static List<Device> getDeviceList(HttpSession session) throws Exception {
		String PhilipsHueURL = "http://" + session.getAttribute("PHILIPS_HUE_BRIDGE_IP").toString();
		String PhilipsUsername = session.getAttribute("PHILIPS_HUE_USERNAME").toString();
		String uId = session.getAttribute("userLoginInfo").toString();

		URL url = new URL(PhilipsHueURL + "/api/" + PhilipsUsername + "/lights");
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
		List<Device> deviceList = parsingDeviceList(responseData, uId);

		return deviceList;
	}

	public static SendTestLog sendAction(HttpSession session, String action, String dId) throws Exception {

		String PhilipsHueURL = "http://" + session.getAttribute("PHILIPS_HUE_BRIDGE_IP").toString();
		String PhilipsUsername = session.getAttribute("PHILIPS_HUE_USERNAME").toString();
		String uId = session.getAttribute("userLoginInfo").toString();

		logger.info("[sendAction]");
		// httpGET ���
		URL url = new URL(PhilipsHueURL + "/api/" + PhilipsUsername + "/lights/" + dId + "/state");
		logger.info("{}", url.getContent().toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("PUT");
		con.setDoOutput(true);
		con.setDoInput(true);

		String param = "";
		if (action.equals("setOn")) {
			param = "{\"on\":true}";
		} else {
			param = "{\"on\":false}";
		}

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();
		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[sendAction] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[sendAction] responseData : {}", responseData);

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(uId, 0, dId, action, responseCode + "",
				new Date(System.currentTimeMillis()));

		return sendTestLog;
	}

	public static List<Device> getDeviceState(HttpSession session, List<Device> deviceList) throws Exception {

		logger.info("[getDeviceState]");

		String PhilipsHueURL = "http://" + session.getAttribute("PHILIPS_HUE_BRIDGE_IP").toString();
		String PhilipsUsername = session.getAttribute("PHILIPS_HUE_USERNAME").toString();

		List<Device> device = new ArrayList<Device>();
		stateChangeFlag = 0;

		for (Device d : deviceList) {
			logger.info("[getDeviceState] DID : {}", d.getdId());
			URL url = new URL(PhilipsHueURL + "/api/" + PhilipsUsername + "/lights/" + d.getdId());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);

			// Response Code
			int responseCode = con.getResponseCode();
			logger.info("[messages] responseCode : {}", responseCode + con.getResponseMessage());

			// Response Data
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String responseData = br.readLine();
			logger.info("[messages] responseData : {}", responseData);
			br.close();

			// device data~
			JSONObject obj = new JSONObject(responseData);
			JSONObject state = obj.getJSONObject("state");
			Boolean onOff = state.getBoolean("on");
			int currentState;
			if (onOff == true)
				currentState = 1;
			else
				currentState = 0;

			if (d.getState() != currentState) {
				stateChangeFlag = 1;
				d.setState(currentState);
			}
			logger.info("{}{}", onOff, currentState);
			device.add(d);
		}

		return device;
	}

	public static List<Device> parsingDeviceList(String jsonMsg, String uId) {

		List<Device> deviceList = new ArrayList<Device>();

		JSONObject obj = new JSONObject(jsonMsg);

		for (int i = 0; i < obj.length(); i++) {
			String s = (i + 1) + "";
			JSONObject device = obj.getJSONObject(s);
			String dtId = device.getString("modelid");
			String name = device.getString("name");
			String dId = s;

			Device d = new Device(uId, dId, name, dtId, 2);
			deviceList.add(d);
		}

		return deviceList;
	}

	public static void authorize1(String bridgeIp) throws Exception {

		String PhilipsHueURL = "http://" + bridgeIp;
		URL url = new URL(PhilipsHueURL + "/api/newdeveloper");
		logger.info("{}", url.getContent().toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// response Code
		int responseCode = con.getResponseCode();
		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[authorize1] responseData: {} - {}", responseCode, responseData);
	}

	public static void authorize2(String bridgeIp, int step) throws Exception {

		String PhilipsHueURL = "http://" + bridgeIp;
		URL url = new URL(PhilipsHueURL + "/api");
		logger.info("{}", url.getContent().toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		
		String param = "{\"devicetype\":\"my_hue_app#iphone peter\"}";

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();
		// response Code
		int responseCode = con.getResponseCode();
		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[authorize2] responseData: {} - {}", responseCode, responseData);
		
	}
}
