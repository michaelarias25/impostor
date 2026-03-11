package co.edu.ustavillavicencio.impostor.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

    @NotBlank(message = "El nickname del host no puede estar vacio")
    private String hostNickname;

    @NotBlank(message = "La categoria no puede estar vacia")
    private String category;

    @Min(value = 1, message = "Debe haber al menos 1 impostor")
    private int impostorCount;
}
