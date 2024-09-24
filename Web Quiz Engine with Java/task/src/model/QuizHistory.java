package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_history")
public class QuizHistory {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer quizId;
    private LocalDateTime completedAt;
    private String solvedBy;

    public QuizHistory(Integer quizId, LocalDateTime completedAt, String solvedBy) {
        this.quizId = quizId;
        this.completedAt = completedAt;
        this.solvedBy = solvedBy;
    }
}
