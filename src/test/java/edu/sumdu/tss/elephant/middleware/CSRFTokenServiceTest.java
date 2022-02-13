package edu.sumdu.tss.elephant.middleware;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSRFTokenServiceTest {

    @Test
    void generateToken() {
        final String sessionID = "test";
        String token = CSRFTokenService.generateToken(sessionID);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(CSRFTokenService.validateToken(token, sessionID));
    }
}