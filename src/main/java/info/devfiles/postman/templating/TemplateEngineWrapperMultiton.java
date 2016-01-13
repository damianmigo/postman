package info.devfiles.postman.templating;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateEngineWrapperMultiton {

	private Map<String, TemplateEngineWrapper> multiton = new ConcurrentHashMap<String, TemplateEngineWrapper>();
		
	public void registerInstace(String engineName, TemplateEngineWrapper templateEngineWrapper) {
		multiton.put(engineName, templateEngineWrapper);
	}
	
	public TemplateEngineWrapper getInstance(String engineName) {
		TemplateEngineWrapper instance = multiton.get(engineName);
		if (instance == null) {
			throw new InvalidParameterException(String.format("template engine %s doesn't exist", engineName));
		}
		return instance;
	}
	
}
