package com.securet.ssm.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultService {

	public static final String ADMIN = "admin/";

	@RequestMapping("/login")
	public String login(Model model){
		
		return "login";
	}
	
	@RequestMapping("/admin/")
	public String admin(Model model){
		//TODO - Prepare admin view model later
		return "admin/home";
	}
	
	@RequestMapping("error/403")
	public String forbidden(Model model){
		return "error/error.403";
	}
	@RequestMapping("error/404")
	public String notFound(Model model){
		return "error/error.404";
	}
}
