package io.jefrajames.booking;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ChatAiServiceTest {

    @Inject
    ChatAiService chatAiService;

    @Test
    void testRole() {
        final String[] items = new String[] { "support", "car", "rental", "company", "customer" };
        final String userMessage = "What is your role? 1 line please";
        final String response = chatAiService.chat(userMessage);
        System.out.println(userMessage + " => " + response);
        for (String item : items) {
            assertTrue(response.toLowerCase().contains(item));
        }
    }

    @Test
    void testListOfCars() {
        final String[] items = new String[] { "aston martin", "bmw", "renault", "tesla", "toyota" };
        final String userMessage = "What is your list of cars? 1 line with comma separator, please.";
        final String response = chatAiService.chat(userMessage);
        System.out.println(userMessage + " => " + response);
        for (String item : items) {
            assertTrue(response.toLowerCase().contains(item));
        }
    }

    @Test
    void testCancelationPolicy() {
        final String[] items = new String[] { "up to 7 days", "less than 3 days" };
        final String userMessage = "What is your cancelation policy?";
        final String response = chatAiService.chat(userMessage);
        System.out.println(userMessage + " => " + response);
        for (String item : items) {
            assertTrue(response.toLowerCase().contains(item));
        }
    }

    @Test
    void testFleetSize() {
        final String[] items = new String[] { "1000", "cars" };
        final String userMessage = "What is your fleet size? Be short please.";
        final String response = chatAiService.chat(userMessage);
        System.out.println(userMessage + " => " + response);
        for (String item : items) {
            assertTrue(response.toLowerCase().contains(item));
        }
    }

    @Test
    void testHowManyElectricCars() {
        final String userMessage = "How many electric cars? Be short please.";
        final String regex = ".*10%.*|.*100.*";
        final String response = chatAiService.chat(userMessage);

        System.out.println(userMessage + " => " + response);
        assertTrue(response.matches(regex));
    }

    @Test
    void testJamesBondBookings() {
        final String userMessage = "My name is James Bond, please list my bookings.";
        final String[] items = new String[] { "345-678", "234-567", "123-456" };
        final String response = chatAiService.chat(userMessage);

        System.out.println(userMessage + " => " + response);
        for (String item : items) {
            assertTrue(response.toLowerCase().contains(item));
        }
    }

}
