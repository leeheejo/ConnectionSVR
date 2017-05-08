package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtikTestUtils {
	private static final Logger logger = LoggerFactory.getLogger(ArtikTestUtils.class);

	public static void createSubscription(HttpSession session, String uId, String ddId) throws Exception {

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
	
	public static void validateSubscription(HttpSession session, String subscriptionId, String aId, String nonce) throws Exception {
		logger.info("[validateSubscription]");
		// HttpPost 통신
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/"+subscriptionId+"/validate");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);

		String accessToken = session.getAttribute("ACCESS_TOKEN").toString();
		// Header
//		String authorizationHeader = "bearer " + accessToken;
//		con.setRequestProperty("Authorization", authorizationHeader);
		con.setRequestProperty("Content-Type", "application/json");

		// Param
		String param = "{\"aid\":\"" + aId +"\",\"nonce\":\""+nonce+"\"}";

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
		URL url = new URL("https://api.artik.cloud/v1.1/subscriptions/"+subscriptionId);
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
	
	public static void getNotification(HttpSession session, String notificationId) throws Exception {

		logger.info("[getNotification]");
		// HttpPost 통신
		URL url = new URL(
				"https://api.artik.cloud/v1.1/notifications/"+notificationId+"/messages");
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
		logger.info("[getNotification] responseCode : {}", responseCode + con.getResponseMessage());

		// Response Data
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseData = br.readLine();
		logger.info("[getNotification] responseData : {}", responseData);
		br.close();
	}
	
	
 
}
