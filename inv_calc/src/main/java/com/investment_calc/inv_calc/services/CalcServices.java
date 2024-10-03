package com.investment_calc.inv_calc.services;

import com.investment_calc.inv_calc.dto.InvDto;
import com.investment_calc.inv_calc.execptions.InvalidAccessException;
import com.investment_calc.inv_calc.model.Investment;
import com.investment_calc.inv_calc.repository.CalcRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CalcServices implements CalcServiceInterface {
    @Autowired
    private CalcRepository calcRepository;

    @Autowired
    private JWTService jwtService;

    private Investment createInvestmentObject(InvDto invDto,String type,String email)
    {
        Investment investment = null;
        try {
            Float amount  = invDto.getAmount();
            Float exp_return_rate = invDto.getExp_return_rate();
            int years = invDto.getYears();
            Float inflation_rate = invDto.getInflation_rate();
            Float step_up = invDto.getStep_up();
            BigDecimal inflated_earnings = null;
            BigDecimal nominal_earnings = null;

            if(type.equals("sip"))
            {
                int months = years*12;
                Float monthlyRate = exp_return_rate/1200;
                Double ne = amount*((Math.pow(1+monthlyRate,months)-1)/monthlyRate)*(1+monthlyRate);
                Double ie = ne/Math.pow(1+inflation_rate/100,years);
                nominal_earnings = new BigDecimal(ne);
                nominal_earnings = nominal_earnings.setScale(2,BigDecimal.ROUND_HALF_UP);
                inflated_earnings = new BigDecimal(ie);
                inflated_earnings = inflated_earnings.setScale(2,BigDecimal.ROUND_HALF_UP);
            } else if (type.equals("lumpsum")) {
                Double ne = amount*Math.pow(1+exp_return_rate/100,years);
                Double ie = ne/Math.pow(1+inflation_rate/100,years);
                nominal_earnings = new BigDecimal(ne);
                nominal_earnings = nominal_earnings.setScale(2,BigDecimal.ROUND_HALF_UP);
                inflated_earnings = new BigDecimal(ie);
                inflated_earnings = inflated_earnings.setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if(invDto.getId() == null)
            {
                investment = Investment.builder()
                        .type(type)
                        .amount(amount)
                        .email(email)
                        .exp_return_rate(exp_return_rate)
                        .years(years)
                        .inflation_rate(inflation_rate)
                        .step_up(step_up)
                        .inflated_earnings(inflated_earnings)
                        .nominal_earnings(nominal_earnings)
                        .build();
                calcRepository.save(investment);
            }
            else
            {
                Optional<Investment> investmentOptional = calcRepository.findById(invDto.getId());
                if(!email.equals(investmentOptional.get().getEmail()))
                {
                    return null;
                }
                investment = Investment.builder()
                        .id(invDto.getId())
                        .type(type)
                        .amount(amount)
                        .email(email)
                        .exp_return_rate(exp_return_rate)
                        .years(years)
                        .inflation_rate(inflation_rate)
                        .step_up(step_up)
                        .inflated_earnings(inflated_earnings)
                        .nominal_earnings(nominal_earnings)
                        .build();
                calcRepository.save(investment);
            }

        } catch (Exception e) {
            //
        }
        return investment;
    }
    
    public Investment addInvestment(InvDto invDto,String email)
    {
        if(invDto.getType().equals("sip")) return createInvestmentObject(invDto,"sip",email);
        else return createInvestmentObject(invDto,"lumpsum",email);

    }
    public List<Investment> getSips(String email)
    {
        List<Investment> sipList = new ArrayList<>();
        try {
            sipList = calcRepository.findByTypeAndEmail("sip",email);
        } catch (Exception e) {
            //
        }
        return sipList;
    }

    public List<Investment> getLumpsums(String email)
    {
        List<Investment> lumpsumList = new ArrayList<>();
        try {
            lumpsumList = calcRepository.findByTypeAndEmail("lumpsum",email);
        } catch (Exception e) {
            //
        }
        return lumpsumList;
    }

    public String deleteInvestment(String id, String email) {
        try {
            // Fetch the investment by id
            Optional<Investment> investment = calcRepository.findById(id);

            // Check if the investment exists
            if (investment.isEmpty()) {
                throw new NoSuchElementException("Investment with id " + id + " not found.");
            }

            // Check if the current user is the owner of the investment
            if (!email.equals(investment.get().getEmail())) {
                throw new InvalidAccessException("Invalid access. You do not own this investment.");
            }

            // If all checks pass, delete the investment
            calcRepository.deleteById(id);
            return "Investment with id " + id + " successfully deleted.";

        } catch (NoSuchElementException e) {
            return "Error: " + e.getMessage();  // Return a meaningful error message if the investment is not found
        } catch (InvalidAccessException e) {
            return "Error: " + e.getMessage();  // Return a meaningful error message for invalid access
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();  // Catch any other unexpected exceptions
        }
    }

    public Optional<Investment> getOneInvestment(String id, String email) {
        try {
            // Fetch the investment by id
            Optional<Investment> investment = calcRepository.findById(id);

            // Check if the investment exists
            if (investment.isEmpty()) {
                throw new NoSuchElementException("Investment with id " + id + " not found.");
            }

            // Check if the current user is the owner of the investment
            if (!email.equals(investment.get().getEmail())) {
                throw new InvalidAccessException("Invalid access. You do not own this investment.");
            }

            // Return the found investment if no exceptions are thrown
            return investment;

        } catch (NoSuchElementException | InvalidAccessException e) {
            // Log the error (you can use a logger here)
            System.err.println("Error: " + e.getMessage());

            // Optionally, return an empty Optional or throw an exception for higher-level handling
            return Optional.empty();
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return Optional.empty();
        }
    }



}
