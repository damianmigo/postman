package info.devfiles.postman;

import java.util.concurrent.TimeUnit;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;

import info.devfiles.postman.config.ModuleConfig;
import info.devfiles.postman.engine.EmailMessageConsumer;

public class Main implements Daemon {
	
	private AnnotationConfigApplicationContext applicationContext;
	
	private EmailMessageConsumer emailMessageConsumer;

	public static void main(String[] args) {
	
	}

	@Override
	public void destroy() {
		applicationContext.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {
		
		String[] args = daemonContext.getArguments();
		String queueName = args[0];
		int nThreads = Integer.parseInt(args[1]);
		
		applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(ModuleConfig.class);
		applicationContext.refresh();

		RedisTemplate<String, String> redisTemplate = applicationContext.getBean(RedisTemplate.class);
		DefaultRedisList<String> emailMessageQueue = new DefaultRedisList<String>(queueName, redisTemplate);
		
		emailMessageConsumer = applicationContext.getBean(EmailMessageConsumer.class, emailMessageQueue, nThreads);
	}

	@Override
	public void start() throws Exception {
		emailMessageConsumer.start();
	}

	@Override
	public void stop() throws Exception {
		emailMessageConsumer.stop(5, TimeUnit.MINUTES);
	}
	
}
