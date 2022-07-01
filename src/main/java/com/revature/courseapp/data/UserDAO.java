package com.revature.courseapp.data;

import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;
import com.revature.courseapp.utils.List;

public interface UserDAO extends DataAccessObject<User> {

    /** Inserts a user into the table users with password and salt.
     *  @param User The object to be added to the data source
     *  @return The object that was added or null if the object was unable to be added
     */
    public User create(User user, String pass, byte[] salt);

    /** Checks the users table to see if user exists using both user id and username.
     * @param userid
     * @param username
     * @return boolean
     */
    public boolean doesUserExist (int user_id, String username);

    /** Checks the users table to see if user exists using a username.
     *  @param selectSQLQuery
     *  @return boolean
     */
    public boolean doesUserExist (String username);

    /** Checks the users table to see if user exists using the id.
     * @param userid
     * @return boolean
     */
    public boolean doesUserExist (int user_id);

    /** Get a list of all enrolled students of a particular course.
     * @param course_id 
     * @return List<Student>
     */
    public List<Student> getAllEnrolledStudents (int course_id);

    /** Validates a password using a username and password. Password will be encrypted and salted to match the given password.
     * @param username
     * @param password
     * @return boolean
     */
    public boolean validatePassword (String username, String password);
}
