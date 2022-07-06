package com.revature.courseapp.utils;

// Junit imports
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LoggerTest {
    @Test
    public void createFileLogger () {
        assertTrue (Logger.createFile("test.log"));
    }

    @Test
    public void writeToFileLogger () {
        assertTrue(Logger.writeToFile("test.log", "WOW NEW CONTENT \n THIS IS A NEW LINE!"));
    }
}
