package com.example.ipl.iplstats.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private BasicAuthenticationPoint basicAuthenticationPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
                .antMatchers("/login","/oauth/authorize","/v2/api-docs","/swagger-ui.html",
                        "/swagger-resources/configuration/ui","/swagger-resources","/swagger-resources/configuration/security")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
//                .and()
//                .formLogin()
//                .permitAll();
        http.httpBasic().authenticationEntryPoint(basicAuthenticationPoint);
    }


    

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.parentAuthenticationManager(authenticationManager)
//                .inMemoryAuthentication()
//                .withUser("Uday")
//                .password("uday")
//                .roles("USER");
//    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("Uday").password("uday").roles("USER");
    }

}
