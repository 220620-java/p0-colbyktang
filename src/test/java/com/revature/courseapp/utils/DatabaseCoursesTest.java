package com.revature.courseapp.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.courseapp.course.Course;

public class DatabaseCoursesTest {

    static DatabaseCourses db;

    @BeforeAll
    public static void OpenDatabase () {
        db = new DatabaseCourses();
    }

    @AfterAll
    public static void afterAll () {
        db.closeConnection();
    }

    @Test
    void testGetCourse() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30);
        assertEquals(course, db.getCourse(101));
    }

    @Test
    void testWithdrawCourse() {

    }

    @Test
    void testDoesCourseExist() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30);
        Course course2 = new Course (301, "Introduction to Electricity", "FALL 2022", 30);
        Course course3 = new Course (201, "Introduction to Technology", "FALL 2022", 30);
        assertTrue(db.doesCourseExist(course));
        assertTrue(db.doesCourseExist(course2));
        assertTrue(db.doesCourseExist(course3));
    }

    @Test
    void testEnrollCourse() {
        
    }

    @Test
    void testGetAllEnrolledCourses() {
        List<Course> courses101 = db.getAllEnrolledCourses(101);
        assertEquals (101, courses101.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses101.get(0).getCourseName());

        List<Course> courses102 = db.getAllEnrolledCourses(102);
        assertEquals (101, courses102.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses102.get(0).getCourseName());

        List<Course> courses201 = db.getAllEnrolledCourses(201);
        assertEquals (null, courses201.get(0));

    }

    @Test
    void testInsertCourse() {
        Course course = new Course (101, "TEST COURSE", "SPRING 2023", 5);
        // insertCourse (course);
    }

    @Test
    void testRemoveCourse() {

    }
}
