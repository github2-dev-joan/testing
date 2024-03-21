package com.luv2code.WebScraperDB1.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        //Do not forget antMatchers() mvcMatchers() and regexMatchers() have been depricated
        httpSecurity
                .cors(withDefaults())// configure CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection

        .authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/checkRevNo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getActualTable").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getHistoryTableByDate").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tregjet/getHistoryTableByDate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tregjet/authenticate").permitAll()
                        .anyRequest().authenticated()
        )
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) //unable to import???
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        //.formLogin(withDefaults()); // Use form-based authentication


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
        //initialized as a local variable
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        //define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, enabled from users where username=?"
        );
        //define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username, authority from authorities where username=?"
        );
        return jdbcUserDetailsManager;
    }

    /*
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/tregjet/**", config);
        return new CorsFilter(source);
    }

    */


    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }





}

