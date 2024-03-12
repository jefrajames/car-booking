package io.jefrajames.booking;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FraudResponse(String customerName, String customerSurname, boolean fraudDetected, String description) {

    @JsonCreator
    public FraudResponse {
    }

}
