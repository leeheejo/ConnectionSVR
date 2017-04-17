package com.icontrols.test.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icontrols.test.HomeController;
import com.icontrols.test.domain.Device;

public class IparkUtils {

	private static final Logger logger = LoggerFactory.getLogger(IparkUtils.class);
	public static int stateChangeFlag;

	public static String Action(String type, String action, String dId, String uId) {

		String jsonMsg = "";
		if (type.equals("control")) {
			jsonMsg = "{\"type\": \"control\",\"id\": \"" + uId + "\",\"dId\": \"" + dId + "\",\"action\": \"" + action
					+ "\"}";
		} else if (type.equals("state")) {
			jsonMsg = "{\"type\": \"state\",\"dId\": \"" + dId + "\",\"id\": \"" + uId + "\"}";
		} else if (type.equals("join")) {
			jsonMsg = "{\"type\": \"join\",\"id\": \"" + uId + "\"}";
		}

		logger.info("{}", jsonMsg);
		return clientDo(jsonMsg);
	}

	public static String clientDo(String jsonMsg) {

		Socket socket = null; // Server�� �ٴ� ����

		String ip = "192.168.101.204"; // ������ ip
		int port = 10001; // ������ port

		BufferedReader br = null;
		InputStream is = null;
		OutputStream os = null;
		PrintWriter pw = null;
		String sendMsg = jsonMsg;
		String response = null; // �޴� �޽�

		try {
			socket = new Socket(ip, port); // ������ ip, port ����

			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(new OutputStreamWriter(os)); // �Է��� ���� ����
			br = new BufferedReader(new InputStreamReader(is));

			pw.println(sendMsg); // sendMsg���
			pw.flush(); // ���۸޸� ���

			response = br.readLine(); // �����޽����޾ƿ�857+96

			System.out.println("Server�� ���� ���ƿ� ���ڿ� : " + response);

			pw.close();
			br.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public static Device getIparkInfo(String uId) {

		String dId = "";
		String name = "";
		String dtId = "";
		String response = Action("join", "", "", uId);

		JSONObject obj = new JSONObject(response);
		dId = obj.getString("dId");
		dtId = obj.getString("dtId");
		name = obj.getString("name");
		Device device = new Device(uId, dId, name, dtId);

		return device;
	}

	public static void sendAction(String action, String uId, String dId) {
		logger.info("[sendAction-Ipark]");
		Action("control", action, dId, uId);
	}

	public static int getState(Device d) {
		stateChangeFlag = 0;
		int result = -1;
		String responseData = Action("state", "", d.getdId(), d.getuId());
		JSONObject obj = new JSONObject(responseData);
		String state = obj.getString("state");

		if (state.equals("on")) {
			result = 1;
		} else if (state.equals("off")) {
			result = 0;
		}

		if (d.getState() != result) {
			stateChangeFlag = 1;
			logger.info("[sendAction-Ipark] stateChange");

		}

		return result;
	}
}
