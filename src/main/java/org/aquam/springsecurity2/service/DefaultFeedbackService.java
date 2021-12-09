package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultFeedbackService {

    private final DefaultFeedbackRepository defaultFeedbackRepository;

    @Autowired
    public DefaultFeedbackService(DefaultFeedbackRepository defaultFeedbackRepository) {
        this.defaultFeedbackRepository = defaultFeedbackRepository;
    }

    public void addFeedback(Feedback feedback) {
        defaultFeedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        List<Feedback> allFeedbacks = defaultFeedbackRepository.findAll();
        return  allFeedbacks;
    }
}
