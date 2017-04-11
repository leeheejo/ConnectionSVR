package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.icontrols.test.domain.DeviceList;
import com.icontrols.test.service.DeviceService;

public class ArtikUtils {

	private static final Logger logger = LoggerFactory.getLogger(ArtikUtils.class);


	// get Device State
	public static Integer getDeviceState(HttpSession session, String dId, String dtId) throws Exception {

		logger.info("[getDeviceState]");
		

		Integer result = 0;

		String AccessToken = session.getAttribute("ACCESS_TOKEN").toString();

		String authorizationHeader = "bearer " + AccessToken;

		URL url = new URL("https://api.artik.cloud/v1.1/messages/last?count=1&fieldPresence=state&sdids=" + dId);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);

		// Header
		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Response Code
		int responseCode = con.getResponseCode();
		logger.info("[messages] responseCode2 : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[messages] responseData 2: {}", responseData);
		br.close();

		// JSON Parsing
		JSONObject obj = new JSONObject(responseData);
		logger.info("[messages] obj : {}", obj);
		JSONArray devices = obj.getJSONArray("data");
		logger.info("[messages] devices : {}", devices);
		if (devices.length() != 0) {
			JSONObject device = devices.getJSONObject(0);
			logger.info("[messages] device : {}", device);
			JSONObject state = device.getJSONObject("data");
			if (dtId.equals(ArtikDeviceType.AMULATOR)) {
				if (state.getBoolean("state") == false) {
					result = 0;
				} else {
					result = 1;
				}
			} else if (dtId.equals(ArtikDeviceType.PHILIPS_HUE_COLOR_LAMP)) {
				if (state.getString("state").equals("off")) {
					result = 0;
				} else {
					result = 1;
				}

			}
		}
		logger.info("[getDeviceState] result : {}", result);

		return result;
	}

	// Parsing DeviceList(Json)
	public static List<DeviceList> parsinDeviceList(String jsonMsg, String uId) {

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
