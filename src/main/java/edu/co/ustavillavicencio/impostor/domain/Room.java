package edu.co.ustavillavicencio.impostor.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 6)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Column(nullable = false)
    private UUID hostPlayerId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int impostorCount;

    @Column(nullable = false)
    private int currentRound;

    private String secretWord;

    private String winner; // "CIVILES" o "IMPOSTORES"
}
