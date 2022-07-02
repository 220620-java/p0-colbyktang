package com.revature.courseapp.utils;

import java.util.regex.*;

public class Validation {

    public static boolean isUsernameValid (String username) {
        // Regex string to validate username.
        // Allow usernames from 5 characters to 30.
        // Usernames should start with a letter.
        String regex = "^[A-Za-z]\\w{4,29}$";

        Pattern p = Pattern.compile(regex);
  
        // If the username is empty, return false
        if (username == null) {
            return false;
        }
  
        Matcher m = p.matcher(username);
  
        // True if username is valid.
        return m.matches();
    }
}