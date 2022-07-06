package com.revature.courseapp.utils;

// Junit imports
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class ValidationTest {
    @Test
    public void isUsernameValidTrue () {
        assertTrue (Validation.isUsernameValid("ctang"));
        assertTrue (Validation.isUsernameValid("CTANG"));
        assertTrue (Validation.isUsernameValid("Ctang"));
        assertTrue (Validation.isUsernameValid("ctangadd"));
        assertTrue (Validation.isUsernameValid("longusernameispossible"));

        // Test 30 character limit
        assertTrue(Validation.isUsernameValid("t23456789012345678901234567890"));
    }

    @Test
    public void isLongUsernameInvalid () {
        assertFalse(Validation.isUsernameValid("reallylongusernamethatshouldberejected"));

        // Test 31 character limit
        assertFalse(Validation.isUsernameValid("t234567890123456789012345678901"));
    }

    @Test
    public void usernameStartsWithNonCharacter () {
        assertTrue (Validation.isUsernameValid("ctango"));
        assertFalse (Validation.isUsernameValid("1ctango"));
        assertFalse (Validation.isUsernameValid("#ctango"));
        assertFalse (Validation.isUsernameValid("!@#@)#(@)($%"));
    }

    @Test
    public void usernameHasNonWordCharacter () {
        assertFalse (Validation.isUsernameValid("t@#@)#(@)($%"));
        assertFalse (Validation.isUsernameValid("ctango#"));
        assertFalse (Validation.isUsernameValid("c$tango$"));
    }

    @Test
    public void isNameValid () {
        assertTrue (Validation.isNameValid("Tang"));
        assertTrue (Validation.isNameValid("Colby"));

        // Should fail on space
        assertFalse (Validation.isNameValid("Colby Tang"));
    }

    // Passwords must be at least 4 characters or at most 32 characters.
    @Test
    public void isPasswordValid () {
        assertTrue (Validation.isPasswordValid("pass"));
        assertTrue (Validation.isPasswordValid("password"));
        assertTrue (Validation.isPasswordValid("reallylongpassword12345"));
    }

    @Test
    public void isPasswordInvalid () {
        assertFalse (Validation.isPasswordValid("a"));
        assertFalse (Validation.isPasswordValid(""));
        assertFalse (Validation.isPasswordValid(" "));
    }

    @Test
    public void isEmailValid () {
        assertTrue (Validation.isEmailValid("ctang@email.com"));
        assertTrue (Validation.isEmailValid("ctangs_dsdsdsdssd2@email.com"));
        assertTrue (Validation.isEmailValid("ct@em.com"));
    }

    @Test
    public void isEmailInvalid () {
        assertFalse (Validation.isEmailValid("ctang@.com"));
        assertFalse (Validation.isEmailValid("ctangs_dsdsdsdssd2.com"));
        assertFalse (Validation.isEmailValid("c"));
        assertFalse (Validation.isEmailValid(""));
    }
}
