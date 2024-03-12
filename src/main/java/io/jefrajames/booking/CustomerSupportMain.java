package io.jefrajames.booking;

import java.util.Scanner;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

@QuarkusMain
@ActivateRequestContext
public class CustomerSupportMain {

    public static void main(String... args) {
        Quarkus.run(CustomerSupportApplication.class, args);
        Quarkus.waitForExit();
        System.exit(0);
    }

    public static class CustomerSupportApplication implements QuarkusApplication {

        @Inject
        ChatAiService chatAiService;

        @Inject
        FraudAiService fraudAiService;

        @Override
        @ActivateRequestContext
        public int run(String... args) throws Exception {

            System.out.println("Welcome to the Customer Support Chat");
            System.out.println("Enter 'bye' to exit");
            System.out.println("Enter 'fraud <name> <surname>' to check for fraud");
            System.out.println("Enter any other text to chat with the support");
            
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("User message: ");
                    String userMessage = scanner.nextLine();

                    if (userMessage.equalsIgnoreCase("bye")) {
                        break;
                    }

                    if (userMessage.startsWith("fraud")) {
                        String[] parts = userMessage.split(" ");
                        if ( parts.length != 3 ) {
                            Log.error("Invalid request, usage: fraud <name> <surname>");
                            continue;
                        }
                        String name = parts[1];
                        String surname = parts[2];
                        long start = System.currentTimeMillis();
                        FraudResponse msg = fraudAiService.detectFraudForCustomer(name, surname);
                        Log.info("Fraud detection response: " + msg);
                        Log.info("Fraud detection response time: " + (System.currentTimeMillis() - start) + " ms");
                        continue;
                    } else {
                        long start = System.currentTimeMillis();
                        String msg = chatAiService.chat(userMessage);
                        Log.info("Chat response: " + msg);
                        Log.info("Chat response time: " + (System.currentTimeMillis() - start) + " ms");
                    }
                }
            }

            return 0;
        }

    }
}