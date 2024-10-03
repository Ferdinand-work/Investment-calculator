package com.investment_calc.inv_calc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "users")
public class User {
    String email;
    String password;
    String userName;
}
