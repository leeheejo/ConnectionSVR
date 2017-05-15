package com.icontrols.test.util;

public class NetworkInfo {
	/*
	 * AWS 배포시 주의 사항 src\main\webapp\WEB-INF\spring\root-context.xml에서 DB
	 * develop ver 주석 처리 DB AWS ver 주석 해제
	 * 
	 * ARTIK_CALLBACK_URL을 ARTIK_CALLBACK_URL_AWS로 변경 
	 * ARTIK Cloud에서 AppInfo의 Callback URL도 ARTIK_CALLBACK_URL_AWS와 동일하게 설정해야 
	 * AWS에서 ARTIK Cloud 로그인 시 Token 발급 가능함
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
