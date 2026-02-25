package edu.co.ustavillavicencio.impostor.dto.response;

import edu.co.ustavillavicencio.impostor.domain.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseRoundResponse {

    private int roundClosed;
    private RoomStatus status;

    // Si el juego continúa
    private ExpelledInfo expelled;
    private Integer nextRound;
    private Integer aliveCount;

    // Si el juego terminó
    private String winner;
    private String secretWord;
    private List<PlayerReveal> reveal;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpelledInfo {
        private UUID id;
        private String nickname;
        private boolean wasImpostor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerReveal {
        private UUID playerId;
        private String nickname;
        private String role;
    }
}
