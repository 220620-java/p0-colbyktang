package com.revature.courseapp.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;  
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static String logFilename = "";
    private static String path = "src/main/resources/logs/";

    public static boolean createFile (String filename) {
        // if .log extension does not exist then add it
        if (!filename.endsWith(".log")) {
            filename += ".log";
        }

        try {
            logFilename = filename;
            File fileObj = new File ("src/main/resources/logs/" + logFilename);
            if (fileObj.createNewFile()) {
                System.out.println("New file created " + fileObj.getName());
                return true;
            }
            else {
                System.out.println("File already exists!");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeToFile (String filename, String content) {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/logs/" + filename);
            fileWriter.write (content);
            fileWriter.close();
            System.out.println("Writing to " + path + " " + filename + " Successful!");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean logMessage (String content) {
        try {
            String filename = "exceptions.log";
            FileWriter fileWriter = new FileWriter("src/main/resources/logs/" + filename);
            fileWriter.write (content);
            fileWriter.close();
            System.out.println("Writing to " + path + " " + filename + " Successful!");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a stack trace message with time stamp
     * @param elements
     * @return
     */
    public static boolean logMessage (StackTraceElement[] elements) {
        try {
            String filename = "exceptions.log";
            File fileObj = new File ("src/main/resources/logs/" + logFilename);
            if (!fileObj.exists()) {
                createFile(filename);
            }

            FileWriter fileWriter = new FileWriter("src/main/resources/logs/" + filename, true);

            Date date = Calendar.getInstance().getTime();  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
            String strDate = dateFormat.format(date);  

            fileWriter.write ("-------------" + strDate + "-------------\n");
            for (StackTraceElement element : elements) {
                fileWriter.write (element.toString() + '\n');
            }
            fileWriter.write ("-------------------------");
            fileWriter.close();
            System.out.println("Writing to " + path + " " + filename + " Successful!");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
