package com.icontrols.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.icontrols.test.domain.AccessToken;
import com.icontrols.test.service.AccessTokenService;
import com.icontrols.test.util.ArtikUtils;

@Component
@EnableScheduling
public class ScheduleTask {
	
	private final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

	@Autowired
	AccessTokenService accessTokenService;

	@Scheduled(cron = "0 0 15 * * ?") //���� ����3�ÿ� AccessToekn Validate �˻�
	public void scheduleRun() throws Exception {
		logger.info("Token Check Scheduler START");
		
		List<AccessToken> accessTokenList = accessTokenService.getAllAccessToken();
		for (AccessToken at : accessTokenList) {
			logger.info(" {} 's old token {}", at.getuId(), at.getAccess_token());
			if(Long.parseLong(at.getIssuedTime())+Long.parseLong(at.getExpires_in()) > System.currentTimeMillis()+86500 ) {
				AccessToken accessToken = ArtikUtils.refreshAccessToken(at.getRefresh_token());
				accessToken.setuId(at.getuId());
				accessTokenService.updateAccessToken(accessToken);
				logger.info(" {} 's new token {}",at.getuId(), accessToken.getAccess_token());
			}
		}
	}

}
