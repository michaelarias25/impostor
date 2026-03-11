package co.edu.ustavillavicencio.impostor.service;

import co.edu.ustavillavicencio.impostor.dto.request.CastVoteRequest;
import co.edu.ustavillavicencio.impostor.dto.response.*;

import java.util.UUID;

public interface GameService {
    GameStartedResponse startGame(String code, UUID hostPlayerId);
    MyRoleResponse getMyRole(String code, UUID playerId);
    VoteRegisteredResponse castVote(String code, UUID voterId, CastVoteRequest request);
    RoundResultResponse closeRound(String code, UUID hostPlayerId);
}
