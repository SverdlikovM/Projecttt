package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class BackupExceptionTest {

    @Test
    void emptyConstructor() {
        Exception exception = new Exception();
        String message = StringUtils.randomAlphaString(8);
        BackupException backupException =
                assertThrows(BackupException.class,
                        () -> {
                            throw new BackupException(message, exception);
                        }
                );

        assertEquals(message, backupException.getMessage());
        assertEquals(exception, backupException.getCause());
    }

    @Test
    void exceptionConstructor() {
        Exception exception = new Exception(StringUtils.randomAlphaString(8));
        BackupException backupException =
                assertThrows(BackupException.class,
                        () -> {
                            throw new BackupException(exception);
                        }
                );

        assertTrue(backupException.getMessage().contains(exception.getMessage()));
        assertEquals(exception, backupException.getCause());
    }

    @Test
    void messageConstructor() {
        String message = StringUtils.randomAlphaString(8);
        BackupException backupException =
                assertThrows(BackupException.class,
                        () -> {
                            throw new BackupException(message);
                        }
                );

        assertEquals(message, backupException.getMessage());
    }

    @Test
    void getCode() {
        final int exceptionCode = 500;
        BackupException exception = new BackupException(StringUtils.randomAlphaString(8));
        assertEquals(exceptionCode, exception.getCode());
    }
}