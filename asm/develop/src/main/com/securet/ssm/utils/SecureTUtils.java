package com.securet.ssm.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.securet.ssm.persistence.objects.SecureTObject;

import freemarker.ext.beans.BeansWrapper;

public class SecureTUtils {

	private static final Logger _logger = LoggerFactory.getLogger(SecureTUtils.class);
	
	public static class FormField{
		private String fieldName;
		private String fieldType;
		private String fieldDesc;
		private boolean canDisplay;
		
		public FormField(String fieldName, String fieldType, String fieldDesc){
			this.fieldName=fieldName;
			this.fieldType=fieldType;
			this.fieldDesc=fieldDesc;
		}
		
		public FormField(String fieldName, String fieldType, boolean canDisplay, String fieldDesc) {
			this.fieldName=fieldName;
			this.fieldType=fieldType;
			this.canDisplay=canDisplay;
			this.fieldDesc=fieldDesc;
		}

		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getFieldType() {
			return fieldType;
		}
		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}
		public String getFieldDesc() {
			return fieldDesc;
		}
		public void setFieldDesc(String fieldDesc) {
			this.fieldDesc = fieldDesc;
		}

		public boolean isCanDisplay() {
			return canDisplay;
		}

		public void setCanDisplay(boolean canDisplay) {
			this.canDisplay = canDisplay;
		}

		@Override
		public String toString() {
			return "FormField [fieldName=" + fieldName + ", fieldType=" + fieldType + ", fieldDesc=" + fieldDesc + "]";
		}
	}

	public static String bCryptText(String text){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		String result = encoder.encode(text);
		return result;
	}
	
	public static List<FormField> getFieldsFromClass(Class<? extends SecureTObject> clazz){
		return getFieldsFromClass(clazz, null, null);
	}
	
	
	public static List<FormField> getFieldsFromClass(Class<? extends Object> clazz, List<String> excludeInDisplay, Map<String, String> customFieldTypes) {
		List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
		List<FormField> fieldList = new ArrayList<FormField>();				
		for(Field field : fields){
			boolean canDisplay = excludeInDisplay!=null?!excludeInDisplay.contains(field.getName()):true;
			String fieldName=field.getName();
			
			String fieldType = customFieldTypes!=null?customFieldTypes.get(field.getName()):null;
			if(fieldType==null){
				fieldType = decapitalize(field.getType().getSimpleName());
			}
			fieldList.add(new SecureTUtils.FormField(fieldName,fieldType,canDisplay,null));
		}
		if(clazz.getSuperclass()!=null){
			fieldList.addAll(getFieldsFromClass(clazz.getSuperclass(),excludeInDisplay,customFieldTypes));
		}
		return fieldList;
	}

	/**
	 * Implementation copied  from javax.beans.Introspector.decapitalize(String name)
	 * @param name
	 * @return
	 */
			
    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                        Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

	public static int getFieldsCount(Class<? extends Object> clazz) {
		int count = clazz.getDeclaredFields().length;
		if(clazz.getSuperclass()!=null){
			count = count + getFieldsCount(clazz.getSuperclass());
		}
		return count;
	}
	
	public static String saveToFile(MultipartFile inputFile, String path,String fileName){
		URL url = Thread.currentThread().getContextClassLoader().getResource("../../"+path);
		if(url!=null){
			String filePath = url.getPath()+fileName;
			_logger.debug("destPath  :  "+filePath);
			File destFile = new File(filePath);
			try {
				FileUtils.writeByteArrayToFile(destFile, inputFile.getBytes());
				//FileUtils.copyFile(inputFile, destFile);
				return filePath;
			} catch (IOException e) {
				_logger.error("Could not file to path : "+filePath,e);
			}
		}
		return null;
	}

	public static String getFileExtension(String contentType){
		String[] contentTypeParts = contentType.split("/");
		if(contentTypeParts.length>0){
			return "."+contentTypeParts[1];
		}else{
			_logger.warn("Could not make out a extension for given content type..");
			return ""; 
		}
	}

	public static boolean isEmpty(String str) {
		return (str== null || str.isEmpty());
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(List<?> list){
		return (list==null || list.isEmpty());
	}
	
	public static boolean isNotEmpty(List<?> list){
		return !isEmpty(list);
	}

	public static List<String> fetchCSVAsList(String mobile) {
		if(!isEmpty(mobile)){
			return Arrays.asList(mobile.split(","));
		}
		return null;
	}

	public static String formatTimeInHrsMins(long timeInMillis){
		// get total seconds between the times
		long delta = timeInMillis;
		// calculate (and subtract) whole days
		int days = (int) Math.floor(delta / 86400);
		delta -= days * 86400;
		// calculate (and subtract) whole hours
		int hours = (int) (Math.floor(delta / 3600) % 24);
		delta -= hours * 3600;
		// calculate (and subtract) whole minutes
		int minutes = (int) (Math.floor(delta / 60) % 60);
		delta -= minutes * 60;
		// what's left is seconds
		int seconds = (int) (delta % 60);  // in theory the modulus is not required
		StringBuilder formatStrParts = new StringBuilder();
		if(days>0){
			formatStrParts.append(days+(days>1?" days ":" day "));
		}
		if(hours >0){
			formatStrParts.append(hours+(hours>1?" hrs ":" hr "));
		}
		if(formatStrParts.length()<2){
			formatStrParts.append(minutes+(minutes>1?" mins ":" min "));
		}
		if(formatStrParts.length()<2){
			formatStrParts.append(seconds+(seconds>1?" secs ":" sec "));
		}
		return formatStrParts.toString();
	}
	
	/**
	 * This will soon be removed once we can configure this across the config. Using a workaround for now.. 
	 * @param freemarkerConfig
	 * @param model
	 */
	public static void addFTLStaticModelToModelAttribute(FreeMarkerConfigurer freemarkerConfig,Model model){
		if(!model.containsAttribute("statics")){
			model.addAttribute("statics",((BeansWrapper)freemarkerConfig.getConfiguration().getObjectWrapper()).getStaticModels());
		}
	}
	
	public static void main(String[] args) {
		String[] empIds = {"securet","1122053","1131001","1146823","1862359","1869175","1874551","2850672","2850729","2861208","2861836","2867907","2869691","2870924","2872242","2936941","3322122","3324443","3325636","3339785","3345181","3345378","4156854","4229002","4310446","4312864","4318781","4318838","4319680","4320182","4320271","4320344","4326709","4331621","4335287","5237289","5552346","5552370","5556023","5784026","5787793","6063322","6080235","6081320","6084230","6087604","6089070","6092497"};
		for(int i=0;i<empIds.length;i++){
			System.out.println(empIds[i]+"="+bCryptText(empIds[i]));
		}
		//_logger.debug("password 'securet' = "+bCryptText("securet"));
		//field test
		//List<FormField> fields = getFieldsFromClass(Organization.class);
		//System.out.println(fields);
		//System.out.println(getFieldsCount(Organization.class));
	}
}