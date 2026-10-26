package com.stevenrios.todo;

import static org.junit.Assert.*;
import org.junit.Test;

public class LogicTest {
    @Test public void hashIsSaltedAndVerifies() {
        String a = PasswordHasher.hash("secret123"), b = PasswordHasher.hash("secret123");
        assertNotEquals("same password must give different salted hashes", a, b);
        assertFalse(a.contains("secret123"));
        assertTrue(PasswordHasher.verify("secret123", a));
        assertFalse(PasswordHasher.verify("Secret123", a));
        assertFalse(PasswordHasher.verify("x", "garbage"));
        assertFalse(PasswordHasher.verify("x", null));
    }
    @Test public void usernameRules() {
        assertNull(Validator.username("steven_r"));
        assertNotNull(Validator.username(""));
        assertNotNull(Validator.username("ab"));
        assertNotNull(Validator.username("bad name"));
        assertNotNull(Validator.username("x'; DROP TABLE users;--"));
    }
    @Test public void passwordRules() {
        assertNull(Validator.password("abcdef", "abcdef"));
        assertEquals("Password must be at least 6 characters", Validator.password("abc", "abc"));
        assertEquals("Passwords do not match", Validator.password("abcdef", "abcdeg"));
    }
    @Test public void taskRules() {
        assertNull(Validator.task("Buy milk"));
        assertNotNull(Validator.task("   "));
        assertNotNull(Validator.task(new String(new char[201]).replace('\0', 'a')));
    }
}
