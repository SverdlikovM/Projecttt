package edu.sumdu.tss.elephant.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class UserTest {

    @Test
    void user() {
        User user = new User();
        Random random = new Random();

        String username = StringUtils.randomAlphaString(8);
        String password = StringUtils.randomAlphaString(8);

        user.setLogin(username);
        user.setPassword(password);

        Long roleN = (long) random.nextInt(5);
        UserRole role = UserRole.byValue(roleN);

        user.setRole(roleN);

        assertEquals(user.crypt(password), user.getPassword());
        assertEquals(roleN, user.getRole());
        assertEquals(role, user.role());

        String newPassword = StringUtils.randomAlphaString(8);
        user.setPassword(newPassword);
        assertNotEquals(user.crypt(password), user.getPassword());

        user.resetToken();
        String oldToken = user.getToken();

        user.resetToken();
        String newToken = user.getToken();

        assertNotEquals(oldToken, newToken);
    }
}