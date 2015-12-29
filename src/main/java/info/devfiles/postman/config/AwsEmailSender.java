package info.devfiles.postman.config;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import info.devfiles.postman.EmailMessage;

public class AwsEmailSender extends EmailTemplateSender {

	private JavaMailSender mailSender;
	
	@Override
	public void doSend(EmailMessage emailMessage) {		
		getMailSender().send((MimeMessage mimeMessage) -> {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setFrom(emailMessage.getFrom());
			mimeMessageHelper.setTo(emailMessage.getTo());
			mimeMessageHelper.setSubject(emailMessage.getSubject());
			mimeMessageHelper.setText(emailMessage.getBody(), emailMessage.isHtml());
		});
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
