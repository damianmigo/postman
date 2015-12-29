package info.devfiles.postman.tools;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import info.devfiles.postman.EmailTemplateMessage;
import info.devfiles.postman.TemplateParameters;
import info.devfiles.postman.templating.TemplateEngineWrapper;
import info.devfiles.postman.templating.TemplateEngineWrapperMultiton;
import info.devfiles.postman.templating.TemplateLoader;
import info.devfiles.postman.templating.TemplateLoaderMultiton;

public class EmailTemplateTools {
	
	private TemplateEngineWrapperMultiton templateEngineWrapperMultiton;
	
	private TemplateLoaderMultiton templateLoaderMultiton;

	public String getTemplate(String sourceType, String source) {
		TemplateLoader templateLoader = getTemplateLoaderMultiton().getInstance(sourceType);
		return templateLoader.loadFrom(source);
	}
	
	public String applyTemplate(String templateContent, String engineName, Map<String, Object> attributes) {
		TemplateEngineWrapper templateEngineWrapper = getTemplateEngineWrapperMultiton().getInstance(engineName);
		return 	templateEngineWrapper.apply(templateContent, attributes);
	}
	
	public void proccessEmailTemplateMessage(EmailTemplateMessage emailTemplateMessage) {
		TemplateParameters templateParameters = emailTemplateMessage.getTemplateParameters();
		String template = getTemplate(templateParameters.getSourceType(), templateParameters.getSource());
		String body = applyTemplate(template, templateParameters.getEngineName(), templateParameters.getAttributes());
		emailTemplateMessage.setBody(body);
	}

	public TemplateEngineWrapperMultiton getTemplateEngineWrapperMultiton() {
		return templateEngineWrapperMultiton;
	}

	@Autowired
	public void setTemplateEngineWrapperMultiton(TemplateEngineWrapperMultiton templateEngineWrapperMultiton) {
		this.templateEngineWrapperMultiton = templateEngineWrapperMultiton;
	}

	public TemplateLoaderMultiton getTemplateLoaderMultiton() {
		return templateLoaderMultiton;
	}

	@Autowired
	public void setTemplateLoaderMultiton(TemplateLoaderMultiton templateLoaderMultiton) {
		this.templateLoaderMultiton = templateLoaderMultiton;
	}
	
}
