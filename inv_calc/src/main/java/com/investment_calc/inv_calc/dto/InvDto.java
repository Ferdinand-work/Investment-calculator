package com.investment_calc.inv_calc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvDto {
    String id;
    String email;
    String type;
    Float amount;
    Float exp_return_rate;
    int years;
    Float inflation_rate;
    Float step_up;
}
