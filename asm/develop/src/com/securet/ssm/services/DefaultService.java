package com.securet.ssm.services;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.securet.ssm.services.rest.SecureTJSONResponse;

@Controller
public class DefaultService {

	public static final String ADMIN = "admin/";
	public static final String TICKET = "ticket/";
	private static final Logger _logger =  LoggerFactory.getLogger(DefaultService.class);

	@RequestMapping("/login")
	public String login(Model model) {

		return "login";
	}

	@RequestMapping("/logout")
	public String logout(Model model,HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:login";
	}

	@RequestMapping(value= {"/admin","/admin/"})
	public String admin(Model model) {
		// TODO - Prepare admin view model later
		return "admin/home";
	}

	@RequestMapping(value= {"/tickets","/tickets/"})
	public String tickets(Model model) {
		// TODO - Prepare admin view model later
		return "ticket/listTickets";
	}

	@RequestMapping("error/403")
	public String forbidden(Model model) {
		return "error/error.403";
	}

	@RequestMapping("error/404")
	public String notFound(Model model) {
		return "error/error.404";
	}
	
}
