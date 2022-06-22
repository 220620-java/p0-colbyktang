package src.main.java.com.revature.courseapp.user;

public class FacultyMember extends User {
    public FacultyMember (String first, String last, String username, String password, String email) {
        super(first, last, username, password, email);
        userType = UserType.FACULTY;
    }

    private static final String ToStringTemplate = "FacultyID: = %1s, First Name = %2s, Last Name = %3s, Username = %4s, Email = %5s";

    public String toString () {
        return String.format (ToStringTemplate, this.id, this.firstName, this.lastName, this.username, this.email);
    }
}
