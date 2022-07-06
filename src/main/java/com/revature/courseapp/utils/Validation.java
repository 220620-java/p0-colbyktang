package com.revature.courseapp.utils;

import java.util.regex.*;

/**
 * Offers user input validation using regex.
 * @author Colby Tang
 * @version 1.0
 */
public class Validation {

    /** Validate names.
     * @param name
     * @return boolean
     */
    public static boolean isNameValid (String name) {
        // Regex string to validate first or last name.
        String regex = "^[A-Za-z]\\w{1,29}$";

        return regexMatch (regex, name);
    }
    
    /** Allow usernames from 5 characters to 30.
     *  Usernames should start with a letter.
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
    
    /** Email must follow email conventions.
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid (String email) {
        // Regex string to validate email.
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return regexMatch (regex, email);
    }

    /** Majors must have zero or one space in between each word.
     * @param major
     * @return boolean
     */
    public static boolean isMajorValid (String major) {
        // Regex string to validate major.
        String regex = "^[A-Za-z\\s]+\\w{1,29}$";
        return regexMatch (regex, major);
    }

    /**
     * Checks id are all numbers up to 64 digits.
     * @param id
     * @return
     */
    public static boolean isIdValid (String id) {
        String regex = "^[0-9]{1,64}$";
        return regexMatch(regex, id);
    }

    /** Majors must have zero or one space in between each word.
     * @param major
     * @return boolean
     */
    public static boolean isCourseNameValid (String courseName) {
        // Regex string to validate major.
        String regex = "^[A-Za-z\\s]+\\w{1,99}$";
        return regexMatch (regex, courseName);
    }

    /**
     * Tests if capacity is a positive integer.
     * @param capacity
     * @return
     */
    public static boolean isCapacityValid (String capacity) {
        String regex = "^[0-9]{1,3}$";
        return regexMatch(regex, capacity);
    }

    /**
     * Menu selection must be a number between -999 and 999.
     * @param selection
     * @return
     */
    public static boolean isMenuSelectionValid (String selection) {
        String regex = "^-?[0-9-]{1,3}$";
        return regexMatch (regex, selection);
    }

    /**
     * Tests if boolean for isAvailable is valid.
     * @param isValid
     * @return
     */
    public static boolean isAvailableValid (String isValid) {
        String regex = "^[YyNn]$";
        return regexMatch (regex, isValid);
    }

    /**
     * Tests if semester is FALL ####, SPRING ####, or SUMMER ####.
     * @param semester
     * @return
     */
    public static boolean isSemesterValid (String semester) {
        String regex = "^\\b(FALL|SPRING|SUMMER)\\b\\s[12][0-9]{3}$";
        return regexMatch(regex, semester);
    }

    /* Helper method to compile regex and match the string.
     * @param regex
     * @param String
     */
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