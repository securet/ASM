package com.securet.ssm.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FTLUtil {
	public static Configuration cfg = null;
	public static final Logger _logger = LoggerFactory.getLogger(FTLUtil.class);
	
	static{
		initConfig();
	}
	
	public static  void initConfig(){
        cfg = new Configuration(Configuration.VERSION_2_3_21);
        try{
        	URL url = Thread.currentThread().getContextClassLoader().getResource("../../WEB-INF/ftl/templates/");
        	cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
        }catch(IOException e){
        	e.printStackTrace();
        	_logger.error("could not initialize freemarker config: ",e);
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	}
	
	public static String processTemplate(String templateName,Map<String,?> input){
		try {
			Template template = cfg.getTemplate(templateName);
			StringWriter writer = new StringWriter();
			template.process(input, writer);
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			_logger.error("could not find template: ", e);
		} catch (TemplateException e) {
			e.printStackTrace();
			_logger.error("could not parse the template: ",e);
		}
		_logger.warn("no template processed for :"+templateName);
		return "";
	}
	
	public static String processString(String templateString,Map<String,?> input){
		try {
			StringReader reader = new StringReader(templateString);
			Template template = new Template("", reader, cfg);
			StringWriter writer = new StringWriter();
			template.process(input, writer);
			return writer.toString();
		} catch (IOException e) {
			_logger.error("could not find template: ", e);
		} catch (TemplateException e) {
			_logger.error("could not parse the template: ",e);
		}
		return "";
	}
	
	public static void main(String[] args) {
		System.out.println("url: "+Thread.currentThread().getContextClassLoader().getResource("com/securet"));
		Map<String,Object> mailContext = new HashMap<String,Object>();
		mailContext.put("from","sharadbhushank@gmail.com");
		//String[] to = {"1","2"}; 
		String to = "1"; 
		mailContext.put("to",to);
		mailContext.put("subject","Test Mail!");
		mailContext.put("template","test.ftl");
		Map<String,Object> bodyParameters = new HashMap<String, Object>();
		bodyParameters.put("name", "sharad");
		bodyParameters.put("company", "securet");
		//String templateString = processTemplate("test.ftl", bodyParameters);
		//System.out.println(templateString);
		System.out.println(mailContext.get("to").getClass().isArray());

	}
}
