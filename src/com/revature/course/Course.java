package src.com.revature.course;

import src.com.revature.user.Student;

public class Course {
    protected String courseId;
    protected String courseName;
    protected String semester;
    protected int capacity;
    // protected List<Student> enrolledStudents = new ArrayList<Student>();

    public Course (String id, String name, String semester, int capacity) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
    }

    // public boolean IsFull () {
    //     return enrolledStudents.size() >= capacity;
    // }
}
