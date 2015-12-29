package info.devfiles.postman.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

import info.devfiles.postman.templating.HttpTemplateLoader;
import info.devfiles.postman.templating.StringTemplateWrapper;
import info.devfiles.postman.templating.TemplateEngineWrapperMultiton;
import info.devfiles.postman.templating.TemplateLoaderMultiton;

@Configuration
@PropertySource("classpath:defaults.properties")
@EnableContextCredentials(accessKey="${postman.awsAccessKey}", secretKey="${postman.awsSecretKey}")
public class ModuleConfig {

	private String awsRegion;
	
	@Bean
	public JavaMailSender mailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
		Region awsRegion = Region.getRegion(Regions.fromName(getAwsRegion()));
		amazonSimpleEmailService.setRegion(awsRegion);
	    return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
	}

	@Bean(destroyMethod="shutdown")
	public AmazonSimpleEmailServiceClient amazonSimpleEmailService(AWSCredentialsProvider credentialsProvider) {
	    return new AmazonSimpleEmailServiceClient(credentialsProvider);
	}

	@Bean
	public TemplateEngineWrapperMultiton templateEngineWrapperMultiton() {
		TemplateEngineWrapperMultiton templateEngineWrapperMultiton = new TemplateEngineWrapperMultiton();
		templateEngineWrapperMultiton.registerInstace("StringTemplate", stringTemplateWrapper());
		return templateEngineWrapperMultiton;
	}
	
	@Bean
	public StringTemplateWrapper stringTemplateWrapper() {
		return new StringTemplateWrapper();
	}
	
	@Bean
	public TemplateLoaderMultiton templateLoaderMultiton() {
		TemplateLoaderMultiton templateLoaderMultiton = new TemplateLoaderMultiton();
		templateLoaderMultiton.registerInstace("HTTP", httpTemplateLoader());
		return templateLoaderMultiton();
	}
	
	@Bean
	public HttpTemplateLoader httpTemplateLoader() {
		return new HttpTemplateLoader();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public String getAwsRegion() {
		return awsRegion;
	}

	@Value("${postman.awsRegion}")
	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

}
