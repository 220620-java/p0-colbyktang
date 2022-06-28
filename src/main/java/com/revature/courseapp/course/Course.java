package com.revature.courseapp.course;

import com.revature.courseapp.user.Student;
import com.revature.courseapp.utils.*;

/**
*
* @author Colby Tang
* 
*/
public class Course {
    protected int courseId;
    protected String courseName;
    protected String semester;
    protected int capacity;
    protected List<Student> enrolledStudents = new LinkedList<Student>();

    
    /** 
     * @return int
     */
    public int getId () {
        return courseId;
    }

    
    /** 
     * @return String
     */
    public String getCourseName() {
        return courseName;
    }

    
    /** 
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    
    /** 
     * @return String
     */
    public String getSemester() {
        return semester;
    }

    
    /** 
     * @param semester
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    
    /** 
     * @return int
     */
    public int getCapacity() {
        return capacity;
    }

    
    /** 
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Course (int id, String name, String semester, int capacity) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
    }

    public Course (int id, String name, String semester, int capacity, List<Student> students) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
        this.enrolledStudents = students;
    }

    
    /** 
     * @return boolean
     */
    public boolean isFull () {
        return enrolledStudents.size() >= capacity;
    }

    
    /** 
     * @return int
     */
    public int getNumberOfStudents () {
        return enrolledStudents.size();
    }

    
    /** 
     * @return List<Student>
     */
    public List<Student> getEnrolledStudents () {
        return enrolledStudents;
    }

    
    /** 
     * @param students
     */
    public void setEnrolledStudents (List<Student> students) {
        enrolledStudents = students;
    }

    
    /** 
     * @param student
     * @return boolean
     */
    public boolean isStudentInCourse (Student student) {
        return enrolledStudents.getIndex (student) != -1;
    }

    
    /** 
     * @param student
     */
    public void registerCourse (Student student) {
        if (!isStudentInCourse(student)) {
            enrolledStudents.add(student);
        } 
    }

    private static final String ToStringTemplate = "CourseID: = %d, Course Name = %2s, Semester = %3s, Capacity = %d, EnrolledStudents = %s";

    
    /** 
     * @return String
     */
    public String toString () {
        return String.format (ToStringTemplate, this.getId(), this.getCourseName(), this.getSemester(), this.getCapacity(), this.getEnrolledStudents());
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
