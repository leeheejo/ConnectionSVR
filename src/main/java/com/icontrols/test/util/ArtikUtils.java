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
import com.icontrols.test.domain.DeviceList;
import com.icontrols.test.domain.SendTestLog;
import com.icontrols.test.service.DeviceService;

public class ArtikUtils {

	/* heejo's cloud info private static */

	// private static String clientId = "cfecebedc26b4690bf9f883d425200a5";
	// private static String clientSecret = "88fb76ff979543979b3f7b4728b317f2";

	/* inbo's cloud info */
	private static String clientId = "cbd3e38e12344b22a8c76cd3789b0e0e";
	private static String clientSecret = "fafe61a1191940989a86d9e69e1e3d66";

	// private static String callbackUrl =
	// "https://localhost:8443/test/callback";
	private static String callbackUrl = "http://192.168.101.24:8080/connectionSVR/callback";

	private static final Logger logger = LoggerFactory.getLogger(ArtikUtils.class);
	public static int stateChangeFlag;

	public static AccessToken getArtikAccessToken(String code) throws Exception {

		logger.info("[getArtikAccessToken]");
		String userpass = clientId + ":" + clientSecret;
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
		String param = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + callbackUrl;
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

	public static List<DeviceList> getArtikDeviceList(HttpSession session) throws Exception {

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
		List<DeviceList> artikDeviceList = parsingDeviceList(responseData,
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

	public static SendTestLog Action(HttpSession session, String dId, String action, String rgb) throws Exception {
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
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/");
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

	public static void createSubscription(HttpSession session) throws Exception {

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
		String param = "{\"uid\": \"" + session.getAttribute("ARTIK_USER_ID").toString() + "\",\"callbackUrl\": \""
				+ callbackUrl + "\",  \"messageType\": \"message\"}";

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
	}

	public static AccessToken refreshAccessToken(String refreshToken) throws Exception {

		logger.info("[getArtikAccessToken]");
		String userpass = clientId + ":" + clientSecret;
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

	// Parsing DeviceList(Json)
	public static List<DeviceList> parsingDeviceList(String jsonMsg, String uId) {

		List<DeviceList> deviceList = new ArrayList<DeviceList>();

		JSONObject obj = new JSONObject(jsonMsg);

		JSONObject data = obj.getJSONObject("data");
		JSONArray devices = data.getJSONArray("devices");

		// device data~

		for (int i = 0; i < devices.length(); i++) {
			JSONObject device = devices.getJSONObject(i);
			DeviceList dl = new DeviceList();

			String id = "";
			String dtid = "";
			String name = "";
			Integer manifestVersion = 0;
			String manifestVersionPolicy = "";
			Boolean needProviderAuth = false;
			String cloudAuthorization = "";
			String createdOn = "";
			Boolean connected = false;
			Boolean sharedWithOthers = false;
			String sharedWithMe = "";

			dl.setuId(uId);

			if (device.keySet().contains("id")) {
				id = device.getString("id");
			}
			dl.setId(id);

			if (device.keySet().contains("dtid")) {
				dtid = device.getString("dtid");
			}
			dl.setDtid(dtid);

			if (device.keySet().contains("name")) {
				name = device.getString("name");
			}
			dl.setName(name);

			if (device.keySet().contains("manifestVersion")) {
				manifestVersion = device.getInt("manifestVersion");
			}
			dl.setManifestVersion(manifestVersion);

			if (device.keySet().contains("manifestVersionPolicy")) {
				manifestVersionPolicy = device.getString("manifestVersionPolicy");
			}
			dl.setManifestVersionPolicy(manifestVersionPolicy);

			if (device.keySet().contains("needProviderAuth")) {
				needProviderAuth = device.getBoolean("needProviderAuth");
			}
			dl.setNeedProviderAuth(needProviderAuth);

			if (device.keySet().contains("id")) {
				cloudAuthorization = device.getString("cloudAuthorization");
			}
			dl.setCloudAuthorization(cloudAuthorization);

			if (device.keySet().contains("createdOn")) {
				createdOn = device.getInt("createdOn") + "";
			}
			dl.setCreatedOn(createdOn);

			if (device.keySet().contains("connected")) {
				connected = device.getBoolean("connected");
			}
			dl.setConnected(connected);

			if (device.keySet().contains("sharedWithOthers")) {
				sharedWithOthers = device.getBoolean("sharedWithOthers");
			}
			dl.setSharedWithOthers(sharedWithOthers);

			if (device.keySet().contains("sharedWithMe")) {
				sharedWithMe = device.getString("sharedWithMe");
			}
			dl.setSharedWithMe(sharedWithMe);

			deviceList.add(dl);
		}

		return deviceList;
	}
}
