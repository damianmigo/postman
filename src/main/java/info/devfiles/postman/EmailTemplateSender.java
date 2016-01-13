package info.devfiles.postman;

import org.springframework.beans.factory.annotation.Autowired;

import info.devfiles.postman.EmailMessage;
import info.devfiles.postman.EmailTemplateMessage;
import info.devfiles.postman.tools.EmailTemplateTools;

public abstract class EmailTemplateSender extends EmailSender {
	
	private EmailTemplateTools emailTemplateTools;
	
	@Override
	protected void beforeSend(EmailMessage emailMessage) {
		super.beforeSend(emailMessage);
		if (emailMessage instanceof EmailTemplateMessage) {
			getEmailTemplateTools().proccessEmailTemplateMessage((EmailTemplateMessage) emailMessage);
		}
	}

	public EmailTemplateTools getEmailTemplateTools() {
		return emailTemplateTools;
	}

	@Autowired
	public void setEmailTemplateTools(EmailTemplateTools emailTemplateTools) {
		this.emailTemplateTools = emailTemplateTools;
	}

}
