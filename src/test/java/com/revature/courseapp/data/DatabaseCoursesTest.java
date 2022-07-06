package com.revature.courseapp.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.revature.courseapp.models.Course;
import com.revature.courseapp.utils.List;

public class DatabaseCoursesTest {

    static CourseDAO courseDAO;
    static ConnectionUtil db;


    @BeforeAll
    public static void OpenDatabase () {
        db = ConnectionUtil.getConnectionUtil("local_db.json");
        courseDAO = new CoursePostgres();
    }

    @Test
    void testGetCourse() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30, true);
        assertEquals(course, courseDAO.findById(101));
    }

    @Test
    void testWithdrawCourse() {

    }

    @Test
    void testDoesCourseExist() {
        Course course = new Course (101, "Introduction to Computer Science", "FALL 2022", 30, true);
        Course course2 = new Course (301, "Introduction to Electricity", "FALL 2022", 30, true);
        Course course3 = new Course (201, "Introduction to Technology", "FALL 2022", 30, true);
        assertTrue(courseDAO.doesCourseExist(course.getId()));
        assertTrue(courseDAO.doesCourseExist(course2.getId()));
        assertTrue(courseDAO.doesCourseExist(course3.getId()));
    }

    @Test
    void testEnrollCourse() {
        
    }

    @Test
    void testGetAllEnrolledCourses() {
        List<Course> courses101 = courseDAO.getAllEnrolledCourses(101);
        assertEquals (101, courses101.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses101.get(0).getCourseName());

        List<Course> courses102 = courseDAO.getAllEnrolledCourses(102);
        assertEquals (101, courses102.get(0).getId());
        assertEquals ("Introduction to Computer Science", courses102.get(0).getCourseName());

        List<Course> courses201 = courseDAO.getAllEnrolledCourses(201);
        assertEquals (null, courses201.get(0));

    }

    @Test
    void testInsertCourse() {
        Course course = new Course (101, "TEST COURSE", "SPRING 2023", 5, true);
        // insertCourse (course);
    }

    @Test
    void testRemoveCourse() {

    }
}
