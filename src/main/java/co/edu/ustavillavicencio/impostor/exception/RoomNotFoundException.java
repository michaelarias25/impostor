package co.edu.ustavillavicencio.impostor.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String code) {
        super("No se encontro la sala con codigo: " + code);
    }
}
