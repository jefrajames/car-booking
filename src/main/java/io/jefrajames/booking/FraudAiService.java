package io.jefrajames.booking;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = BookingService.class)
public interface FraudAiService {

        @SystemMessage("""
                        You are a car booking fraud detection AI for Miles of Smiles. 
                        You have to detect customer fraud in bookings.
                        """)
        @UserMessage("""
                        Your task is to detect whether a fraud was committed for the customer {{name}} {{surname}}.

                        To detect a fraud, perform the following actions:
                        1 - Retrieve all bookings for the customer {{name}} {{surname}}.
                        2 - Determine if there is an overlap between several bookings.
                        3 - If there is an overlap, a fraud is detected.
                        4 - If a fraud is detected, return the fraud status and the bookings that overlap.

                        A booking overlap (and hence a fraud) occurs when there are several bookings for a given date.
                        For instance:
                        -there is no overlap if a given customer has the following bookings:
                                - Booking number 345-678 with the period from 2024-03-25 to 2024-03-31.
                                - Booking number 234-567 with the period from 2024-03-21 to 2024-03-23.
                        -there is an overlap if a given customer has the following bookings:
                                - Booking number 456-789 with the period from 2024-03-21 to 2024-03-31.
                                - Booking number 567-890 with the period from 2024-03-22 to 2024-03-27.

                        Answer with the following information in a valid JSON document:
                        - the customer-name key set to {name}
                        - the customer-surname key set to {surname}
                        - the fraud-detected key set to 'true' or 'false'
                        - In case of fraud, the explanation of the fraud in the fraud-explanation key.
                        You must respond in a valid JSON format.

                        You must not wrap JSON response in backticks, markdown, or in any other way, but return it as plain text.
                        """)
        @Timeout(unit = ChronoUnit.MINUTES, value = 5)
        @Retry(maxRetries = 2)
        @Fallback(fallbackMethod = "fraudFallback")
        FraudResponse detectFraudForCustomer(String name, String surname);

        default FraudResponse fraudFallback(String name, String surname) {
                return new FraudResponse(name, surname, false,
                                "Sorry, I am not able to detect fraud for customer " + name + " " + surname
                                                + " at the moment. Please try again later.");
        }

}
