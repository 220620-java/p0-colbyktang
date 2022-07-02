package com.revature.courseapp.data;

import com.revature.courseapp.models.Course;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.utils.List;

public interface CourseDAO extends DataAccessObject<Course>{
    /** Removes a course from the catalog. It should also delete any students enrolled into the class.
     * @param course_id
     * @return boolean
     */
    public void delete(int course_id);

    /** Checks if course already exists in the table.
     * @param course
     * @return boolean
     */
    public boolean doesCourseExist (int course_id);
    /** Retrieve all the enrolled courses a student has using their student id.
     * @param student_id
     * @return List<Course>
     */
    public List<Course> getAllEnrolledCourses (int student_id);

    /** Get a list of all enrolled students of a particular course.
     * @param course_id 
     * @return List<Student>
     */
    public List<Student> getAllEnrolledStudents (int course_id);
    
    /** Have a student enroll in a course by adding it to the courses_users table.
     * @param course
     * @param student
     */
    public void enrollCourse (int course_id, int student_id);

    /** Withdraw from a course. Returns true if successful.
     * @param course
     * @param student
     * @return boolean
     */
    public boolean withdrawFromCourse (int course_id, int student_id);
}
