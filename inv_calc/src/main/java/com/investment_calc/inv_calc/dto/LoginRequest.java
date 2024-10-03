package com.investment_calc.inv_calc.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
