package edu.co.ustavillavicencio.impostor.dto.response;

import edu.co.ustavillavicencio.impostor.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyAssignmentResponse {
    private Role role;
    private String word; // null si es IMPOSTOR
}
