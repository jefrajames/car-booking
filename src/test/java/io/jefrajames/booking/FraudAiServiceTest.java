package io.jefrajames.booking;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class FraudAiServiceTest {

    @Inject
    FraudAiService fraudAiService;

    @Test
    void testFraudJamesBond() {
        final String userMessage = "fraud James Bond";
        final FraudResponse response = fraudAiService.detectFraudForCustomer("James", "Bond");
        System.out.println(userMessage + " => " + response);
        assertFalse(response.fraudDetected());
    }

    @Test
    void testFraudEmilioLargo() {
        final String userMessage = "fraud Emilio Largo";
        final FraudResponse response = fraudAiService.detectFraudForCustomer("Emilio", "Largo");
        System.out.println(userMessage + " => " + response);
        assertTrue(response.fraudDetected());
    }
    
}
