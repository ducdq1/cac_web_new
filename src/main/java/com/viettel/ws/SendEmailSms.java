/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ws;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.ws.sendEmailAndSMS.transaction.MessageEmail;

/**
 *
 * @author GPCP_BINHNT53
 */
public class SendEmailSms {

	Properties properties = new Properties();
	// sms and email unsent buffer
	// Flag choose Email
	// Email configuration
	private String emailUsername; // username cua email
	private String emailPassword; // password cua email
	private String emailSMTPHost; // dia chi server gui email (SMTP Host)
	private String emailAlias; // alias cua email, hien thi thay cho dia chi
								// email tai hom thu den
	private String emailDefaultSubject; // tieu de thu mac dinh
	private String emailSecure; // dang giao thuc bao mat de gui tin nhan
	private int emailSendDuration; // chu ky hoat dong cua dich vu gui email
	private String lstMailCC; // username cua email
	// db configuration
	private static String dbConnecter;

	// private static final Logger log =
	// Logger.getLogger(SendSMSAndEmail.class);
	public boolean onSendEmail(MessageEmail messageEmail) {
		// Load Config
		loadConfig();
		return sendEmail(messageEmail);
	}

	public SendEmailSms() {
	}

	public void loadConfig() {
		// get email properties
		try {
			lstMailCC = ResourceBundleUtil.getString("lstMailCC", "config");
			emailUsername = ResourceBundleUtil.getString("emailUsername", "config");
			emailPassword = ResourceBundleUtil.getString("emailPassword", "config");
			emailSMTPHost = ResourceBundleUtil.getString("emailSMTPHost", "config");

			emailAlias = ResourceBundleUtil.getString("emailAlias", "config");
			emailDefaultSubject = ResourceBundleUtil.getString("emailDefaultSubject", "config");

			emailSecure = ResourceBundleUtil.getString("emailSecure", "config");
			emailSendDuration = Integer.parseInt(properties.getProperty("emailSendDuration"));

			System.out.println("dbConnecter: " + dbConnecter);
			System.out.println("Email using: " + emailUsername);
			System.out.println("SMTP Host: " + emailSMTPHost);
			System.out.println("Load properties successfully. Sending email each " + emailSendDuration + " seconds.");
		} catch (UnsupportedEncodingException | NumberFormatException e) {
			LogUtils.addLog(e);
		}
	}

	// ham thuc hien gui 1 email
	public boolean sendEmail(MessageEmail messageEmail) {
		if (messageEmail == null) {
			return false;
		}
		try {
			// proxy settings, temporary
			/*
			 * System.getProperties().put("proxySet", "true");
			 * System.getProperties().put("proxyHost", "10.224.143.53");
			 * System.getProperties().put("proxyPort", "8080");
			 */
			// cau hinh gui tin nhan
			Properties props = new Properties();
			props.put("mail.smtp.host", emailSMTPHost);
			props.put("mail.smtp.user", emailUsername);
			props.put("mail.smtp.password", emailPassword);
			props.put("mail.transport.protocol", "smtp");

			switch (emailSecure) {
			case "SSL":
				// cau hinh giao thuc SMTPS su dung SSL
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				break;
			case "TLS":
				// cau hinh giao thuc SMTPS su dung TLS
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");
				break;
			default:
				// cau hinh giao thuc SMTP thong thuong
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.auth", "true");
			}

			Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUsername, emailPassword);
				}
			});

			/* Receive bugs from gmail */
			// session.setDebug(true);
			// gui tin nhan co bat ngoai le
			try {
				InternetAddress fromAddress = new InternetAddress(emailUsername, emailAlias);

				String toAddress = messageEmail.getReceiveEmail();
				if (toAddress != null && !"".equals(toAddress) && !"".equals(toAddress.trim())) {
					System.out.print(new Date() + ": Sending email to " + toAddress + "...");

					Message message = new MimeMessage(session);
					message.setFrom(fromAddress);
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
					if (lstMailCC != null && lstMailCC != "") {
						message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(lstMailCC));
					}
					message.setSubject(emailDefaultSubject);
					message.setContent(messageEmail.getContent(), "text/html; charset=utf-8");

					Transport transport = session.getTransport("smtp");
					transport.connect(emailSMTPHost, emailUsername, emailPassword);

					Transport.send(message);
					transport.close();
					System.out.println(new Date() + ": successfully.");
					return true;
				}
				return false;
			} catch (MessagingException | UnsupportedEncodingException ex) {
				LogUtils.addLog(ex);

				messageEmail.setErrorMsg(ex.getMessage());
				// log.error(ex);
				return false;
			}
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);

			// log.error(ex);
			messageEmail.setErrorMsg(ex.getMessage());
			return false;
		}
	}
	/*
	 * MessageEmail me = new MessageEmail(); me.setContent(
	 * "dinh menh cho ta gap nhau");
	 * me.setReceiveEmail("binh.ninhthanh@gmail.com"); SendEmailSms se = new
	 * SendEmailSms(); se.onSendEmail(me);
	 */

	public boolean sendEmailManual(String subject, String email, String content) {
		loadConfig();
		if (email == null || content == null) {
			return false;
		}
		try {
			// proxy settings, temporary

			// System.getProperties().put("proxySet", "true");
			// System.getProperties().put("proxyHost", "10.61.11.38");
			// System.getProperties().put("proxyPort", "3128");

			// cau hinh gui tin nhan
			Properties props = new Properties();
			props.put("mail.smtp.host", emailSMTPHost);
			props.put("mail.smtp.user", emailUsername);
			props.put("mail.smtp.password", emailPassword);
			props.put("mail.transport.protocol", "smtp");

			switch (emailSecure) {
			case "SSL":
				// cau hinh giao thuc SMTPS su dung SSL
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				break;
			case "TLS":
				// cau hinh giao thuc SMTPS su dung TLS
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");
				break;
			default:
				// cau hinh giao thuc SMTP thong thuong
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.auth", "true");
			}

			Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUsername, emailPassword);
				}
			});

			/* Receive bugs from gmail */
			// session.setDebug(true);
			// gui tin nhan co bat ngoai le
			try {
				InternetAddress fromAddress = new InternetAddress(emailUsername, emailAlias);
				if (!"".equals(email) && !"".equals(email.trim())) {

					Message message = new MimeMessage(session);
					message.setFrom(fromAddress);
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
					if (lstMailCC != null && lstMailCC != "") {
						message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(lstMailCC));
					}
					message.setSubject(subject);
					message.setContent(content, "text/html; charset=utf-8");

					Transport transport = session.getTransport("smtp");
					transport.connect(emailSMTPHost, emailUsername, emailPassword);

					Transport.send(message);
					transport.close();
					return true;
				}
				return false;
			} catch (MessagingException ex) {
				LogUtils.addLog(ex);

				return false;
			}
		} catch (Exception ex) {
			LogUtils.addLog(ex);
			return false;
		}
	}
}
