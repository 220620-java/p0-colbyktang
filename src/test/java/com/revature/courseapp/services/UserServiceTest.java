package com.revature.courseapp.services;

// import test methods
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.courseapp.data.CourseDAO;
import com.revature.courseapp.data.UserDAO;
import com.revature.courseapp.exceptions.UserAlreadyExistsException;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;

@ExtendWith (MockitoExtension.class)
public class UserServiceTest {
    
    // Object that we're testing
    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    private UserDAO userDao;

    @Mock
    private CourseDAO courseDao;

    private Student mockUser = new Student("Jack", "Test", "jacktest", "jacktest@email.com", "Comp Sci", 3.0f);

    private String password = "pass";

    private String mockUsername = "jacktest";

    /** Tests if login process can be successful.
     * 
     */
    @Test
    public void loginSuccessfully () {
        Mockito.when(userDao.validatePassword(mockUsername, password)).thenReturn(true);
        Mockito.when(userDao.findByUsername(mockUsername)).thenReturn(mockUser);

		// call method
		User returnedUser = userService.userLogin(mockUsername, password);

		// assertion
		assertNotNull(returnedUser);
    }

    @Test
    public void registerSuccessfully () {
        Mockito.when(userDao.create(mockUser, password)).thenReturn(mockUser);

        Student returnedUser = null;
		// call method
        try {
		    returnedUser = userService.registerStudent();
        }
        catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }

		// assertion
		assertNotNull(returnedUser);
    }
}
