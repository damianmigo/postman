package info.devfiles.postman;

import info.devfiles.postman.EmailMessage;

public abstract class EmailSender {

	protected void beforeSend(EmailMessage emailMessage) {
		
	}
	
	public void send(EmailMessage emailMessage) {
		beforeSend(emailMessage);
		doSend(emailMessage);
		afterSend(emailMessage);
	}
	
	protected abstract void doSend(EmailMessage emailMessage);
	
	protected void afterSend(EmailMessage emailMessage) {
		
	}
	
}
