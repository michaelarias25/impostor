package co.edu.ustavillavicencio.impostor.service.impl;

import co.edu.ustavillavicencio.impostor.config.WordBankService;
import co.edu.ustavillavicencio.impostor.domain.Assignment;
import co.edu.ustavillavicencio.impostor.domain.Player;
import co.edu.ustavillavicencio.impostor.domain.Room;
import co.edu.ustavillavicencio.impostor.domain.Vote;
import co.edu.ustavillavicencio.impostor.dto.request.CastVoteRequest;
import co.edu.ustavillavicencio.impostor.dto.response.*;
import co.edu.ustavillavicencio.impostor.enums.Role;
import co.edu.ustavillavicencio.impostor.enums.RoomStatus;
import co.edu.ustavillavicencio.impostor.exception.GameRuleException;
import co.edu.ustavillavicencio.impostor.exception.PlayerNotFoundException;
import co.edu.ustavillavicencio.impostor.exception.RoomNotFoundException;
import co.edu.ustavillavicencio.impostor.repository.AssignmentRepository;
import co.edu.ustavillavicencio.impostor.repository.PlayerRepository;
import co.edu.ustavillavicencio.impostor.repository.RoomRepository;
import co.edu.ustavillavicencio.impostor.repository.VoteRepository;
import co.edu.ustavillavicencio.impostor.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final AssignmentRepository assignmentRepository;
    private final VoteRepository voteRepository;
    private final WordBankService wordBankService;

    @Override
    public GameStartedResponse startGame(String code, UUID hostPlayerId) {
        Room room = fetchRoom(code);

        // Validar que sea el host
        validateIsHost(room, hostPlayerId);

        // Validar que la sala este en LOBBY
        if (room.getStatus() != RoomStatus.LOBBY) {
            throw new GameRuleException("La partida ya fue iniciada o ha finalizado");
        }

        // Obtener jugadores vivos de la sala
        List<Player> players = playerRepository.findByRoomIdAndAliveTrue(room.getId());

        // Validar minimo 3 jugadores
        if (players.size() < 3) {
            throw new GameRuleException("Se necesitan al menos 3 jugadores para iniciar la partida");
        }

        // Escoger palabra secreta para los civiles
        String secretWord = wordBankService.pickRandomWord(room.getCategory());
        room.setSecretWord(secretWord);

        // Escoger palabra distinta para el impostor (de otra categoria)
        String impostorWord = wordBankService.pickRandomWordFromDifferentCategory(room.getCategory());

        // Seleccionar impostores aleatoriamente
        List<Player> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled, ThreadLocalRandom.current());

        int impostorsToAssign = Math.min(room.getImpostorCount(), players.size() - 1);

        Set<UUID> impostorIds = new HashSet<>();
        for (int i = 0; i < impostorsToAssign; i++) {
            impostorIds.add(shuffled.get(i).getId());
        }

        // Crear asignaciones de rol y palabra
        for (Player player : players) {
            if (impostorIds.contains(player.getId())) {
                Assignment assignment = new Assignment(room.getId(), player.getId(), Role.IMPOSTOR, impostorWord);
                assignmentRepository.save(assignment);
            } else {
                Assignment assignment = new Assignment(room.getId(), player.getId(), Role.CIVIL, secretWord);
                assignmentRepository.save(assignment);
            }
        }

        // Actualizar estado de la sala
        room.setStatus(RoomStatus.IN_GAME);
        room.setCurrentRound(1);
        roomRepository.save(room);

        return new GameStartedResponse(room.getStatus().name(), room.getCurrentRound());
    }

    @Override
    @Transactional(readOnly = true)
    public MyRoleResponse getMyRole(String code, UUID playerId) {
        Room room = fetchRoom(code);
        Player player = fetchPlayer(playerId);

        // Verificar que el jugador pertenece a esta sala
        if (!player.getRoomId().equals(room.getId())) {
            throw new GameRuleException("El jugador no pertenece a esta sala");
        }

        Assignment assignment = assignmentRepository.findByRoomIdAndPlayerId(room.getId(), playerId)
                .orElseThrow(() -> new GameRuleException("Aun no se han asignado roles. La partida no ha iniciado"));

        return new MyRoleResponse(assignment.getRole().name(), assignment.getWord());
    }

    @Override
    public VoteRegisteredResponse castVote(String code, UUID voterId, CastVoteRequest request) {
        Room room = fetchRoom(code);

        // La sala debe estar IN_GAME
        if (room.getStatus() != RoomStatus.IN_GAME) {
            throw new GameRuleException("No se puede votar si la sala no esta en juego");
        }

        Player voter = fetchPlayer(voterId);
        Player voted = fetchPlayer(request.getVotedId());

        // Validar que el votante pertenece a la sala y esta vivo
        if (!voter.getRoomId().equals(room.getId())) {
            throw new GameRuleException("El votante no pertenece a esta sala");
        }
        if (!voter.isAlive()) {
            throw new GameRuleException("Un jugador eliminado no puede votar");
        }

        // Validar que el votado pertenece a la sala y esta vivo
        if (!voted.getRoomId().equals(room.getId())) {
            throw new GameRuleException("El jugador votado no pertenece a esta sala");
        }
        if (!voted.isAlive()) {
            throw new GameRuleException("No se puede votar por un jugador eliminado");
        }

        // Verificar que no haya votado ya en esta ronda
        if (voteRepository.existsByVoterIdAndRoomIdAndRoundNumber(voterId, room.getId(), room.getCurrentRound())) {
            throw new GameRuleException("Ya votaste en esta ronda");
        }

        // Registrar el voto
        Vote vote = new Vote(room.getId(), room.getCurrentRound(), voterId, request.getVotedId());
        voteRepository.save(vote);

        return new VoteRegisteredResponse("Voto registrado", room.getCurrentRound());
    }

    @Override
    public RoundResultResponse closeRound(String code, UUID hostPlayerId) {
        Room room = fetchRoom(code);

        // Validar que sea el host
        validateIsHost(room, hostPlayerId);

        // Validar que la sala este en juego
        if (room.getStatus() != RoomStatus.IN_GAME) {
            throw new GameRuleException("La sala no esta en juego");
        }

        int closedRound = room.getCurrentRound();

        // Obtener votos de la ronda actual
        List<Vote> roundVotes = voteRepository.findByRoomIdAndRoundNumber(room.getId(), closedRound);

        // Contar votos por cada jugador votado
        Map<UUID, Long> voteCounts = roundVotes.stream()
                .collect(Collectors.groupingBy(Vote::getVotedId, Collectors.counting()));

        // Determinar quien fue expulsado (Regla C: empate = nadie expulsado)
        Player expelledPlayer = determineExpelled(voteCounts);

        ExpelledPlayerInfo expelledInfo = null;
        boolean expelledWasImpostor = false;

        if (expelledPlayer != null) {
            // Marcar como eliminado
            expelledPlayer.setAlive(false);
            playerRepository.save(expelledPlayer);

            // Verificar si era impostor
            Assignment expelledAssignment = assignmentRepository
                    .findByRoomIdAndPlayerId(room.getId(), expelledPlayer.getId())
                    .orElse(null);

            expelledWasImpostor = expelledAssignment != null && expelledAssignment.getRole() == Role.IMPOSTOR;

            expelledInfo = new ExpelledPlayerInfo(
                    expelledPlayer.getId(),
                    expelledPlayer.getNickname(),
                    expelledWasImpostor
            );
        }

        // Evaluar condiciones de victoria
        List<Player> alivePlayers = playerRepository.findByRoomIdAndAliveTrue(room.getId());
        String gameResult = evaluateWinner(expelledWasImpostor, alivePlayers, room);

        if (gameResult != null) {
            // El juego termino
            room.setStatus(RoomStatus.FINISHED);
            room.setWinner(gameResult);
            roomRepository.save(room);

            // Construir la revelacion de roles
            List<RevealedPlayerInfo> revealList = buildRevealList(room.getId());

            return RoundResultResponse.builder()
                    .roundClosed(closedRound)
                    .expelled(expelledInfo)
                    .status(RoomStatus.FINISHED.name())
                    .winner(gameResult)
                    .secretWord(room.getSecretWord())
                    .reveal(revealList)
                    .build();
        } else {
            // El juego continua
            room.setCurrentRound(closedRound + 1);
            roomRepository.save(room);

            return RoundResultResponse.builder()
                    .roundClosed(closedRound)
                    .expelled(expelledInfo)
                    .status(RoomStatus.IN_GAME.name())
                    .nextRound(room.getCurrentRound())
                    .aliveCount(alivePlayers.size())
                    .build();
        }
    }

    // ========================
    // Metodos auxiliares
    // ========================

    /**
     * Regla C: si hay empate en votos, nadie es expulsado.
     * Si no hay votos, tampoco se expulsa a nadie.
     */
    private Player determineExpelled(Map<UUID, Long> voteCounts) {
        if (voteCounts.isEmpty()) {
            return null;
        }

        long maxVotes = voteCounts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        // Buscar quienes tienen la mayor cantidad de votos
        List<UUID> topVoted = voteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .toList();

        // Regla C: si hay empate (mas de uno con el maximo), nadie es expulsado
        if (topVoted.size() != 1) {
            return null;
        }

        return playerRepository.findById(topVoted.getFirst()).orElse(null);
    }

    /**
     * Evalua si hay un ganador:
     * - CIVILES ganan si el expulsado era impostor
     * - IMPOSTORES ganan si quedan 2 vivos y el impostor sigue vivo
     */
    private String evaluateWinner(boolean expelledWasImpostor, List<Player> alivePlayers, Room room) {
        // Victoria civiles: expulsaron al impostor
        if (expelledWasImpostor) {
            // Verificar si quedan mas impostores vivos
            boolean impostorAlive = alivePlayers.stream()
                    .anyMatch(p -> {
                        Assignment a = assignmentRepository.findByRoomIdAndPlayerId(room.getId(), p.getId()).orElse(null);
                        return a != null && a.getRole() == Role.IMPOSTOR;
                    });

            if (!impostorAlive) {
                return "CIVILES";
            }
        }

        // Victoria impostores: quedan 2 jugadores vivos y al menos uno es impostor
        if (alivePlayers.size() <= 2) {
            boolean impostorSurvives = alivePlayers.stream()
                    .anyMatch(p -> {
                        Assignment a = assignmentRepository.findByRoomIdAndPlayerId(room.getId(), p.getId()).orElse(null);
                        return a != null && a.getRole() == Role.IMPOSTOR;
                    });

            if (impostorSurvives) {
                return "IMPOSTORES";
            }
        }

        return null; // el juego continua
    }

    private List<RevealedPlayerInfo> buildRevealList(UUID roomId) {
        List<Assignment> allAssignments = assignmentRepository.findByRoomId(roomId);

        return allAssignments.stream()
                .map(a -> {
                    Player p = playerRepository.findById(a.getPlayerId()).orElse(null);
                    String nickname = (p != null) ? p.getNickname() : "Desconocido";
                    return new RevealedPlayerInfo(a.getPlayerId(), nickname, a.getRole().name());
                })
                .toList();
    }

    private Room fetchRoom(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() -> new RoomNotFoundException(code));
    }

    private Player fetchPlayer(UUID id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    private void validateIsHost(Room room, UUID playerId) {
        if (!room.getHostPlayerId().equals(playerId)) {
            throw new GameRuleException("Solo el host puede realizar esta accion");
        }
    }
}
