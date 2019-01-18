package com.tiny.serviceImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tiny.service.MailerService;
import com.tiny.utils.TinyUtils;

@Service
public class MailerServiceImpl implements MailerService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromAddress;
	
	@Value("${app.mail_to_Address}")
	private String toMailsAddress;
	
	@Value("${app.mail_cc_Addresses}")
	private String ccAddresses;

	public void sendCSVLogs(String locPath) throws MessagingException {
		try {
			
			String[] cc;
			cc = ccAddresses.split(",");
			
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSubject("Daily Report");
			helper.setText("Test Mail for daily logs with CC added");
			helper.setTo(toMailsAddress);
			helper.setFrom(fromAddress);
			helper.setCc(cc);
			FileSystemResource file = new FileSystemResource(locPath);
			helper.addAttachment(file.getFilename(), file);

			javaMailSender.send(message);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}