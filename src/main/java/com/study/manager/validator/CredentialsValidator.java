package com.study.manager.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class CredentialsValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public CredentialsValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public boolean validate(String email, String password) {
		boolean isValidCredentials;
		matcher = pattern.matcher(email);
		isValidCredentials = matcher.matches();
		return isValidCredentials;

	}
}
