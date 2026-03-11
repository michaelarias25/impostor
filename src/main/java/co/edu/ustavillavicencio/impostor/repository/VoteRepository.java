package co.edu.ustavillavicencio.impostor.repository;

import co.edu.ustavillavicencio.impostor.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByVoterIdAndRoomIdAndRoundNumber(UUID voterId, UUID roomId, int roundNumber);
    List<Vote> findByRoomIdAndRoundNumber(UUID roomId, int roundNumber);
}
