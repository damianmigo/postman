package info.devfiles.postman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("FakeEmailSender")
public class FakeEmailSender extends EmailTemplateSender {

	static final Logger logger = LogManager.getLogger(FakeEmailSender.class);
	
	private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
		
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH mm ss");
		}
		
    };
    
    private static final ThreadLocal<AtomicLong> COUNTER = new ThreadLocal<AtomicLong>() {
    	
    	@Override
    	protected AtomicLong initialValue() {
			return new AtomicLong();
		}
    	
    };
	
	private String outputDirectory;
	
	@PostConstruct
	public void createOutputDirectory() {
		new File(outputDirectory).mkdirs();
	}
	
	@Override
	public void doSend(EmailMessage emailMessage) {		
		Path outputPath = Paths.get(getOutputDirectory(), generateFilename());
		try (
			BufferedWriter writer = Files.newBufferedWriter(outputPath, Charset.defaultCharset());
		) {
			writer.write("From: ");
			writer.write(emailMessage.getFrom());
			writer.write(System.lineSeparator());
			
			writer.write("To: ");
			writer.write(emailMessage.getTo());
			writer.write(System.lineSeparator());
			
			writer.write("Subject: ");
			writer.write(emailMessage.getSubject());
			writer.write(System.lineSeparator());
			writer.write(System.lineSeparator());
			
			writer.write(emailMessage.getBody());
			
			logger.info("Email message " + outputPath + " has been written to disk successfully");
		} catch (IOException ioe) {
			logger.warn("Cannot write email message to disk", ioe);
		}
	}
	
	protected String generateFilename() {
		Date now = new Date();
		Long threadId = Thread.currentThread().getId();
		return String.format("%s %d-%d.txt",
				DATE_FORMATTER.get().format(now),
				threadId,
				COUNTER.get().incrementAndGet());
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	@Value("${info.devfiles.postman.tools.FakeEmailSender.outputDirectory}")
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
}
