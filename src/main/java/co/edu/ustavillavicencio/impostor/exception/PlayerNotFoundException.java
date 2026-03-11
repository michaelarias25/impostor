package co.edu.ustavillavicencio.impostor.exception;

import java.util.UUID;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(UUID id) {
        super("No se encontro el jugador con id: " + id);
    }
}
