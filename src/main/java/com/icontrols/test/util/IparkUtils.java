package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icontrols.test.HomeController;
import com.icontrols.test.domain.Device;

public class IparkUtils {

	private static final Logger logger = LoggerFactory.getLogger(IparkUtils.class);
	public static String IparkSvrURL = "http://192.168.101.204:8080/test";
	public static int stateChangeFlag;

	public static Device getIparkInfo(String uId) throws Exception {

		String dId = "";
		String name = "";
		String dtId = "";

		// httpGET 통신
		URL url = new URL(IparkSvrURL + "/join?id=" + uId);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getIparkInfo] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getIparkInfo] responseData : {}", responseData);

		JSONObject obj = new JSONObject(responseData);
		dId = obj.getString("dId");
		dtId = obj.getString("dtId");
		name = obj.getString("name");
		Device device = new Device(uId, dId, name, dtId);

		return device;
	}

	public static void sendAction(String action, String uId, String dId) throws Exception {
		// httpGET 통신
		URL url = new URL(IparkSvrURL + "/control?id=" + uId + "&dId=" + dId + "&action=" + action);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[sendAction] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[sendAction] responseData : {}", responseData);

	}

	public static int getState(Device d) throws Exception{
		stateChangeFlag = 0;
		int result = -1;
		// httpGET 통신
		URL url = new URL(IparkSvrURL + "/state?id=" + d.getuId() + "&dId=" + d.getdId());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getState] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		JSONObject obj = new JSONObject(responseData);
		String state = obj.getString("state");

		if (state.equals("on")) {
			result = 1;
		} else if (state.equals("off")) {
			result = 0;
		}

		if (d.getState() != result) {
			stateChangeFlag = 1;
			logger.info("[getState-Ipark] stateChange");

		}

		return result;
	}
}
