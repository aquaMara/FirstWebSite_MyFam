package org.aquam.springsecurity2.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;
    private String notename;
    private String noteText;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name="roomId")
    private Room room;

    public Note() {
    }

    public Note(String notename, String noteText) {
        this.notename = notename;
        this.noteText = noteText;
        this.completed = false;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getNotename() {
        return notename;
    }

    public void setNotename(String notename) {
        this.notename = notename;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setNoteStatus(boolean status) {
        this.completed = status;
    }

    public boolean getNoteStatus() {
        return completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }



    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", notename='" + notename + '\'' +
                ", noteText='" + noteText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(noteId, note.noteId) && Objects.equals(notename, note.notename) && Objects.equals(noteText, note.noteText) && Objects.equals(room, note.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, notename, noteText, room);
    }
}

