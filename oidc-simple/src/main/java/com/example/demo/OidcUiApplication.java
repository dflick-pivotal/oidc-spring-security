package com.example.demo;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OidcUiApplication extends WebSecurityConfigurerAdapter {

    
	public static void main(String[] args) {
		SpringApplication.run(OidcUiApplication.class, args);
	}
		
	@RequestMapping("/authorities")
    String authorities(@AuthenticationPrincipal OidcUser user) {
        return "authorities: " + user.getAuthorities().toString();
    }

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
				.antMatchers("/webjars/**");

	}
   	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.antMatcher("/**").authorizeRequests()
         .antMatchers("/", "/login/**").permitAll()
//         .antMatchers("/XYZ/**").hasAuthority("SCOPE_XYZ")
         .anyRequest().authenticated()
         .and().logout().logoutSuccessUrl("/").permitAll().and().csrf()
         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
         .and()
         .oauth2Login();
     }
}
