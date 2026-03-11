package co.edu.ustavillavicencio.impostor.domain;

import co.edu.ustavillavicencio.impostor.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private UUID playerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String word;

    public Assignment(UUID roomId, UUID playerId, Role role, String word) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.role = role;
        this.word = word;
    }
}
