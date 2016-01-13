package info.devfiles.postman.templating;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateLoaderMultiton {

	private Map<String, TemplateLoader> multiton = new ConcurrentHashMap<String, TemplateLoader>();
	
	public void registerInstace(String sourceType, TemplateLoader templateLoader) {
		multiton.put(sourceType, templateLoader);
	}
	
	public TemplateLoader getInstance(String sourceType) {
		TemplateLoader instance = multiton.get(sourceType);
		if (instance == null) {
			throw new InvalidParameterException(
					String.format("template loader of source type %s doesn't exist", sourceType));
		}
		return instance;
	}
	
}
