package edu.sumdu.tss.elephant.helper;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRoleTest {

    private static void check(long[] values, UserRole role) {
        assertEquals(values[0], role.maxConnections());
        assertEquals(values[1], role.maxDB());
        assertEquals(values[2], role.maxStorage());
        assertEquals(values[3], role.maxBackupsPerDB());
        assertEquals(values[4], role.maxScriptsPerDB());
    }

    @Test
    void anyone() {
        UserRole role = UserRole.ANYONE;
        long[] expectedValues = new long[]{0, 0, 0, 0, 0};
        check(expectedValues, role);
    }

    @Test
    void unchecked() {
        UserRole role = UserRole.UNCHEKED;
        long[] expectedValues = new long[]{0, 0, 0, 0, 0};
        check(expectedValues, role);
    }

    @Test
    void basic() {
        UserRole role = UserRole.BASIC_USER;
        long[] expectedValues = new long[]{5, 2, 20 * FileUtils.ONE_MB, 1, 2};
        check(expectedValues, role);
    }

    @Test
    void promoted() {
        UserRole role = UserRole.PROMOTED_USER;
        long[] expectedValues = new long[]{5, 3, 50 * FileUtils.ONE_MB, 5, 5};
        check(expectedValues, role);
    }

    @Test
    void admin() {
        UserRole role = UserRole.ADMIN;
        long[] expectedValues = new long[]{100, 100, FileUtils.ONE_GB, 50, 50};
        check(expectedValues, role);
        assertEquals(4, role.getValue());
    }

    @Test
    void getter() {
        assertEquals(UserRole.ANYONE, UserRole.byValue(0));
        assertEquals(UserRole.UNCHEKED, UserRole.byValue(1));
        assertEquals(UserRole.BASIC_USER, UserRole.byValue(2));
        assertEquals(UserRole.PROMOTED_USER, UserRole.byValue(3));
        assertEquals(UserRole.ADMIN, UserRole.byValue(4));
        assertThrows(RuntimeException.class, () -> UserRole.byValue(5));
    }
}