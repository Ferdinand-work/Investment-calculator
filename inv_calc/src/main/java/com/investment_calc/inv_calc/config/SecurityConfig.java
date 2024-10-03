package com.investment_calc.inv_calc.config;

import com.investment_calc.inv_calc.services.UserDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private UserDetailsServices userDetailsServices;

    @Autowired
    private JwtFilter jwtFilter;

    @PostConstruct
    public void init() {
        System.out.println("SecurityConfig initialized");  // Log to indicate the class is loaded
    }

    // Security filter chain configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Configuring security filter chain...");

        SecurityFilterChain filterChain = http
                .cors(Customizer.withDefaults())  // Enable CORS support in Spring Security
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF
                .authorizeHttpRequests(request -> {
                    System.out.println("Configuring authorized endpoints...");
                    request.requestMatchers("/api/usr/login", "/api/usr/signup").permitAll();  // Permit login and signup endpoints
                    request.anyRequest().authenticated();  // All other endpoints require authentication
                })
                .sessionManagement(session -> {
                    System.out.println("Setting session creation policy to STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Stateless sessions for JWT
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
                .build();

        System.out.println("Security filter chain configuration complete.");
        return filterChain;
    }

    // Authentication provider configuration
    @Bean
    public AuthenticationProvider authenticationProvider() {
        System.out.println("Configuring authentication provider...");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));  // Set bcrypt password encoder
        provider.setUserDetailsService(userDetailsServices);  // Set custom user details service

        System.out.println("Authentication provider configuration complete.");
        return provider;
    }

    // Authentication manager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        System.out.println("Configuring authentication manager...");
        AuthenticationManager manager = config.getAuthenticationManager();
        System.out.println("Authentication manager configuration complete.");
        return manager;
    }

    // CORS configuration to allow requests from frontend
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Configuring CORS mappings...");

        registry.addMapping("/**")  // Allow all endpoints
                .allowedOrigins("http://localhost:3000")  // Allow frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow these HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow credentials (cookies, authorization headers, etc.)

        System.out.println("CORS configuration complete.");
    }
}
