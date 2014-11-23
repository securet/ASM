package com.securet.asm.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultService {

	@RequestMapping("/login")
	public String login(Model model){
		
		return "login";
	}
	
	@RequestMapping("/admin/")
	public String admin(Model model){
		//TODO - Prepare admin view model later
		return "admin/home";
	}
	
	@RequestMapping("/403")
	public String forbidden(Model model){
		return "error/error.403";
	}
}
