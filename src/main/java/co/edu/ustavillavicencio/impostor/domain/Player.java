package co.edu.ustavillavicencio.impostor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean alive = true;

    public Player(UUID roomId, String nickname) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.alive = true;
    }
}
