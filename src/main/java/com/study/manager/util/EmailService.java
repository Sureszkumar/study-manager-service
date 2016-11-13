package com.study.manager.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import freemarker.template.Template;
import freemarker.template.TemplateException;
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
	public void sendVerifyToken(Long userId, String email) {

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
			String newUserEmailContent = getNewUserEmailContent(email, email, url.toString());
			message.setContent(newUserEmailContent, "text/html");
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

			Transport.send(message);
		} catch (Exception e) {
		}

	}

	public void sendNewPassword(String email, String password) {
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
		try {
			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(mailUsername));
			message.setSubject("Study Manager Request New password");
			String newUserEmailContent = getForgotPasswordContent(email, email, password);
			message.setContent(newUserEmailContent, "text/html");
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			Transport.send(message);
		} catch (Exception e) {
		}

	}

	public String getNewUserEmailContent(String userName, String userEmail, String verifyEmailLink) {
		FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine("new_user.ftl");
		Writer stringWriter = new StringWriter();
		Template template = null;
		try {
			template = freemarkerTemplateEngine.getTemplate();
			Map<String, Object> input = new HashMap<>();
			input.put("userName", userName);
			input.put("userEmail", userEmail);
			input.put("verifyEmailLink", verifyEmailLink);
			template.process(input, stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}

	public String getForgotPasswordContent(String userName, String userEmail, String newPassword) {
		FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine("forgot_password.ftl");
		Writer stringWriter = new StringWriter();
		Template template = null;
		try {
			template = freemarkerTemplateEngine.getTemplate();
			Map<String, Object> input = new HashMap<>();
			input.put("userName", userName);
			input.put("userEmail", userEmail);
			input.put("newPassword", newPassword);
			template.process(input, stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}



	public static void main(String ap[]){
		EmailService emailService = new EmailService();
		String htmlContent = emailService.getNewUserEmailContent("Suresh", "sureszkumar@gmail.com",
				"http://localhost:8080/user/verifyEmail?user=MA==&token=EED81280-D3A7-404C-9939-5C696D93103AMA==");
		System.out.println(htmlContent);
	}
}
