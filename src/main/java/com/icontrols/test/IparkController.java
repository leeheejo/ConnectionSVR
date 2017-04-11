package com.icontrols.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IparkController {

	private static final Logger logger = LoggerFactory.getLogger(IparkController.class);

	@RequestMapping("/tcpIpTest")
	public String tcpIpTest(Model model, HttpSession session) {
		logger.info("[tcpIpTest]");
		String id = session.getAttribute("userLoginInfo").toString();
		String did = "mainLight";
		String action = "setOn";

		String jsonMsg = "{\"type\": \"control\",\"id\": \"" + id + "\",\"did\": \"" + did + "\",\"action\": \"setOn\"}";
		clientDo(jsonMsg);

		return "redirect:/success";
	}

	public static void clientDo(String jsonMsg) {
		Socket socket = null; // Server�� �ٴ� ����
		String ip = "192.168.101.204"; // ������ ip
		int port = 10001; // ������ port

		BufferedReader br = null;
		InputStream is = null;
		OutputStream os = null;
		PrintWriter pw = null;

		String sendMsg = jsonMsg; // ������ �޽���
		String response = null; // �޴� �޽�

		try {
			socket = new Socket(ip, port); // ������ ip, port ����
			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(new OutputStreamWriter(os)); // �Է��� ���� ����
			br = new BufferedReader(new InputStreamReader(is));

			pw.println(sendMsg); // sendMsg���
			pw.flush(); // ���۸޸� ���

			response = br.readLine(); // �����޽����޾ƿ�
			System.out.println("Server�� ���� ���ƿ� ���ڿ� : " + response);

			pw.close();
			br.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
