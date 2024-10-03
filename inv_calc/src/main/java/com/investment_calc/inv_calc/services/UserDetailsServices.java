package com.investment_calc.inv_calc.services;

import com.investment_calc.inv_calc.model.User;
import com.investment_calc.inv_calc.model.UserPrincipal;
import com.investment_calc.inv_calc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserDetailsServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }
        UserDetails userDetails = new UserPrincipal(user);
        return userDetails;
    }
}
