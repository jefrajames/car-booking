package io.jefrajames.booking;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FraudResponse(String customerName, String customerSurname, boolean fraudDetected, List<String> bookingIds) {

    @JsonCreator
    public FraudResponse {
    }

}
