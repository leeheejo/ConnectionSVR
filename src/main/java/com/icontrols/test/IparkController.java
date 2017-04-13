package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IparkController {

	private static final Logger logger = LoggerFactory.getLogger(IparkController.class);

	@RequestMapping("/tcpIpTest")
	public String tcpIpTest(Model model, HttpSession session) {
		logger.info("[tcpIpTest]");

		String id = session.getAttribute("userLoginInfo").toString(); // user's id
		String did = "mainLight";
		String action = "setOn"; // setOn, setOff
		String type = "state"; // state, action, join

		String jsonMsg = "{\"type\": \"" + type + "\",\"id\": \"" + id + "\",\"did\": \"" + did + "\",\"action\": \""
				+ action + "\"}";
		
		clientDo(jsonMsg);

		return "redirect:/success";
	}

	@RequestMapping("/getIparkInfo")
	public String getIparkInfo(Model model, HttpSession session) {
		
		String id = session.getAttribute("userLoginInfo").toString(); // user's id
		String jsonMsg = "{\"type\": \" join \",\"id\": \"" + id + "\"}";
		

		return "redirect:/success";
	}

	@RequestMapping("/getIparkState")
	public String getIparkState(Model model, HttpSession session) {
		
		String id = session.getAttribute("userLoginInfo").toString(); // user's id
		String type = "state"; // state, action, join
		String did = "mainLight";

		String jsonMsg = "{\"type\": \" state \",\"did\": \"" + did + "\",\"id\": \"" + id + "\"}";

		return "redirect:/success";
	}

	@RequestMapping("/sendIparkAction")
	public String sendIparkAction(Model model, HttpSession session) {
		
		String id = session.getAttribute("userLoginInfo").toString(); // user's id
		String did = "mainLight";
		String action = "setOn"; // setOn, setOff
		String type = "state"; // state, action, join

		String jsonMsg = "{\"type\": \"" + type + "\",\"id\": \"" + id + "\",\"did\": \"" + did + "\",\"action\": \""
				+ action + "\"}";
		

		return "redirect:/success";
	}

	public static void clientDo(String jsonMsg) {

		Socket socket = null; // Server에 붙는 소켓

		String ip = "192.168.101.204"; // 접속할 ip
		int port = 10001; // 접속할 port

		BufferedReader br = null;
		InputStream is = null;
		OutputStream os = null;
		PrintWriter pw = null;
		// String sendMsg = "{\"type\": \"state\",\"id\": \"heejo\",\"did\":
		// \"mainLight\",\"action\": \"state\"}"; // 보내는 메시지
		String sendMsg = jsonMsg;
		String response = null; // 받는 메시

		try {
			socket = new Socket(ip, port); // 접속할 ip, port 지정

			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(new OutputStreamWriter(os)); // 입력한 문자 전송
			br = new BufferedReader(new InputStreamReader(is));

			pw.println(sendMsg); // sendMsg출력
			pw.flush(); // 버퍼메모리 사용

			response = br.readLine(); // 서버메시지받아옴

			System.out.println("Server로 부터 돌아온 문자열 : " + response);

			pw.close();
			br.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void jsonParserForIpark(int flag, String responseDate) {
		
		switch(flag) {
		case 0: // join
			break;
		case 1: // state
			break;
		case 2: // action
			break;
		}
	}
}
