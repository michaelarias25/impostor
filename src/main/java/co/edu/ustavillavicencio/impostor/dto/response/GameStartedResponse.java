package co.edu.ustavillavicencio.impostor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStartedResponse {
    private String status;
    private int currentRound;
}
