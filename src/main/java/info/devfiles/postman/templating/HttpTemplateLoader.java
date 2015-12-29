package info.devfiles.postman.templating;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class HttpTemplateLoader implements TemplateLoader {

	private String cacheSpec;
	
	private volatile Cache<String, String> templateCache;
	
	@PostConstruct
	public void initCache() {
		templateCache = CacheBuilder.from(getCacheSpec()).build();
	}
	
	public String loadFrom(final String source) throws UnavailableTemplateException {
		try {
			return templateCache.get(source, () -> {
				InputStream in = new URL(source).openStream();
				try {
				   return IOUtils.toString(in);
				} finally {
					IOUtils.closeQuietly(in);
				}
			});
		} catch (ExecutionException ee) {
			throw new UnavailableTemplateException(ee.getCause());	
		}
	}

	public String getCacheSpec() {
		return cacheSpec;
	}

	@Value("${info.devfiles.postman.tools.HttpTemplateLoader.cacheSpec}")
	public void setCacheSpec(String cacheSpec) {
		this.cacheSpec = cacheSpec;
	}

}
