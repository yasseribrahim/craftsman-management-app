package com.craftsman.management.app;

import org.junit.Test;

import static org.junit.Assert.*;

import com.craftsman.management.app.models.User;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void generate_admin_user() {
        User user = new User();
        user.setId("");
    }
}