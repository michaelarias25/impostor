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
public class RoomStatusResponse {

    private RoomStatus status;
    private String category;
    private int currentRound;
    private List<PlayerInfo> players;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerInfo {
        private UUID id;
        private String nickname;
        private boolean alive;
    }
}
