package controller;

import dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import service.QuizService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping
    public ResponseEntity<QuizPageableDto> getQuizzes(@RequestParam(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(quizService.getQuizzes(page.orElse(0)));
    }

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@RequestBody @Valid QuizRequest request, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(quizService.createQuiz(request, user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(quizService.getQuiz(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<QuizCompletedPageableDto> getCompletedQuizzes(@RequestParam(required = false) Optional<Integer> page, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(quizService.getCompletedQuizzes(page.orElse(0), user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Integer id, @AuthenticationPrincipal UserDetails user) {
        try {
            quizService.deleteQuiz(id, user.getUsername());
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).build();
        }
    }


    @PostMapping("/{id}/solve")
    public ResponseEntity<QuizAnswerDto> solveQuiz(@PathVariable Integer id, @RequestBody QuizSolveRequest request, @AuthenticationPrincipal UserDetails user) {
        try {
            return ResponseEntity.ok(quizService.solveQuiz(id, request.getAnswer(), user.getUsername()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
