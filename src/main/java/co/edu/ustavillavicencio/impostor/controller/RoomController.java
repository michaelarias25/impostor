package co.edu.ustavillavicencio.impostor.controller;

import co.edu.ustavillavicencio.impostor.dto.request.CreateRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.request.JoinRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.response.PlayerJoinedResponse;
import co.edu.ustavillavicencio.impostor.dto.response.RoomCreatedResponse;
import co.edu.ustavillavicencio.impostor.dto.response.RoomDetailResponse;
import co.edu.ustavillavicencio.impostor.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomCreatedResponse createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @PostMapping("/{code}/players")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerJoinedResponse joinRoom(
            @PathVariable String code,
            @Valid @RequestBody JoinRoomRequest request) {
        return roomService.joinRoom(code, request);
    }

    @GetMapping("/{code}")
    public RoomDetailResponse getRoomDetail(@PathVariable String code) {
        return roomService.getRoomByCode(code);
    }
}
