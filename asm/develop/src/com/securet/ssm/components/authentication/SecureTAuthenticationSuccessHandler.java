package com.securet.ssm.components.authentication;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.admin.UserService;

@Repository
@Service
public class SecureTAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{


	
	public static enum  Authorities {ADMIN,CLIENT_USER,RESOLVER,CLIENT_CONTROLLER};
	private static final Logger _logger = LoggerFactory.getLogger(SecureTAuthenticationSuccessHandler.class);
    private RequestCache requestCache = new HttpSessionRequestCache();
    public static final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(Authorities.ADMIN.toString());
    public static final GrantedAuthority clientUserAuthority = new SimpleGrantedAuthority(Authorities.CLIENT_USER.toString());
    public static final GrantedAuthority resolverAuthority = new SimpleGrantedAuthority(Authorities.RESOLVER.toString());
    public static final GrantedAuthority clientControllerAuthority = new SimpleGrantedAuthority(Authorities.CLIENT_CONTROLLER.toString());

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

	
	public void setEntityManager(EntityManager entityManager){
		this.entityManager=entityManager;
	}

	
	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        UserService.saveLastLoginTimestamp(entityManager,authentication.getName());
        
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
