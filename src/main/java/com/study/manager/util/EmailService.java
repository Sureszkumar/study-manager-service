package com.study.manager.util;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailService {

	public boolean sendEmail(Long userId, String email) {

		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.quitwait", "false");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("studymanagerapp@gmail.com", "muthamizhan");
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress("studymanagerapp@gmail.com"));
			message.setSubject("Study Manager : Verify email address");
			message.setContent("Please click below link to verify your email address", "text/plain");
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

			Transport.send(message);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}
