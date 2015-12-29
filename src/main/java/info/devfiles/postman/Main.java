package info.devfiles.postman;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import info.devfiles.postman.config.ModuleConfig;

public class Main {

	public static void main(String[] args) {
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ModuleConfig.class);
		ctx.refresh();
		
		/*SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("admin@devfiles.info");
		simpleMailMessage.setTo("damian.miranda1@gmail.com");
		simpleMailMessage.setSubject("test subject");
		simpleMailMessage.setText("test content");*/
		
		
		JavaMailSender mailSender = ctx.getBean(JavaMailSender.class);
		
		
		mailSender.send(new MimeMessagePreparator() {
			   public void prepare(MimeMessage mimeMessage) throws MessagingException {
				     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				     message.setFrom("admin@devfiles.info");
				     message.setTo("damian.miranda1@gmail.com");
				     message.setSubject("my subject");
				     message.setText("<h1>this is</h1> <strong>HTML</strong>", true);
				   }
				 });
		
		ctx.close();
		
		
	}
	
}
