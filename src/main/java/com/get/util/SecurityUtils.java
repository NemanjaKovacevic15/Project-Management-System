package com.get.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//This method encrypts our input for password
@Component
public class SecurityUtils {

    public String encodePassword(String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));

        return passwordEncoder.encode(password);
    }
}
