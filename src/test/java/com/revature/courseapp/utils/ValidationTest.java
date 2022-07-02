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
}
