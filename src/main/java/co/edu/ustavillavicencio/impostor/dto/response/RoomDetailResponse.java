package co.edu.ustavillavicencio.impostor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse {
    private String status;
    private String category;
    private int currentRound;
    private List<PlayerInfoResponse> players;
}
