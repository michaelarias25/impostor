package co.edu.ustavillavicencio.impostor.domain;

import co.edu.ustavillavicencio.impostor.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 6)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    private UUID hostPlayerId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int impostorCount;

    private int currentRound;

    private String secretWord;

    private String winner;

    public Room(String code, String category, int impostorCount) {
        this.code = code;
        this.category = category;
        this.impostorCount = impostorCount;
        this.status = RoomStatus.LOBBY;
        this.currentRound = 0;
    }
}
