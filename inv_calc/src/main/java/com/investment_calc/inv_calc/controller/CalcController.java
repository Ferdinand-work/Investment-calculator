package com.investment_calc.inv_calc.controller;

import com.investment_calc.inv_calc.dto.InvDto;
import com.investment_calc.inv_calc.model.Investment;
import com.investment_calc.inv_calc.services.CalcServiceInterface;
import com.investment_calc.inv_calc.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inv")
public class CalcController {
    @Autowired
    private CalcServiceInterface calcServiceInterface;

    @Autowired
    private JWTService jwtService;

    private String getEmail(String authHeader)
    {

        System.out.println(authHeader);
        String token = null;
        String email = null;
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            email = jwtService.extractUserName(token);
        }
        System.out.println(email);
        return email;
    }

    @PostMapping("/addInvestment/")
    public Investment addInvestment(@RequestBody InvDto invDto, HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        return calcServiceInterface.addInvestment(invDto,email);
    }

    @GetMapping("/sip/")
    public List<Investment> getSip(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        return calcServiceInterface.getSips(email);
    }

    @GetMapping("/lumpsum/")
    public List<Investment> getLumpsum(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        return calcServiceInterface.getLumpsums(email);
    }

    @DeleteMapping("/")
    public String deleteInvestment(@RequestParam String id,HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        return calcServiceInterface.deleteInvestment(id,email);
    }
    @GetMapping("/getOneInvestment/")
    public Optional<Investment> getOneInvestment(@RequestParam String id,HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String email = getEmail(authHeader);
        return calcServiceInterface.getOneInvestment(id,email);
    }
}
