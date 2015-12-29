package info.devfiles.postman.templating;

import java.util.Map;
import java.util.Map.Entry;

import org.stringtemplate.v4.ST;

public class StringTemplateWrapper extends TemplateEngineWrapper {

	@Override
	public String apply(String template, Map<String, Object> attributes) {
		ST stringTemplate = new ST(template);
		for (Entry<String, Object> attributeEntry : attributes.entrySet()) {
			stringTemplate.add(attributeEntry.getKey(), attributeEntry.getValue());
		}
		return stringTemplate.render();
	}
	
}
