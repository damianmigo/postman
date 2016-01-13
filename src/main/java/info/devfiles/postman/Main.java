package info.devfiles.postman;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.util.StreamUtils;

import info.devfiles.postman.config.ModuleConfig;
import info.devfiles.postman.engine.EmailMessageConsumer;

public class Main implements Daemon {
	
	static final Logger logger = LogManager.getLogger(Main.class);
	
	private AnnotationConfigApplicationContext applicationContext;
	
	private EmailMessageConsumer emailMessageConsumer;
	
	private static final String BANNER_FILENAME = "banner.txt";
	
	private static final int DEFAULT_STOP_TIMEOUT_IN_MINUTES = 5;

	public static void main(String[] args) throws DaemonInitException, Exception {
		DaemonContext daemonContext = new DaemonContext() {
			
			@Override
			public DaemonController getController() {
				return null;
			}
			
			@Override
			public String[] getArguments() {
				return args;
			}
			
		};
		
		logger.info("Starting application" + System.lineSeparator() + readApplicationBanner());
		
		Main main = new Main();
		main.init(daemonContext);
		main.start();
	}

	@Override
	public void destroy() {
		applicationContext.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {
		
		CommandLineParser parser = new DefaultParser();
		Options options = createOptions();
		
		try {
			CommandLine cmd = parser.parse(options, daemonContext.getArguments());
			
			applicationContext = new AnnotationConfigApplicationContext();
			applicationContext.register(ModuleConfig.class);
			applicationContext.refresh();
			
			RedisTemplate<String, EmailMessage> redisTemplate = applicationContext.getBean(RedisTemplate.class);
			DefaultRedisList<EmailMessage> emailMessageQueue = new DefaultRedisList<EmailMessage>(
					cmd.getOptionValue("queue"),
					redisTemplate);
			
			EmailSender emailSender = (EmailSender) applicationContext.getBean(cmd.getOptionValue("sender"));
			
			emailMessageConsumer = (EmailMessageConsumer) applicationContext.getBean(
					"EmailMessageConsumer",
					emailMessageQueue,
					Integer.parseInt(cmd.getOptionValue("threads")));
			emailMessageConsumer.setEmailSender(emailSender);
			
		} catch (ParseException | NumberFormatException e) {
			logger.error("Parsing failed", e);
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("postman-client", options);
		}
	}

	@Override
	public void start() throws Exception {
		emailMessageConsumer.start();
	}

	@Override
	public void stop() throws Exception {
		emailMessageConsumer.stop(DEFAULT_STOP_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
	}
	
	private Options createOptions() {
		Options options = new Options();
		
		Option sender = Option.builder("s")
				.required(true)
				.hasArg()
				.longOpt("sender")
				.desc("sender bean name")
				.build();
		
		Option threads = Option.builder("t")
				.required(true)
				.hasArg()
				.longOpt("threads")
				.desc("number of worker threads")
				.build();
		
		Option queue = Option.builder("q")
				.required(true)
				.hasArg()
				.longOpt("queue")
				.desc("Redis queue name")
				.build();
		
		options.addOption(sender);
		options.addOption(threads);
		options.addOption(queue);
		
		return options;
	}
	
	private static String readApplicationBanner() throws IOException {
		InputStream in = Main.class.getClassLoader().getResourceAsStream(BANNER_FILENAME);
		try {
			return StreamUtils.copyToString(in, Charset.defaultCharset());
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
}
