package info.devfiles.postman;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("AwsEmailSender")
public class AwsEmailSender extends EmailTemplateSender {

	static final Logger logger = LogManager.getLogger(AwsEmailSender.class);
	
	private JavaMailSender mailSender;
	
	@Override
	public void doSend(EmailMessage emailMessage) {		
		getMailSender().send((MimeMessage mimeMessage) -> {
			logger.info("Sending " + emailMessage);
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
