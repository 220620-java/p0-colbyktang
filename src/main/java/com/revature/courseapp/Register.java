package com.revature.courseapp;

import java.io.Console;
import java.util.Scanner;

import com.revature.courseapp.user.User;
import com.revature.courseapp.user.Student;

public class Register {
    public static void RegisterUser () {
        Scanner scanner = new Scanner(System.in);
        System.out.print(
            "Registering as a new student..."
        );

        System.out.print(
            "Enter your first and last name: "
        );
        String name = scanner.nextLine();

        System.out.print(
            "Enter your username: "
        );
        String username = scanner.nextLine();

        System.out.print(
            "Enter your email: "
        );
        String email = scanner.nextLine();

        Console console = System.console();

        String password;
        String verifyPassword;
        do {
        password = new String (console.readPassword("Enter your password: "));
        verifyPassword = new String (console.readPassword("Enter your password again: "));

        if (!password.equals(verifyPassword)) {
            System.out.println("Passwords do not match");
        }
        } while (!password.equals(verifyPassword));

    }

    public static void RegisterStudent () {

    }
}
