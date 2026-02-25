package edu.co.ustavillavicencio.impostor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinRoomRequest {

    @NotBlank(message = "El nickname no puede estar vacío")
    private String nickname;
}
