package com.revature.courseapp.services;

import com.revature.courseapp.models.Student;

public interface UserService {
    public boolean userLogin(String username, String password);
    public void registerStudent(Student student, String password);
    public void studentViewAvailableClasses();
    public void studentEnrollClass();
    public void studentViewRegisteredClasses();
    public void studentCancelClass();
    public void facultyViewClasses();
    public void facultyAddNewClass ();
    public void facultyChangeClassDetails ();
    public void facultyRemoveClass ();
}