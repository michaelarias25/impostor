package co.edu.ustavillavicencio.impostor.service.impl;

import co.edu.ustavillavicencio.impostor.config.WordBankService;
import co.edu.ustavillavicencio.impostor.domain.Player;
import co.edu.ustavillavicencio.impostor.domain.Room;
import co.edu.ustavillavicencio.impostor.dto.request.CreateRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.request.JoinRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.response.*;
import co.edu.ustavillavicencio.impostor.enums.RoomStatus;
import co.edu.ustavillavicencio.impostor.exception.GameRuleException;
import co.edu.ustavillavicencio.impostor.exception.InvalidCategoryException;
import co.edu.ustavillavicencio.impostor.exception.RoomNotFoundException;
import co.edu.ustavillavicencio.impostor.repository.PlayerRepository;
import co.edu.ustavillavicencio.impostor.repository.RoomRepository;
import co.edu.ustavillavicencio.impostor.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final WordBankService wordBankService;

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;

    @Override
    public RoomCreatedResponse createRoom(CreateRoomRequest request) {
        // Validar que la categoria exista
        if (!wordBankService.categoryExists(request.getCategory())) {
            throw new InvalidCategoryException(request.getCategory());
        }

        // Generar codigo unico para la sala
        String code = generateUniqueCode();

        // Crear la sala
        Room room = new Room(code, request.getCategory().toUpperCase(), request.getImpostorCount());
        room = roomRepository.save(room);

        // Crear el jugador host
        Player host = new Player(room.getId(), request.getHostNickname());
        host = playerRepository.save(host);

        // Asignar el host a la sala
        room.setHostPlayerId(host.getId());
        roomRepository.save(room);

        return new RoomCreatedResponse(room.getCode(), host.getId());
    }

    @Override
    public PlayerJoinedResponse joinRoom(String code, JoinRoomRequest request) {
        Room room = fetchRoomByCode(code);

        // Solo se puede unir en LOBBY
        if (room.getStatus() != RoomStatus.LOBBY) {
            throw new GameRuleException("No se puede unir a una sala que ya esta en juego o finalizada");
        }

        Player newPlayer = new Player(room.getId(), request.getNickname());
        newPlayer = playerRepository.save(newPlayer);

        return new PlayerJoinedResponse(newPlayer.getId(), newPlayer.getNickname());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDetailResponse getRoomByCode(String code) {
        Room room = fetchRoomByCode(code);

        List<PlayerInfoResponse> playerList = playerRepository.findByRoomId(room.getId())
                .stream()
                .map(p -> new PlayerInfoResponse(p.getId(), p.getNickname(), p.isAlive()))
                .toList();

        return new RoomDetailResponse(
                room.getStatus().name(),
                room.getCategory(),
                room.getCurrentRound(),
                playerList
        );
    }

    private Room fetchRoomByCode(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() -> new RoomNotFoundException(code));
    }

    private String generateUniqueCode() {
        SecureRandom rng = new SecureRandom();
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARS.charAt(rng.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
        } while (roomRepository.findByCode(code).isPresent());
        return code;
    }
}
