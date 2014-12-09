package com.securet.ssm.components.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;


public class SecureTAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	public static enum  Authorities {ADMIN,CLIENT_USER,RESOLVER,CLIENT_CONTROLLER};
	private static final Logger _logger = LoggerFactory.getLogger(SecureTAuthenticationSuccessHandler.class);
    private RequestCache requestCache = new HttpSessionRequestCache();
    public static final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(Authorities.ADMIN.toString());
    public static final GrantedAuthority clientUserAuthority = new SimpleGrantedAuthority(Authorities.CLIENT_USER.toString());
    public static final GrantedAuthority resolverAuthority = new SimpleGrantedAuthority(Authorities.RESOLVER.toString());
    public static final GrantedAuthority clientControllerAuthority = new SimpleGrantedAuthority(Authorities.CLIENT_CONTROLLER.toString());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest!=null){
			if(_logger.isDebugEnabled())_logger.debug("redirecting to target url "+savedRequest.getRedirectUrl());
			super.onAuthenticationSuccess(request, response, authentication);
        }else if(authentication.getAuthorities().contains(adminAuthority)){
        	getRedirectStrategy().sendRedirect(request, response, "/admin/");
        }else if((authentication.getAuthorities().contains(clientUserAuthority) || authentication.getAuthorities().contains(resolverAuthority))){
        	getRedirectStrategy().sendRedirect(request, response, "/tickets/");
        }else if(authentication.getAuthorities().contains(clientControllerAuthority)){
        	getRedirectStrategy().sendRedirect(request, response, "/reports/");
        }else{
        	getRedirectStrategy().sendRedirect(request, response, "/403");
        }
	}
	
	
	public static void main(String[] args) {
		List<String> auth = new ArrayList<String>();
		auth.add("ADMIN");
		_logger.debug("is ADMIN? "+ auth.contains(Authorities.ADMIN.toString()));
	}
}
