package com.investment_calc.inv_calc.controller;

import com.investment_calc.inv_calc.dto.LoginRequest;
import com.investment_calc.inv_calc.dto.SignUpRequest;
import com.investment_calc.inv_calc.model.Investment;
import com.investment_calc.inv_calc.model.User;
import com.investment_calc.inv_calc.services.JWTService;
import com.investment_calc.inv_calc.services.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usr")
public class UserController {

    @Autowired
    UserServiceInterface userServiceInterface;

    @Autowired
    JWTService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            userServiceInterface.signup(signUpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // Log the exception (you can use a logging framework)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Signup failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userServiceInterface.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/getUser")
    public User getUser(HttpServletRequest request)
    {
        System.out.println("Request _. "+request);
        return userServiceInterface.getUser(request);
    }
}
