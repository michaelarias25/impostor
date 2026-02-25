package edu.co.ustavillavicencio.impostor.controller;

import edu.co.ustavillavicencio.impostor.dto.request.CreateRoomRequest;
import edu.co.ustavillavicencio.impostor.dto.request.JoinRoomRequest;
import edu.co.ustavillavicencio.impostor.dto.request.VoteRequest;
import edu.co.ustavillavicencio.impostor.dto.response.*;
import edu.co.ustavillavicencio.impostor.service.GameService;
import edu.co.ustavillavicencio.impostor.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final GameService gameService;

    // ── RF-01: Crear sala ────────────────────────────────────────────────────
    // POST /api/rooms
    @PostMapping
    public ResponseEntity<CreateRoomResponse> createRoom(
            @Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(request));
    }

    // ── RF-02: Unirse a sala ─────────────────────────────────────────────────
    // POST /api/rooms/{code}/players
    @PostMapping("/{code}/players")
    public ResponseEntity<JoinRoomResponse> joinRoom(
            @PathVariable String code,
            @Valid @RequestBody JoinRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.joinRoom(code, request));
    }

    // ── RF-03: Consultar estado de sala ──────────────────────────────────────
    // GET /api/rooms/{code}
    @GetMapping("/{code}")
    public ResponseEntity<RoomStatusResponse> getRoomStatus(@PathVariable String code) {
        return ResponseEntity.ok(roomService.getRoomStatus(code));
    }

    // ── RF-04: Iniciar partida ───────────────────────────────────────────────
    // POST /api/rooms/{code}/start?hostPlayerId=...
    @PostMapping("/{code}/start")
    public ResponseEntity<Map<String, Object>> startGame(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {
        return ResponseEntity.ok(gameService.startGame(code, hostPlayerId));
    }

    // ── RF-05: Consultar rol/palabra personal ────────────────────────────────
    // GET /api/rooms/{code}/me?playerId=...
    @GetMapping("/{code}/me")
    public ResponseEntity<MyAssignmentResponse> getMyAssignment(
            @PathVariable String code,
            @RequestParam UUID playerId) {
        return ResponseEntity.ok(gameService.getMyAssignment(code, playerId));
    }

    // ── RF-06: Registrar voto ────────────────────────────────────────────────
    // POST /api/rooms/{code}/votes?voterId=...
    @PostMapping("/{code}/votes")
    public ResponseEntity<Map<String, Object>> castVote(
            @PathVariable String code,
            @RequestParam UUID voterId,
            @Valid @RequestBody VoteRequest request) {
        return ResponseEntity.ok(gameService.castVote(code, voterId, request));
    }

    // ── RF-07: Cerrar ronda ──────────────────────────────────────────────────
    // POST /api/rooms/{code}/round/close?hostPlayerId=...
    @PostMapping("/{code}/round/close")
    public ResponseEntity<CloseRoundResponse> closeRound(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {
        return ResponseEntity.ok(gameService.closeRound(code, hostPlayerId));
    }
}
