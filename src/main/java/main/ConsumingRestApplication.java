package main;


	import java.util.Collections;

import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.boot.CommandLineRunner;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.client.RestTemplateBuilder;
	import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import vo.LoginVO;
import vo.TokenVO;
import webservice.RestWebServiceV1;

	@SpringBootApplication
	@ComponentScan(basePackageClasses = RestWebServiceV1.class)
	public class ConsumingRestApplication {
		//public static RestTemplate restTemplate
		private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

		public static void main(String[] args) {
	
			 SpringApplication app = new SpringApplication(ConsumingRestApplication.class);
		        app.setDefaultProperties(Collections
		          .singletonMap("server.port", "8083"));
		      //  spring.main.web-application-type=none
		        app.run(args);
		}

		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
			return builder.build();
		}

		@Bean
		public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
			/*LoginVO login = new LoginVO("admin","admin");
			 
			return args -> {
				TokenVO quote = restTemplate.postForObject(
						"http://localhost:8080/api/authenticate",login, TokenVO.class);
				log.info(quote.getId_token());
			};*/
			return null;
		}
	}
