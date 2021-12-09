package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultNoteService {

    private final DefaultNoteRepository defaultNoteRepository;

    @Autowired
    public DefaultNoteService(DefaultNoteRepository defaultNoteRepository) {
        this.defaultNoteRepository = defaultNoteRepository;
    }

    public void deleteNoteById(Long id) {
        Note note = defaultNoteRepository.getById(id);
        defaultNoteRepository.delete(note);
    }
    public void completeNoteById(Long id) {
        Note note = defaultNoteRepository.getById(id);
        note.setCompleted(true);
        defaultNoteRepository.save(note);
    }

}
