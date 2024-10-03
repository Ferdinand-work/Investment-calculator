package com.investment_calc.inv_calc.config;

import com.investment_calc.inv_calc.services.JWTService;
import com.investment_calc.inv_calc.services.UserDetailsServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("Auth -> "+authHeader);

        System.out.println("Called!!");

        String token = null;
        String email = null;

        System.out.println("Taken data -> "+authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Inside 2");
            token = authHeader.substring(7);
            email = jwtService.extractUserName(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("Inside 3");

            UserDetails userDetails = context.getBean(UserDetailsServices.class).loadUserByUsername(email);
            if (jwtService.validateToken(token, userDetails)) {

                System.out.println("inside 5");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
