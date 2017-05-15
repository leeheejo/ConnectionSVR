package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.domain.ArtikUserProfile;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.DeviceService;

public class ArtikUtils {

	private static final Logger logger = LoggerFactory.getLogger(ArtikUtils.class);
	public static int stateChangeFlag;

	public static AccessToken getArtikAccessToken(String code) throws Exception {

		logger.info("[getArtikAccessToken]");
		String userpass = NetworkInfo.ARTIK_CLIENT_ID + ":" + NetworkInfo.ARTIK_CLIENT_SECRET;
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));

		// httpPost 통신
		URL url = new URL("https://accounts.artik.cloud/token");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Host", "accounts.artik.cloud");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// Parameter
		String param = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + NetworkInfo.ARTIK_CALLBACK_URL;
		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response
		int responseCode = con.getResponseCode();
		logger.info("[getArtikAccessToken] responseCode : {}", responseCode + con.getResponseMessage());

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikAccessToken] responseData : {}", responseData);
		br.close();

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		// AccessToken.class 와 response json 의 parameter명이 일치해야 매핑가능
		AccessToken accessToken = mapper.readValue(responseData, AccessToken.class);

		return accessToken;
	}

	public static ArtikUserProfile getArtikUserInfo(HttpSession session) throws Exception {

		logger.info("[getArtikUserInfo]");
		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

		// HttpGET 통신
		URL url = new URL("https://api.artik.cloud/v1.1/users/self");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getArtikUserInfo] responseCode : {}", responseCode + con.getResponseMessage());

		// Respose Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikUserInfo] responseData : {}", responseData);
		br.close();

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = mapper.readValue(responseData, HashMap.class);
		logger.info("data : {}", data.get("data"));
		@SuppressWarnings("unchecked")
		HashMap<String, Object> userData = (HashMap<String, Object>) data.get("data");

		ArtikUserProfile artikUserProfile = new ArtikUserProfile(session.getAttribute("userLoginInfo").toString(),
				userData.get("id").toString(), userData.get("name").toString(), userData.get("email").toString(),
				userData.get("fullName").toString(), userData.get("saIdentity").toString(),
				userData.get("accountType").toString(), userData.get("createdOn").toString(),
				userData.get("modifiedOn").toString());

		return artikUserProfile;
	}

	public static List<Device> getArtikDeviceList(HttpSession session) throws Exception {

		logger.info("[getArtikDeviceList]");

		String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
		String userId = (String) session.getAttribute("ARTIK_USER_ID");

		logger.info("[getArtikDeviceList] ACCESS_TOKEN : {}, ARTIK_USER_ID : {}", accessToken, userId);
		// httpGET 통신
		URL url = new URL(
				"https://api.artik.cloud/v1.1/users/" + userId + "/devices?count=100&includeProperties=false");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// response Code
		int responseCode = con.getResponseCode();
		logger.info("[getArtikDeviceList] responseCode : {}", responseCode + con.getResponseMessage());

		// response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikDeviceList] responseData : {}", responseData);
		br.close();
		// parsing response Data
		List<Device> artikDeviceList = parsingDeviceList(responseData,
				session.getAttribute("userLoginInfo").toString());

		return artikDeviceList;
	}
	/*
	 * Send Action Method
	 * 
	 * @RequestParam action (String) setOn, setOff, setColorRGB, setEffect
	 * 
	 * @RequestParam dId (String) device ID
	 * 
	 * @RequestParam rgb (String) rgbColor for setColorRGB
	 * 
	 * @return void
	 */

	public static SendTestLog Action(HttpSession session, String dId, String action, String rgb, int windLevel)
			throws Exception {
		logger.info("[ACTION]");

		URL url = new URL("https://api.artik.cloud/v1.1/messages");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String authorizationHeader = "bearer " + session.getAttribute("ACCESS_TOKEN").toString();
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		String param = "";

		if (action.equals("setOn")) {
			logger.info("[Action] : setOn");
			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setOn\",\"parameters\": {}}]}}";

		} else if (action.equals("setOff")) {
			logger.info("[Action] setOff");
			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setOff\",\"parameters\": {}}]}}";

		} else if (action.equals("setColorRGB")) {
			logger.info("[Action] setColorRGB");
			int R = Integer.parseInt(rgb.split(";")[0]);
			int G = Integer.parseInt(rgb.split(";")[1]);
			int B = Integer.parseInt(rgb.split(";")[2]);

			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setColorRGB\",\"parameters\": {\"colorRGB\":{\"blue\":"
					+ B + ",\"green\":" + G + ",\"red\":" + R + "}}}]}}";

		} else if (action.equals("setEffect")) {
			logger.info("[Action] setEffect");
			logger.info("[Action] dId : {}", dId);
			param = "{ \"data\": {\"actions\": [{\"name\": \"setEffect\",\"parameters\": { \"effect\": \"colorloop\"}}]},\"ddid\": \""
					+ dId + "\",\"ts\": " + System.currentTimeMillis() + ",\"type\": \"action\"}";

		} else if (action.equals("setWind")) {
			logger.info("[Action] setWind");
			param = "{\"ddid\": \"" + dId + "\",\"ts\":" + System.currentTimeMillis()
					+ ",\"type\": \"action\",\"data\": {\"actions\": [{\"name\": \"setWind\",\"parameters\": {\"speedLevel\":"
					+ windLevel + "}}]}}";
			logger.info("[Action] {}", param);
		}
		logger.info("[Action] {}", param);
		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[Action] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[Action] responseData : {}", responseData);
		br.close();

		// Insert sendTestLog
		SendTestLog sendTestLog = new SendTestLog(session.getAttribute("userLoginInfo").toString(), 1, dId, action,
				responseCode + "", new Date(System.currentTimeMillis()));
		logger.info("[Action] insert SEND_TEST_LOG");

		return sendTestLog;

	}

	public static int getManifestVersion(HttpSession session, String dtId) throws Exception {

		logger.info("[getManifestVersion]");
		int result = 0;

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getManifestVersion] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getManifestVersion] responseData : {}", responseData);
		br.close();

		JSONObject obj = new JSONObject(responseData);
		JSONObject data = obj.getJSONObject("data");
		JSONArray devices = data.getJSONArray("versions");

		result = Integer.parseInt(devices.get(devices.length() - 1).toString());

		logger.info("[getManifestVersion] ManifestVersion : {}", result);

		return result;
	}

	// get Device State
	public static List<Device> getDeviceState(HttpSession session, List<Device> deviceList) throws Exception {

		logger.info("[getDeviceState]");

		stateChangeFlag = 0;
		List<Device> result = null;
		String AccessToken = session.getAttribute("ACCESS_TOKEN").toString();
		String authorizationHeader = "bearer " + AccessToken;
		String dIds = "";
		for (Device d : deviceList) {
			dIds += d.getdId();
			if (deviceList.indexOf(d) != deviceList.size() - 1) {
				dIds += ",";
			}
		}

		URL url = new URL("https://api.artik.cloud/v1.1/messages/last?count=1&sdids=" + dIds);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[messages] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[messages] responseData : {}", responseData);
		br.close();

		result = stateParser(session.getAttribute("userLoginInfo").toString(), responseData, deviceList);

		return result;
	}

	public static Integer getDeviceState(HttpSession session, String dId) throws Exception {

		logger.info("[getDeviceState]");

		int result = 0;
		String AccessToken = session.getAttribute("ACCESS_TOKEN").toString();
		String authorizationHeader = "bearer " + AccessToken;

		URL url = new URL("https://api.artik.cloud/v1.1/messages/last?count=1&sdids=" + dId);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[messages] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[messages] responseData : {}", responseData);
		br.close();

		result = stateParser(session.getAttribute("userLoginInfo").toString(), responseData);

		return result;
	}

	public static String getDeviceStateString(HttpSession session, String dId) throws Exception {

		logger.info("[getDeviceState]");

		int result = 0;
		String AccessToken = session.getAttribute("ACCESS_TOKEN").toString();
		String authorizationHeader = "bearer " + AccessToken;

		URL url = new URL("https://api.artik.cloud/v1.1/messages/last?count=1&sdids=" + dId);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[messages] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[messages] responseData : {}", responseData);
		br.close();

		return responseData;
	}

	public static void addNewArtikDevice(HttpSession session, String name, String dtId) throws Exception {

		logger.info("[addNewArtikDevice]");

		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/devices");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		int manifestVersion = ArtikUtils.getManifestVersion(session, dtId);
		// Param
		String param = "{\"uid\": \"" + session.getAttribute("ARTIK_USER_ID").toString() + "\",\"dtid\": \"" + dtId
				+ "\",  \"name\": \"" + name + "\",\"manifestVersion\":" + manifestVersion
				+ ",\"manifestVersionPolicy\": \"LATEST\"}";

		logger.info("[addNewDevice] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[addNewDevice] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[addNewDevice] responseData : {}", responseData);
		br.close();
	}

	public static AccessToken refreshAccessToken(String refreshToken) throws Exception {

		logger.info("[getArtikAccessToken]");
		String userpass = NetworkInfo.ARTIK_CLIENT_ID + ":" + NetworkInfo.ARTIK_CLIENT_SECRET;
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));

		// httpPost 통신
		URL url = new URL("https://accounts.artik.cloud/token");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Host", "accounts.artik.cloud");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// Parameter
		String param = "grant_type=refresh_token&refresh_token=" + refreshToken;
		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response
		int responseCode = con.getResponseCode();
		logger.info("[getArtikAccessToken] responseCode : {}", responseCode + con.getResponseMessage());

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getArtikAccessToken] responseData : {}", responseData);
		br.close();

		// Json Mapping
		ObjectMapper mapper = new ObjectMapper();
		// AccessToken.class 와 response json 의 parameter명이 일치해야 매핑가능
		AccessToken accessToken = mapper.readValue(responseData, AccessToken.class);

		return accessToken;
	}

	public static String createSubscription(HttpSession session, String uId, String ddId) throws Exception {

		logger.info("[createSubscription]");
		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Param
		String param = "{\"messageType\":\"action\", \"uid\":\"" + uId + "\",\"ddid\":\"" + ddId
				+ "\",\"description\":\"" + ddId
				+ "\", \"subscriptionType\": \"httpCallback\",\"callbackUrl\":\"https://icontrols-dev.com/connectionSVR/callback\"}";

		logger.info("[createSubscription] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[createSubscription] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[createSubscription] responseData : {}", responseData);
		br.close();
		String subscriptionId = new JSONObject(responseData).getJSONObject("data").getString("id");

		return subscriptionId;
		// {"data":{"id":"46d418adf6454ecb8ca18632e445e843","aid":"cbd3e38e12344b22a8c76cd3789b0e0e","messageType":"action","uid":"58e8794672f848f5bf65dfd6267ff9b9","ddid":"f9da7204a9644b99a92ae2da56c48df8","description":"f9da7204a9644b99a92ae2da56c48df8","subscriptionType":"httpCallback","callbackUrl":"https://icontrols-dev.com/connectionSVR/callback","status":"PENDING_CALLBACK_VALIDATION","createdOn":1494131150908,"modifiedOn":1494131150908}}

	}

	public static void getSubscription(HttpSession session) throws Exception {

		logger.info("[getSubscription]");
		// HttpPost 통신
		URL url = new URL(
				"https://api.artik.cloud/v1.1/subscriptions?count=100&uid=58e8794672f848f5bf65dfd6267ff9b9&offset=0");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getSubscription] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getSubscription] responseData : {}", responseData);
		br.close();
	}

	public static void validateSubscription(String subscriptionId, String aId, String nonce) throws Exception {
		logger.info("[validateSubscription]");
		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/" + subscriptionId + "/validate");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");

		// Param
		String param = "{\"aid\":\"" + aId + "\",\"nonce\":\"" + nonce + "\"}";

		logger.info("[validateSubscription] PARAM : {}", param);

		OutputStream os = con.getOutputStream();
		os.write(param.getBytes());
		os.flush();
		os.close();

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[validateSubscription] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[validateSubscription] responseData : {}", responseData);
		br.close();
	}

	public static void deleteSubscription(HttpSession session, String subscriptionId) throws Exception {
		logger.info("[deleteSubscription]");
		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/" + subscriptionId);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("DELETE");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[deleteSubscription] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[deleteSubscription] responseData : {}", responseData);
		br.close();
	}

	public static String getNotification(String accessToken, String notificationId) throws Exception {

		logger.info("[getNotification] {} ", notificationId);
		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/notifications/" + notificationId + "/messages");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		String authorizationHeader = "bearer " + accessToken;
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[getNotification] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getNotification] responseData : {}", responseData);
		br.close();

		JSONObject obj = new JSONObject(responseData);
		logger.info("{}", obj);
		JSONObject data = obj.getJSONArray("data").getJSONObject(0);
		logger.info("{}", data);
		JSONObject actions = data.getJSONObject("data").getJSONArray("actions").getJSONObject(0);
		logger.info("{}", actions);
		String action = actions.getString("name");

		return action;
	}

	public static List<Device> stateParser(String uId, String responseData, List<Device> deviceList) {

		List<Device> result = deviceList;
		JSONObject obj = new JSONObject(responseData);
		JSONArray object = obj.getJSONArray("data");

		for (int i = 0; i < object.length(); i++) {

			String power = "";
			JSONObject data = object.getJSONObject(i);
			String dtId = data.getString("sdtid");
			String dId = data.getString("sdid");
			JSONObject data1 = data.getJSONObject("data");
			int state = 0;

			if (dtId.equals(ArtikDeviceType.SAMSUNG_SMARTHOME_AIRPURIFIER)) {
				JSONObject operation = data1.getJSONObject("Operation");
				power = operation.get("power").toString();
			} else {
				JSONArray devices = obj.getJSONArray("data");
				if (devices.length() != 0) {
					if (dtId.equals(ArtikDeviceType.AMULATOR)) {
						power = data1.getBoolean("state") + "";
					} else if (dtId.equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)) {
						power = data1.getString("state");
					}
				}
			}

			if (power.equals("off") || power.equals("Off") || power.equals("false"))
				state = 0;
			else
				state = 1;
			for (Device d : result) {
				if (d.getdId().equals(dId)) {
					if (d.getState() != state) {
						stateChangeFlag = 1;
						d.setState(state);
					}
				}
			}
		}

		return result;
	}

	public static int stateParser(String uId, String responseData) {

		JSONObject obj = new JSONObject(responseData);
		JSONArray object = obj.getJSONArray("data");
		int state = 0;

		String power = "";
		JSONObject data = object.getJSONObject(0);
		String dtId = data.getString("sdtid");
		String dId = data.getString("sdid");
		JSONObject data1 = data.getJSONObject("data");
		state = 0;

		if (dtId.equals(ArtikDeviceType.SAMSUNG_SMARTHOME_AIRPURIFIER)) {
			JSONObject operation = data1.getJSONObject("Operation");
			power = operation.get("power").toString();
		} else {
			JSONArray devices = obj.getJSONArray("data");
			if (devices.length() != 0) {
				if (dtId.equals(ArtikDeviceType.AMULATOR)) {
					power = data1.getBoolean("state") + "";
				} else if (dtId.equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)) {
					power = data1.getString("state");
				}
			}
		}

		if (power.equals("off") || power.equals("Off") || power.equals("false"))
			state = 0;
		else
			state = 1;

		return state;
	}

	// Parsing DeviceList(Json)
	public static List<Device> parsingDeviceList(String jsonMsg, String uId) {

		List<Device> deviceList = new ArrayList<Device>();

		JSONObject obj = new JSONObject(jsonMsg);

		JSONObject data = obj.getJSONObject("data");
		JSONArray devices = data.getJSONArray("devices");

		// device data~

		for (int i = 0; i < devices.length(); i++) {
			JSONObject device = devices.getJSONObject(i);

			String id = "";
			String dtid = "";
			String name = "";

			if (device.keySet().contains("id")) {
				id = device.getString("id");
			}

			if (device.keySet().contains("dtid")) {
				dtid = device.getString("dtid");
			}

			if (device.keySet().contains("name")) {
				name = device.getString("name");
			}

			Device dl = new Device(uId, id, name, dtid, 1);
			deviceList.add(dl);
		}

		return deviceList;
	}
}
