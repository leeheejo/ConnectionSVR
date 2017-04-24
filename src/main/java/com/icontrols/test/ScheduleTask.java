package com.icontrols.test;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.domain.IparkAccessToken;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.service.IparkAccessTokenService;
import com.icontrols.test.util.ArtikUtils;
import com.icontrols.test.util.IparkUtils;

@Component
@EnableScheduling
public class ScheduleTask {

	private final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

	@Autowired
	AccessTokenService accessTokenService;

	@Autowired
	IparkAccessTokenService iparkAccessTokenService;

	@Scheduled(cron = "0 0 15 * * ?") // 매일 오후3시에 AccessToken Validate 검사
	public void scheduleRun() throws Exception {
		logger.info("Token Check Scheduler START");

		List<AccessToken> accessTokenList = accessTokenService.getAllAccessToken();
		for (AccessToken at : accessTokenList) {
			logger.info(" {} 's old token {}", at.getuId(), at.getAccess_token());
			// System.currentTimeMillis()+86500
			if (Long.parseLong(at.getIssuedTime()) + Long.parseLong(at.getExpires_in()) > System.currentTimeMillis()+86500) {
				AccessToken accessToken = ArtikUtils.refreshAccessToken(at.getRefresh_token());
				accessToken.setuId(at.getuId());
				accessTokenService.updateAccessToken(accessToken);
				logger.info(" {} 's new token {}", at.getuId(), accessToken.getAccess_token());
			}
		}

		List<IparkAccessToken> iparkAccessTokenList = iparkAccessTokenService.getAllIparkAccessToken();
		for (IparkAccessToken iat : iparkAccessTokenList) {
			logger.info(" {} 's old token {}", iat.getuId(), iat.getAccessToken());
			if (Long.parseLong(iat.getIssuedTime()) + Long.parseLong(iat.getExpiresIn()) > 0) {
				IparkAccessToken iparkAccessToken = IparkUtils.getIparkAccessToken(iat.getuId());
				iparkAccessToken.setuId(iat.getuId());
				iparkAccessTokenService.updateIparkAccessToken(iparkAccessToken);
				logger.info(" {} 's new token {}", iat.getuId(), iparkAccessToken.getAccessToken());
			}
		}
	}

}
