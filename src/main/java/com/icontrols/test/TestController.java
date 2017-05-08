package com.icontrols.test;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.icontrols.test.service.ArtikUserProfileService;
import com.icontrols.test.util.ArtikTestUtils;
import com.icontrols.test.util.ArtikUtils;

@Controller
public class TestController {

	@Autowired
	ArtikUserProfileService artikUserProfileService;

	@RequestMapping("test")
	public ModelAndView test(HttpSession session, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test");
		return mav;
	}

	@RequestMapping("create")
	public ModelAndView create(HttpSession session, Model model, @RequestParam(value = "ddid") String ddid)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		String uId = artikUserProfileService.getUserIdById(session.getAttribute("userLoginInfo").toString());
		ArtikTestUtils.createSubscription(session, uId, ddid);
		mav.setViewName("test");
		return mav;
	}

	@RequestMapping("delete")
	public ModelAndView delete(HttpSession session, Model model,
			@RequestParam(value = "subscriptionID") String subscriptionId) throws Exception {
		ModelAndView mav = new ModelAndView();
		ArtikTestUtils.deleteSubscription(session, subscriptionId);
		mav.setViewName("test");
		return mav;
	}

	@RequestMapping("getlist")
	public ModelAndView getlist(HttpSession session, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		ArtikTestUtils.getSubscription(session);

		mav.setViewName("test");
		return mav;
	}

	@RequestMapping("validate")
	public ModelAndView validate(HttpSession session, Model model,
			@RequestParam(value = "subscriptionID") String subscriptionId, @RequestParam(value = "aId") String aId,
			@RequestParam(value = "nonce") String nonce) throws Exception {
		ModelAndView mav = new ModelAndView();
		ArtikTestUtils.validateSubscription(session, subscriptionId, aId, nonce);
		mav.setViewName("test");
		return mav;
	}

	@RequestMapping("noti")
	public ModelAndView noti(HttpSession session, Model model, @RequestParam (value = "notificationID") String notificationID) throws Exception {
		ModelAndView mav = new ModelAndView();
		ArtikTestUtils.getNotification(session, notificationID);
		 // {"data":[{"type":"action","cts":1494137878872,"ts":1494137878872,"mid":"764dd9e69d894eef8f5d810651d833bf","sdid":"f9da7204a9644b99a92ae2da56c48df8","ddid":"f9da7204a9644b99a92ae2da56c48df8","data":{"actions":[{"name":"setOn"}]},"ddtid":"dt6f79b9b4aa3b4a80b7b76c2190016c61","uid":"58e8794672f848f5bf65dfd6267ff9b9","boid":"0","mv":11}],"total":1,"offset":0,"count":1,"order":"asc"}
		mav.setViewName("test");
		return mav;
	}

}
