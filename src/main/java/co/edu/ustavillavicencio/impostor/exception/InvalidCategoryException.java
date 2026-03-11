package co.edu.ustavillavicencio.impostor.exception;

public class InvalidCategoryException extends RuntimeException {
    public InvalidCategoryException(String category) {
        super("La categoria no es valida: " + category);
    }
}
