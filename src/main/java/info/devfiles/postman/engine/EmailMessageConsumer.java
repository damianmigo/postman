package info.devfiles.postman.engine;

import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import info.devfiles.postman.EmailMessage;
import info.devfiles.postman.config.EmailSender;

@Service
@Scope("prototype")
public class EmailMessageConsumer extends MultithreadQueueConsumer<EmailMessage> {
	
	private EmailSender emailSender;

	public EmailMessageConsumer(BlockingQueue<EmailMessage> queue, int nThreads) {
		super(queue, nThreads);
	}

	@Override
	public void handle(EmailMessage obj) {
		getEmailSender().send(obj);
	}

	public EmailSender getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

}
