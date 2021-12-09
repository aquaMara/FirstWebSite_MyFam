package org.aquam.springsecurity2.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    @Column(nullable = false)
    private String feedback;

    public Feedback() {}

    public Feedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
