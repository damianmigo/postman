package info.devfiles.postman.templating;

public interface TemplateLoader {

	String loadFrom(String source) throws UnavailableTemplateException;
	
}
