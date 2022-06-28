package com.revature.courseapp;

import java.util.Scanner;
import java.io.Console;

import com.revature.courseapp.utils.Encryption;

public class Login {
    
    
    /** 
     * @return boolean
     */
    public static boolean userLogin () {
        Scanner scanner = App.getScanner();
        String username = "";
        do {
            System.out.print ("Enter your username: ");
            username = scanner.nextLine();
        } while (username == "");

        Console console = System.console();

        String password;
        do {
            password = new String (console.readPassword("Enter your password: "));
        } while (password == "");

        // Check user from the database
        boolean isPasswordValid = App.getUserDB().validatePassword(username, password);
        if (isPasswordValid) {
            App.setLoggedUser(App.getUserDB().getUserFromDB(username));
        }
        else {
            System.out.println("Password is not correct!");
        }
        return isPasswordValid;
    }
}
