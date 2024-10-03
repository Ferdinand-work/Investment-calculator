package com.investment_calc.inv_calc.services;


import com.investment_calc.inv_calc.dto.LoginRequest;
import com.investment_calc.inv_calc.dto.SignUpRequest;
import com.investment_calc.inv_calc.model.Investment;
import com.investment_calc.inv_calc.model.User;
import com.investment_calc.inv_calc.model.UserPrincipal;
import com.investment_calc.inv_calc.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServices implements UserServiceInterface {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    AuthenticationManager authmanager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void signup(SignUpRequest signUpRequest) throws Exception {
        String email = signUpRequest.email();
        User existingUser = userRepository.findByEmail(email);
        if(existingUser != null)
        {
            throw new Exception("User with email " + email + " already exists");
        }
        User user = User.builder()
                    .email(email)
                    .userName(signUpRequest.username())
                    .password(encoder.encode(signUpRequest.password()))
                    .build();
        userRepository.save(user);
    }

    private String getEmail(String authHeader)
    {
        String token = null;
        String email = null;
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            email = jwtService.extractUserName(token);
        }

        System.out.println("Inside get email -> "+email);

        return email;
    }

    public String login(LoginRequest loginRequest)
    {
        Authentication authentication =
                authmanager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        if(authentication.isAuthenticated()) return jwtService.generateToken(loginRequest.getEmail());
        return "fail";
    }

    @Override
    public User getUser(HttpServletRequest request) {

        System.out.println("Insode serrv impl, request -> "+request);

        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        User user = userRepository.findByEmail(email);

        System.out.println("User -> "+user);

        user.setPassword(null);
        return user;
    }
}

