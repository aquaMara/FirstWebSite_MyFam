package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DefaultFeedbackRepository  extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findFeedbackByFeedbackId(Long feedbackId);

}
