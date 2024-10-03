package com.investment_calc.inv_calc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value="investments")
@Data
@Builder
public class Investment {
    @Id
    private String id;
    String email;
    String type;
    Float amount;
    Float exp_return_rate;
    int years;
    Float inflation_rate;
    Float step_up;

    BigDecimal inflated_earnings;
    BigDecimal nominal_earnings;
}
