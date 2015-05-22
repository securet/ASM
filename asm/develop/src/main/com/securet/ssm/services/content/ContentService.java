package com.securet.ssm.services.content;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securet.ssm.services.DefaultService;
import com.securet.ssm.services.SecureTService;

@Controller
@Repository
@Service
public class ContentService extends SecureTService{

	@RequestMapping("/content/help")
	public String userProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser,Model model){
		return DefaultService.CONTENT+"help";
	}

}
