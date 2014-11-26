package com.securet.ssm.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        ServletRegistration.Dynamic registration = container.addServlet("ssm", new DispatcherServlet());
        registration.setLoadOnStartup(1);
        //registration.addMapping("/admin/*");
        registration.addMapping("/");
    }

}