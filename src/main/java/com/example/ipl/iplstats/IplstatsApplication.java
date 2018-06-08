package com.example.ipl.iplstats;

import com.example.ipl.iplstats.utility.SeasonLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableResourceServer
public class IplstatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IplstatsApplication.class, args);
	}


//	@Bean
//	public SeasonLoader loadSeason(){
//			return new SeasonLoader();
//	}
	/**
	 * Allows for @PreAuthorize annotation processing.
	 */
//	@EnableGlobalMethodSecurity(prePostEnabled = true)
//	protected static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
//		@Override
//		protected MethodSecurityExpressionHandler createExpressionHandler() {
//			return new OAuth2MethodSecurityExpressionHandler();
//		}
//	}
//
//	@Bean
//	public OAuthProperties oAuthProperties() {
//		return new OAuthProperties();
//	}



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
