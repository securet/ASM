package com.securet.asm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ASMUtils {

	private static final Logger _logger = LoggerFactory.getLogger(ASMUtils.class);
	
	public static String bCryptText(String text){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		String result = encoder.encode(text);
		return result;
	}
	
	public static void main(String[] args) {
		_logger.debug("password 'shabhu' = "+bCryptText("shabhu"));
	}
}
