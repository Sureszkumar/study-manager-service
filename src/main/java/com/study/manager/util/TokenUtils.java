package com.study.manager.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

	public String getToken(UserDetails userDetails) {
		return "";
	}

	public String getToken(UserDetails userDetails, Long expiration) {
		return "";
	}

	public boolean validate(String token) {
		return true;
	}

	public UserDetails getUserFromToken(String token) {
		return null;
	}
}
