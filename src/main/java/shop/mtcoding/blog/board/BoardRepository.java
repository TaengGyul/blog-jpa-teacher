package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    // 한방 쿼리 (h2 query, om -> dto)
    public BoardResponse.DetailDTO findDetail(Integer boardId, Integer userId) {
        String sql = """
                SELECT new shop.mtcoding.blog.board.BoardResponse$DetailDTO(
                    b.id,
                    b.title,
                    b.content,
                    b.isPublic,
                    CASE WHEN b.user.id = :userId THEN true ELSE false END,
                    b.user.username,
                    b.createdAt,
                    (SELECT COUNT(l.id) FROM Love l WHERE l.board.id = :boardId),
                    (SELECT CASE WHEN COUNT(l2) > 0 THEN true ELSE false END
                     FROM Love l2
                     WHERE l2.board.id = :boardId AND l2.user.id = :userId)
                )
                FROM Board b
                WHERE b.id = :boardId
                """;

        Query query = em.createQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("boardId", boardId);

        return null;
    }

    public Board findByIdJoinUser(Integer id) {
        Query query = em.createQuery("select b from Board b join fetch b.user where b.id = :id", Board.class);
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }

    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    public List<Board> findAll() {
        String sql = "select b from Board b where b.isPublic = true order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        return query.getResultList();
    }

    public List<Board> findAll(Integer userId) {
        String sql = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void save(Board board) {
        em.persist(board);
    }


}
