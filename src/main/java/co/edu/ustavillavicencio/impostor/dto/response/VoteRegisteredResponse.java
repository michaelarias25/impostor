package co.edu.ustavillavicencio.impostor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRegisteredResponse {
    private String message;
    private int round;
}
