package com.luv2code.WebScraperDB1.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        //Do not forget antMatchers() mvcMatchers() and regexMatchers() have been depricated
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getHistoryTableByDate").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getHistoryTableByDate").hasRole("ADMIN")
                        .anyRequest().authenticated()
        )

        .formLogin(Customizer.withDefaults()) // Use form-based authentication
        .csrf(csrf -> csrf.disable()); // Disable CSRF protection

        //httpBasic() and csrf() methods where depricated
        //httpSecurity.httpBasic()
        //httpSecurity.csrf().disable()

        return httpSecurity.build();
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");

    }

    @Bean
    public UserDetailsManager userDetailsManager(){
        return new JdbcUserDetailsManager(dataSource);
    }


}
