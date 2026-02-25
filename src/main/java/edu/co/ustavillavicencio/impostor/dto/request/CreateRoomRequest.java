package edu.co.ustavillavicencio.impostor.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank(message = "El nickname del host no puede estar vacío")
    private String hostNickname;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String category;

    @NotNull(message = "La cantidad de impostores no puede ser nula")
    @Min(value = 1, message = "Debe haber al menos 1 impostor")
    private Integer impostorCount;
}
