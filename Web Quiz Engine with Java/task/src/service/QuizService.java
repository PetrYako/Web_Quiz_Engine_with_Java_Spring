package service;

import dto.*;
import model.Quiz;
import model.QuizHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.QuizHistoryRepository;
import repository.QuizRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizHistoryRepository quizHistoryRepository;

    public QuizDto getQuiz(Integer id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        return mapToQuizDto(quiz);
    }

    public QuizAnswerDto solveQuiz(Integer id, List<Integer> answer, String username) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        boolean isCorrect = answer.equals(quiz.getAnswer());
        if (isCorrect) {
            quizHistoryRepository.save(new QuizHistory(quiz.getId(), LocalDateTime.now(), username.toLowerCase()));
            return new QuizAnswerDto(true, "Correct!");
        } else {
            return new QuizAnswerDto(false, "Wrong!");
        }
    }

    public void deleteQuiz(Integer id, String username) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        if (!quiz.getAuthor().equals(username.toLowerCase())) {
            throw new IllegalArgumentException("You can't delete this quiz");
        }
        quizRepository.delete(quiz);
    }

    public QuizDto createQuiz(QuizRequest request, String username) {
        Quiz quiz = new Quiz(request.getTitle(), request.getText(), request.getOptions(), request.getAnswer(), username.toLowerCase());
        quiz = quizRepository.save(quiz);
        return mapToQuizDto(quiz);
    }

    public QuizPageableDto getQuizzes(Integer page) {
        Page<Quiz> quizPage = quizRepository.findAll(PageRequest.of(page, 10, Sort.by("id").ascending()));
        return new QuizPageableDto(
                quizPage.getTotalPages(),
                page,
                (int) quizPage.getTotalElements(),
                10,
                quizPage.getNumberOfElements(),
                quizPage.getNumber(),
                quizPage.isFirst(),
                quizPage.isLast(),
                quizPage.isEmpty(),
                quizPage.getContent().stream().map(this::mapToQuizDto).toList()
        );
    }

    public QuizCompletedPageableDto getCompletedQuizzes(Integer page, String username) {
        Page<QuizHistory> historyPage = quizHistoryRepository.findAllBySolvedBy(username.toLowerCase(), PageRequest.of(page, 10, Sort.by("completedAt").descending()));
        return new QuizCompletedPageableDto(
                historyPage.getTotalPages(),
                page,
                (int) historyPage.getTotalElements(),
                10,
                historyPage.getNumberOfElements(),
                historyPage.getNumber(),
                historyPage.isFirst(),
                historyPage.isLast(),
                historyPage.isEmpty(),
                historyPage.getContent().stream().map(this::mapToQuizCompletedDto).toList()
        );
    }

    private QuizDto mapToQuizDto(Quiz quiz) {
        return new QuizDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getText(),
                quiz.getOptions()
        );
    }

    private QuizCompletedDto mapToQuizCompletedDto(QuizHistory quizHistory) {
        return new QuizCompletedDto(
                quizHistory.getQuizId(),
                quizHistory.getCompletedAt()
        );
    }
}
