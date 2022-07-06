package com.revature.courseapp.data;

import com.revature.courseapp.models.FacultyMember;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;

public interface UserDAO extends DataAccessObject<User> {

    /** Inserts a user into the table users with password and salt.
     *  @param user The object to be added to the data source
     *  @param pass
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    public User create(User user, String pass);

    /** Inserts a Student into the table users with password and salt and into the students table.
     *  @param student The object to be added to the data source
     *  @param pass
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    public Student create (Student student, String pass);

    /** Inserts a Faculty Member into the table users with password and salt and the table faculty.
     *  @param facultyMember The object to be added to the data source
     *  @param pass
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    public FacultyMember create (FacultyMember facultyMember, String pass);

    /** Retrieves the user from the database using a username
     * @param username
     * @return User
     */
    public User findByUsername(String username);

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

    /** Validates a password using a username and password. Password will be encrypted and salted to match the given password.
     * @param username
     * @param password
     * @return boolean
     */
    public boolean validatePassword (String username, String password);
}
