package co.edu.ustavillavicencio.impostor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreatedResponse {
    private String roomCode;
    private UUID hostPlayerId;
}
