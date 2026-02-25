package edu.co.ustavillavicencio.impostor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VoteRequest {

    @NotNull(message = "El jugador votado no puede ser nulo")
    private UUID votedId;
}
