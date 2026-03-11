package co.edu.ustavillavicencio.impostor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevealedPlayerInfo {
    private UUID playerId;
    private String nickname;
    private String role;
}
