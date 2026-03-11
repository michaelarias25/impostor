package co.edu.ustavillavicencio.impostor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID roomId;

    @Column(nullable = false)
    private int roundNumber;

    @Column(nullable = false)
    private UUID voterId;

    @Column(nullable = false)
    private UUID votedId;

    public Vote(UUID roomId, int roundNumber, UUID voterId, UUID votedId) {
        this.roomId = roomId;
        this.roundNumber = roundNumber;
        this.voterId = voterId;
        this.votedId = votedId;
    }
}
