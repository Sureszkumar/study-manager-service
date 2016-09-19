package com.study.manager.util;

import java.net.URL;
import java.security.Security;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.study.manager.service.UserService;

@Component
public class EmailService {
	
	@Inject
	public UserService userRepository;
	
	@Value("${mail.host}")
	private String mailHost;


	@Value("${mail.username}")
	private String mailUsername;
	

	@Value("${mail.password}")
	private String mailPassword;

	@Value("${domain.host}")
	private String domainHost;

	@Value("${mail.subject}")
	private String mailSubject;

	@Value("${mail.content}")
	private String mailContent;
	@Async
	public void sendEmail(Long userId, String email) {

		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", mailHost);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.quitwait", "false");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailUsername, mailPassword);
				}
			});
			String emailVerifyToken = ServiceUtils.createEmailVerifyToken(String.valueOf(userId));
			userRepository.updateEmailVerifyToken(userId, emailVerifyToken);
			String encryptedUserId = ServiceUtils.encrypttUserId(String.valueOf(userId));
			URL domain = new URL(domainHost);
			URL url = new URL(domain + "/user/verifyEmail?user=" + encryptedUserId + "&token=" + emailVerifyToken);
			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(mailUsername));
			message.setSubject(mailSubject);
			message.setContent(mailContent + "\n" + url, "text/plain");
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

			Transport.send(message);
		} catch (Exception e) {
		}

	}
}
