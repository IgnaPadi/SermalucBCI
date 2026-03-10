package com.bci.user.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Clase común para validar valores con expresiones regulares
 */
@Component
public class RegularExpressionValidator {

    private static String passwordRegex;

    @Value("${app.regex.password}")
    public void setPasswordRegex(String regex) {
        RegularExpressionValidator.passwordRegex = regex;
    }

    public static boolean isEmail(String email){
        return Pattern.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", email);
    }

    public static boolean isPassword(String password){
        return Pattern.matches(passwordRegex, password);

    }
}
