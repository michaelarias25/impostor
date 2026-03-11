package co.edu.ustavillavicencio.impostor.controller;

import co.edu.ustavillavicencio.impostor.dto.request.CastVoteRequest;
import co.edu.ustavillavicencio.impostor.dto.response.*;
import co.edu.ustavillavicencio.impostor.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/{code}/start")
    public GameStartedResponse startGame(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {
        return gameService.startGame(code, hostPlayerId);
    }

    @GetMapping("/{code}/me")
    public MyRoleResponse getMyRole(
            @PathVariable String code,
            @RequestParam UUID playerId) {
        return gameService.getMyRole(code, playerId);
    }

    @PostMapping("/{code}/votes")
    public VoteRegisteredResponse castVote(
            @PathVariable String code,
            @RequestParam UUID voterId,
            @Valid @RequestBody CastVoteRequest request) {
        return gameService.castVote(code, voterId, request);
    }

    @PostMapping("/{code}/round/close")
    public RoundResultResponse closeRound(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {
        return gameService.closeRound(code, hostPlayerId);
    }
}
