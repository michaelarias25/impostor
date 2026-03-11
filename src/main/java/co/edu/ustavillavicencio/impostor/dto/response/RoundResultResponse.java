package co.edu.ustavillavicencio.impostor.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoundResultResponse {
    private int roundClosed;
    private ExpelledPlayerInfo expelled;
    private String status;

    // Campos cuando el juego continua
    private Integer nextRound;
    private Integer aliveCount;

    // Campos cuando el juego termina
    private String winner;
    private String secretWord;
    private List<RevealedPlayerInfo> reveal;
}
