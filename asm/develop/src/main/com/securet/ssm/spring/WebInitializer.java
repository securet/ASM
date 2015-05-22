package com.securet.ssm.spring;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        ServletRegistration.Dynamic registration = container.addServlet("ssm", new DispatcherServlet());
        registration.setLoadOnStartup(1);
/*		MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("3MB");
        factory.setMaxRequestSize("4MB");
        registration.setMultipartConfig(factory.createMultipartConfig());
*/        //registration.addMapping("/admin/*");
        registration.addMapping("/");
    }

}