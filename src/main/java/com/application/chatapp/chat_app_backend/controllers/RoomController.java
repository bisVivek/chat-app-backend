package com.application.chatapp.chat_app_backend.controllers;

import com.application.chatapp.chat_app_backend.entities.Message;
import com.application.chatapp.chat_app_backend.entities.Room;
import com.application.chatapp.chat_app_backend.repo.RoomRepository;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

// create room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){

        if(roomRepository.findByRoomId(roomId) != null){
            //matlb room already exit h
            return ResponseEntity.badRequest().body("Room Already existed");
        }

        // create new room

        Room room = new Room();
        room.setRoomId(roomId);

        Room savedRoom= roomRepository.save(room);
        return  ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    //get room: join
    @GetMapping ("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){

        Room room = roomRepository.findByRoomId(roomId);
        if(roomId == null){
            return ResponseEntity.badRequest().body("Room Not Found");
        }
        return ResponseEntity.ok(room);
    }


    //get messages of room

    @GetMapping("/{roomId}/message")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue="0", required=false)int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    )
    {

        Room room = roomRepository.findByRoomId(roomId);

        if(roomId == null){
            ResponseEntity.badRequest().build();
        }

        //get messages
        //pagination

        List<Message> messages = room.getMessages();
        return ResponseEntity.ok(messages);
    }

}
