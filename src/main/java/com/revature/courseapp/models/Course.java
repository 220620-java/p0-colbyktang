package com.revature.courseapp.models;

import com.revature.courseapp.utils.*;

/**
* A course is a college course that takes place during a semester (e.g. FALL 2022). 
* Students will be enrolled into the course restricted by a certain capacity.
* @author Colby Tang
* @version 1.0
*/
public class Course {
    protected int courseId;
    protected String courseName;
    protected String semester;
    protected int capacity;
    protected boolean isAvailable;
    protected List<Student> enrolledStudents;
    
    /** 
     * Course ID is manually set during instantiation.
     * @return int - The course id.
     * 
     */
    public int getId () {
        return courseId;
    }

    
    /** 
     * @return String - The name of the course.
     */
    public String getCourseName() {
        return courseName;
    }

    
    /** 
     * @param courseName - The name of the course.
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    
    /** 
     * @return String - The semester (e.g. FALL 2022).
     */
    public String getSemester() {
        return semester;
    }

    
    /** 
     * @param semester - The semester (e.g. FALL 2022).
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    
    /** 
     * @return int - The capacity of a class before it gets full.
     */
    public int getCapacity() {
        return capacity;
    }

    
    /** 
     * @param capacity - Sets the capacity of the class.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the course availability
     * @return Is the course available?
     */
    public boolean getIsAvailable () {
        return isAvailable;
    }

    /**
     * Sets the course availability.
     * @param isAvailable Is the course available?
     */
    public void setIsAvailable (boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Constructor for initializing a course with NO students or ID
     * @param name
     * @param semester
     * @param capacity
     * @param isAvailable
     */
    public Course (String name, String semester, int capacity, boolean isAvailable) {
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.enrolledStudents = new LinkedList<Student>();
    }

    /**
     * Constructor for initializing a course with NO students
     * @param id
     * @param name
     * @param semester
     * @param capacity
     * @param isAvailable
     */
    public Course (int id, String name, String semester, int capacity, boolean isAvailable) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.enrolledStudents = new LinkedList<Student>();
    }

    /**
     * Constructor for initializing a course with students
     * @param id
     * @param name
     * @param semester
     * @param capacity
     * @param isAvailable
     * @param students
     */
    public Course (int id, String name, String semester, int capacity, boolean isAvailable, List<Student> students) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.enrolledStudents = students;
    }

    
    /** 
     * Checks to see if the amount of enrolled students is at or over the capacity.
     * @return boolean
     */
    public boolean isFull () {
        return enrolledStudents.size() >= capacity;
    }

    
    /** 
     * Get the number of students enrolled in the class.
     * @return int
     */
    public int getNumberOfStudents () {
        return enrolledStudents.size();
    }

    
    /** 
     * Get a list of enrolled students in the course.
     * @return List<Student>
     */
    public List<Student> getEnrolledStudents () {
        return enrolledStudents;
    }

    
    /** 
     * Set a list of students enrolled in the course.
     * @param students - A list of students
     */
    public void setEnrolledStudents (List<Student> students) {
        enrolledStudents = students;
    }

    
    /** 
     * Checks to see if student is in the course
     * @param student - The student object to check for.
     * @return boolean
     */
    public boolean isStudentInCourse (Student student) {
        return enrolledStudents.getIndex (student) != -1;
    }

    
    /** 
     * Registers the student into the course. 
     * Checks if the student is already in the course or not.
     * @param student - The student object enrolling in the course.
     */
    public void registerCourse (Student student) {
        if (!isStudentInCourse(student)) {
            enrolledStudents.add(student);
        } 
    }

    /**
     * A string template for toString.
     */
    private static final String ToStringTemplate = "CourseID: %d, Course Name: %s, Semester: %s, Capacity: %d, Is Available: %s, EnrolledStudents: %s";

    
    /** 
     * @return String
     */
    public String toString () {
        return String.format (ToStringTemplate, this.getId(), this.getCourseName(), this.getSemester(), this.getCapacity(), String.valueOf(this.getIsAvailable()), this.getEnrolledStudents());
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + capacity;
        result = prime * result + courseId;
        result = prime * result + ((courseName == null) ? 0 : courseName.hashCode());
        result = prime * result + ((enrolledStudents == null) ? 0 : enrolledStudents.hashCode());
        result = prime * result + ((semester == null) ? 0 : semester.hashCode());
        return result;
    }

    
    /** 
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        if (capacity != other.capacity)
            return false;
        if (courseId != other.courseId)
            return false;
        if (courseName == null) {
            if (other.courseName != null)
                return false;
        } else if (!courseName.equals(other.courseName))
            return false;
        if (enrolledStudents == null) {
            if (other.enrolledStudents != null)
                return false;
        } else if (!enrolledStudents.equals(other.enrolledStudents))
            return false;
        if (semester == null) {
            if (other.semester != null)
                return false;
        } else if (!semester.equals(other.semester))
            return false;
        return true;
    }
}
