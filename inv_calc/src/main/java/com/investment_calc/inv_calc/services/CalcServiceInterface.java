package com.investment_calc.inv_calc.services;

import com.investment_calc.inv_calc.dto.InvDto;
import com.investment_calc.inv_calc.model.Investment;

import java.util.List;
import java.util.Optional;

public interface CalcServiceInterface {
    public Investment addInvestment(InvDto invDto,String email);
    public List<Investment> getSips(String email);
    public List<Investment> getLumpsums(String email);
    public String deleteInvestment(String id,String email);
    public Optional<Investment> getOneInvestment(String id,String email);

}
