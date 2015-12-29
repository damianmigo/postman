package info.devfiles.postman.templating;

public class UnavailableTemplateException extends RuntimeException {

	private static final long serialVersionUID = 1776648340941947855L;

	public UnavailableTemplateException() {
		super();
	}
	
	public UnavailableTemplateException(String message) {
		super(message);
	}
	
	public UnavailableTemplateException(Throwable cause) {
		super(cause);
	}
	
}
