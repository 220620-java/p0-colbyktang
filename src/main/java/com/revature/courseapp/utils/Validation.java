package com.revature.courseapp.utils;

import java.util.regex.*;

public class Validation {
    
    /** Allow usernames from 5 characters to 30.
        Usernames should start with a letter.
     * @param username
     * @return boolean
     */
    public static boolean isUsernameValid (String username) {
        // Regex string to validate username.
        String regex = "^[A-Za-z]\\w{4,29}$";

        return regexMatch (regex, username);
    }

    
    /** Passwords must be at least 4 characters or at most 32 characters.
     * @param password
     * @return boolean
     */
    public static boolean isPasswordValid (String password) {
         // Regex string to validate password.
        String regex = ".{4,32}";
        return regexMatch (regex, password);
    }
    
    /** 
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid (String email) {
        // Regex string to validate email.
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return regexMatch (regex, email);
    }

    private static boolean regexMatch (String regex, String inputString) {
        Pattern p = Pattern.compile(regex);
  
        // If the username is empty, return false
        if (inputString == null) {
            return false;
        }
  
        Matcher m = p.matcher(inputString);
  
        // True if username is valid.
        return m.matches();
    }
}