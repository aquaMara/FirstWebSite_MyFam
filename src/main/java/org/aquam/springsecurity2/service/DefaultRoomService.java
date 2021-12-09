package org.aquam.springsecurity2.service;


import org.aquam.springsecurity2.models.Home;
import org.aquam.springsecurity2.models.Note;
import org.aquam.springsecurity2.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRoomService {

    private static final String ROOM_NOT_FOUND = "Room with roomname %s not found";
    private final DefaultNoteRepository defaultNoteRepository;
    private final DefaultRoomRepository defaultRoomRepository;

    @Autowired
    public DefaultRoomService(DefaultNoteRepository defaultNoteRepository, DefaultRoomRepository defaultRoomRepository) {
        this.defaultNoteRepository = defaultNoteRepository;
        this.defaultRoomRepository = defaultRoomRepository;
    }

    public Room loadRoomByRoomname(Home home, String roomname) throws UsernameNotFoundException {
        List<Room> allHomeRooms = home.getRoomsForHome();
        for (Room containerValue : allHomeRooms) {
            if (containerValue.getRoomname().equals(roomname))
                return containerValue;
        }
        return null;
    }


    public void addNoteForRoom(Note note, Room room) {

        defaultNoteRepository.save(note);

        room.addNotesForRoom(note);

        defaultRoomRepository.save(room);

    }

    public void deleteNoteFromRoom(Note note, Room room) {

        defaultNoteRepository.delete(note);

        defaultRoomRepository.save(room);

    }

    public void deleteRoomForHome(Room thatRoom) {

        defaultRoomRepository.delete(thatRoom);

    }

    public void updateRoomForHome(Room oldRoom, String updatedRoomname) {

        oldRoom.setRoomname(updatedRoomname);
        defaultRoomRepository.save(oldRoom);

    }

}
