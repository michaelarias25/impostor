package co.edu.ustavillavicencio.impostor.service;

import co.edu.ustavillavicencio.impostor.dto.request.CreateRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.request.JoinRoomRequest;
import co.edu.ustavillavicencio.impostor.dto.response.PlayerJoinedResponse;
import co.edu.ustavillavicencio.impostor.dto.response.RoomCreatedResponse;
import co.edu.ustavillavicencio.impostor.dto.response.RoomDetailResponse;

public interface RoomService {
    RoomCreatedResponse createRoom(CreateRoomRequest request);
    PlayerJoinedResponse joinRoom(String code, JoinRoomRequest request);
    RoomDetailResponse getRoomByCode(String code);
}
