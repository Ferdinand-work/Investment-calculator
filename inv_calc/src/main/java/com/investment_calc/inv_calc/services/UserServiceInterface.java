package com.investment_calc.inv_calc.services;

import com.investment_calc.inv_calc.dto.LoginRequest;
import com.investment_calc.inv_calc.dto.SignUpRequest;
import com.investment_calc.inv_calc.model.Investment;
import com.investment_calc.inv_calc.model.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserServiceInterface {
    public void signup(SignUpRequest signUpRequest) throws Exception;
    public String login(LoginRequest loginRequest);

    public User getUser(HttpServletRequest request);
}
