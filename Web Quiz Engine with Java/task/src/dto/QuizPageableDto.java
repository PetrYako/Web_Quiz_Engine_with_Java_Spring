package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizPageableDto {
    private int totalPages;
    private int currentPage;
    private int totalElements;
    private int size;
    private int numberOfElements;
    private int number;
    private boolean first;
    private boolean last;
    private boolean empty;
    private List<QuizDto> content;
}
