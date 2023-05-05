package io.javabrains.bigreads;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.bigreads.connection.DataStaxAstraProperties;


@SpringBootApplication
//@RestController
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BigReadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigReadsApplication.class, args);
	}

	
//	@RequestMapping("/user")
//	//this is a rest api, accessible when someone is logged in- authenticated person- accessible whe ur authenticated 
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal); 
//		return principal.getAttribute("name");
//	}
	
	//bean which allows connection to datastax 
	//tells where the secure bundle file is (the path to it) so can use that batabase thats active on datastax 
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	

}
