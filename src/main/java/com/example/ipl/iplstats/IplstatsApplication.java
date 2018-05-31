package com.example.ipl.iplstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@SpringBootApplication
@EnableResourceServer
public class IplstatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IplstatsApplication.class, args);
	}

	/**
	 * Allows for @PreAuthorize annotation processing.
	 */
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	protected static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return new OAuth2MethodSecurityExpressionHandler();
		}
	}

//	@RestController
//	@PreAuthorize("#oauth2.hasScope('custom_mod')")
//	public class MessageOfTheDayController {
//		@GetMapping("/mod")
//		public String getMessageOfTheDay(Principal principal) {
//			return "The message of the day is boring for user: " + principal.getName();
//		}
//
//		@GetMapping("/test")
//		public String testMessage(Principal principal) {
//			return "Test Message: " + principal.toString();
//		}
//	}
}
