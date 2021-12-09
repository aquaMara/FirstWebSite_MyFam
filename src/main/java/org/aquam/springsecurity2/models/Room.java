package org.aquam.springsecurity2.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomname;

    @ManyToOne
    @JoinColumn(name = "homeId")
    private Home home;

    // @OneToMany(mappedBy = "room")
    @OneToMany
    @JoinColumn(name="roomId")
    private List<Note> notesForRoom = new ArrayList<>();

    public void addNotesForRoom(Note note) {
        notesForRoom.add(note);
    }

    public Room() {
    }

    public Room(String roomname) {
        this.roomname = roomname;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }


    public List<Note> getNotesForRoom() {
        return notesForRoom;
    }

    public void setNotesForRoom(List<Note> notesForRoom) {
        this.notesForRoom = notesForRoom;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomname='" + roomname;
    }
}

