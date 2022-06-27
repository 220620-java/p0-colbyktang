package com.revature.courseapp.course;

// Junit imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CourseTest {
    @Test
    public void testGetCapacity() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        int courseCapacity = course.getCapacity();
        assertEquals(20, courseCapacity);
    }

    @Test
    public void testGetCourseName() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        String courseName = course.getCourseName();
        assertEquals("Introduction to Comp Sci", courseName);
    }

    @Test
    public void testGetId() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        int id = course.getId();
        assertEquals(101, id);
    }

    @Test
    public void testGetSemester() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        String semester = course.getSemester();
        assertEquals("FALL 2022", semester);
    }

    @Test
    public void testSetCapacity() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        course.setCapacity(30);
        assertEquals(30, course.getCapacity());
    }

    @Test
    public void testSetCourseName() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        course.setCourseName("Introduction to Programming");
        assertEquals("Introduction to Programming", course.getCourseName());
    }

    @Test
    public void testSetSemester() {
        Course course = new Course(101, "Introduction to Comp Sci", "FALL 2022", 20);
        course.setSemester("SPRING 2022");
        assertEquals("SPRING 2022", course.getSemester());
    }
}
