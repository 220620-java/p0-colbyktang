package com.revature.courseapp.services;

import com.revature.courseapp.exceptions.UserAlreadyExistsException;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;

public interface UserService {
    public User userLogin(String username, String password);
    public Student registerStudent() throws UserAlreadyExistsException;
    public void studentViewAvailableClasses();
    public void studentEnrollClass();
    public void studentViewRegisteredClasses();
    public void studentCancelClass();
    public void studentViewProfile (Student student);
    public Student studentChangeProfile (Student student);
    public void facultyViewClasses();
    public void facultyAddNewClass ();
    public void facultyChangeClassDetails ();
    public void facultyRemoveClass ();
}