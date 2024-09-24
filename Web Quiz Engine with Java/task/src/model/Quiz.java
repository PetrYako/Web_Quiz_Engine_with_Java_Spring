package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String text;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> options;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<Integer> answer;
    private String author;

    public Quiz(String title, String text, List<String> options, List<Integer> answer, String author) {
        this.title = title;
        this.text = text;
        this.options = new ArrayList<>(options);
        this.answer = Objects.requireNonNullElseGet(answer, List::of);
        this.author = author;
    }
}
