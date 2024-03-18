package shop.mtcoding.blog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardJPARepository extends JpaRepository<Board, Integer> {

    @Query("select b from Board b join fetch b.user u where b.id = :id")
    Board findByIdJoinUser(@Param("id") int id);

}
