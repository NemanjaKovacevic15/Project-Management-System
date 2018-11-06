package com.get.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@ComponentScan({"com.get.configuration"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    final UserDetailsService userDetailsService;

    final AuthenticationSuccessHandler successHandler;

    //Strategy used to handle a successful user authentication.
    @Autowired
    public SecurityConfiguration(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
            final AuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    /*Allows for easily building in memory authentication, LDAP authentication, 
      JDBC based authentication, adding UserDetailsService, and adding AuthenticationProvider's.*/
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);//passwordEncoder(passwordEncoder());<-- this commented line
        //is preventing DB Admin tu put hashed password from DB and log in with hash in to the app.
        auth.authenticationProvider(authenticationProvider());
    }

    //Contains information about how to authenticate our users.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/list").access("hasRole('USER') or hasRole('ADMIN')")
                .antMatchers("/newuser/**", "/delete-user-*").access("hasRole('ADMIN')")
                .antMatchers("/edit-user-*").access("hasRole('ADMIN')")
                .antMatchers("/task/**").access("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
                .antMatchers("/project/**").access("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
                .antMatchers("/dev/**").access("hasRole('DEVELOPER') or hasRole('ADMIN')")
                .and().formLogin().loginPage("/login").successHandler(successHandler)
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
    }

    //Implementation of PasswordEncoder that uses the BCrypt strong hashing function for our password's.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //An AuthenticationProvider implementation that retrieves user details from a UserDetailsService.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
