package com.revature.courseapp.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.courseapp.models.Course;
import com.revature.courseapp.utils.List;

public class DatabaseCoursesTest {

    static ConnectionUtil db;

    @BeforeAll
    public static void OpenDatabase () {
        // Try to use AWS DB
        db = ConnectionUtil.getConnectionUtil("aws_db.json");

        // If AWS does not work use local database
        if (db.getCurrentConnection() == null) db = ConnectionUtil.getConnectionUtil();
    }

    @AfterAll
    public static void afterAll () {
        db.closeConnection();
    }

    @Test
    void testGetCourse() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30);
        assertEquals(course, CoursePostgres.getCourse(db.getCurrentConnection(), 101));
    }

    @Test
    void testWithdrawCourse() {

    }

    @Test
    void testDoesCourseExist() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30);
        Course course2 = new Course (301, "Introduction to Electricity", "FALL 2022", 30);
        Course course3 = new Course (201, "Introduction to Technology", "FALL 2022", 30);
        assertTrue(CoursePostgres.doesCourseExist(db.getCurrentConnection(), course.getId()));
        assertTrue(CoursePostgres.doesCourseExist(db.getCurrentConnection(), course2.getId()));
        assertTrue(CoursePostgres.doesCourseExist(db.getCurrentConnection(), course3.getId()));
    }

    @Test
    void testEnrollCourse() {
        
    }

    @Test
    void testGetAllEnrolledCourses() {
        List<Course> courses101 = CoursePostgres.getAllEnrolledCourses(db.getCurrentConnection(), 101);
        assertEquals (101, courses101.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses101.get(0).getCourseName());

        List<Course> courses102 = CoursePostgres.getAllEnrolledCourses(db.getCurrentConnection(), 102);
        assertEquals (101, courses102.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses102.get(0).getCourseName());

        List<Course> courses201 = CoursePostgres.getAllEnrolledCourses(db.getCurrentConnection(), 201);
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
