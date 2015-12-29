package info.devfiles.postman.templating;

import java.util.Map;

public abstract class TemplateEngineWrapper {

	public abstract String apply(String template, Map<String, Object> attributes);
	
}
