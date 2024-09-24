package repository;

import model.QuizHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizHistoryRepository extends PagingAndSortingRepository<QuizHistory, Integer> {
    Page<QuizHistory> findAllBySolvedBy(String username, Pageable pageable);
}
