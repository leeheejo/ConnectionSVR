package com.icontrols.test.util;

public class NetworkInfo {
	/*
	 * AWS ������ ���� ���� src\main\webapp\WEB-INF\spring\root-context.xml���� DB
	 * develop ver �ּ� ó�� DB AWS ver �ּ� ����
	 * 
	 * ARTIK_CALLBACK_URL�� ARTIK_CALLBACK_URL_AWS�� ���� 
	 * ARTIK Cloud���� AppInfo�� Callback URL�� ARTIK_CALLBACK_URL_AWS�� �����ϰ� �����ؾ� 
	 * AWS���� ARTIK Cloud �α��� �� Token �߱� ������
	 * 
	 */
	public static String ARTIK_CALLBACK_URL = "https://icontrols-dev.com/connectionSVR/callback";

	/* ARTIK CALLBACK URL */
	// public String ARTIK_CALLBACK_URL_AWS =
	// "http://52.79.230.217:8080/connectionSVR/callback";
	// public String ARTIK_CALLBACK_URL_IPARK =
	// "http://61.33.215.251:8080/connectionSVR/callback";
	// public String ARTIK_CALLBACK_URL_LOCAL
	// ="http://192.168.101.24:8080/connectionSVR/callback";

	/* ARTIK APPLICATION INFO */
	/* heejo's cloud info private static */
	// private static String clientId = "cfecebedc26b4690bf9f883d425200a5";
	// private static String clientSecret = "88fb76ff979543979b3f7b4728b317f2";

	/* inbo's cloud info */
	public static String ARTIK_CLIENT_ID = "cbd3e38e12344b22a8c76cd3789b0e0e";
	public static String ARTIK_CLIENT_SECRET = "fafe61a1191940989a86d9e69e1e3d66";

	/* IPARK SVR URL */
	public static String IPARK_URL = "http://61.33.215.251:8080/test";
}
