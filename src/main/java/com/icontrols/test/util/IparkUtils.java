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
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.icontrols.test.HomeController;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.IparkAccessToken;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.SendTestLogService;

public class IparkUtils {

	private static final Logger logger = LoggerFactory.getLogger(IparkUtils.class);

	public static int stateChangeFlag;

	public static IparkAccessToken getIparkAccessToken(String uId) throws Exception {

		logger.info("[getIparkInfo]");
		IparkAccessToken iparkAccessToken = new IparkAccessToken();

		String accessToken = "";
		String expiresIn = "";
		// httpGET 통신
		URL url = new URL(NetworkInfo.IPARK_URL + "/author");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("id", uId);
		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getIparkInfo] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		br.close();

		logger.info("[getIparkInfo] responseData : {}", responseData);

		JSONObject obj = new JSONObject(responseData);
		accessToken = obj.getString("accessToken");
		expiresIn = obj.getInt("expires_in") + "";

		iparkAccessToken.setuId(uId);
		iparkAccessToken.setAccessToken(accessToken);
		iparkAccessToken.setExpiresIn(expiresIn);

		return iparkAccessToken;
	}

	public static Device getIparkInfo(String accessToken, String uId) throws Exception {

		logger.info("[getIparkInfo]");

		String dId = "";
		String name = "";
		String dtId = "";
		int state = 0;

		// httpGET 통신
		URL url = new URL(NetworkInfo.IPARK_URL + "/join");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("accessToken", accessToken);
		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getIparkInfo] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		br.close();

		logger.info("[getIparkInfo] responseData : {}", responseData);

		JSONObject obj = new JSONObject(responseData);
		dId = obj.getString("dId");
		dtId = obj.getString("dtId");
		name = obj.getString("name");
		if (obj.getString("state").equals("on")) {
			state = 1;
		}
		
		Device device = new Device(uId, dId, name, state, dtId);

		return device;
	}

	public static SendTestLog sendAction(String action, String uId, String dId, String accessToken) throws Exception {

		logger.info("[sendAction]");
		// httpGET 통신
		URL url = new URL(NetworkInfo.IPARK_URL + "/control?dId=" + dId + "&action=" + action);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("accessToken", accessToken);
		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[sendAction] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		br.close();

		logger.info("[sendAction] responseData : {}", responseData);

		JSONObject obj = new JSONObject(responseData);
		String iparkState = obj.getString("state");

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(uId, 0, dId, action, responseCode + "",
				new Date(System.currentTimeMillis()));
		sendTestLog.setIparkState(iparkState);

		return sendTestLog;
	}

	public static int getState(Device d, String accessToken) throws Exception {

		logger.info("[getState]");
		logger.info("[getState]{}", d.toString());
		stateChangeFlag = 0;
		int result = 0;
		// httpGET 통신
		URL url = new URL(NetworkInfo.IPARK_URL + "/state?dId=" + d.getdId());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// HEADER
		con.setRequestProperty("accessToken", accessToken);
		// con.setRequestProperty("id", "hi");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getState] responseCode : {}", responseCode);

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		br.close();

		JSONObject obj = new JSONObject(responseData);
		String state = obj.getString("state");
		logger.info("[getState] responseData : {}", responseData);

		if (state.equals("on")) {
			result = 1;
		} else if (state.equals("off")) {
			result = 0;
		}

		if (d.getState() != result) {
			stateChangeFlag = 1;
			logger.info("[getState] stateChange");

		}

		return result;
	}
}
