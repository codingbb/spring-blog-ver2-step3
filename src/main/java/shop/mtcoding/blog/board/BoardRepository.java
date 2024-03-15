package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    @Transactional
    public void updateById(int id, String title, String content) {
        Board board = findById(id);
        board.setTitle(title);
        board.setContent(content);
        //더티체킹
    }


    @Transactional
    public void deleteById(int id) {
        Query query = em.createQuery("delete from Board b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    public List<User> findAllV2_v2() {
        String q1 = "select b from Board b order by b.id desc";
        //lazy로딩하면 망한다. 인쿼리(동적쿼리)로 만들어야 함.
        List<Board> boardList = em.createQuery(q1, Board.class).getResultList();    //애 크기 4

        List<Integer> userIds = boardList.stream().mapToInt(board ->
                board.getUser().getId()).distinct().boxed().toList();

        //-----------------------------------

        //boardList가 가지고 있는 유저의 개수만큼 ?를 걸어서 in 쿼리가 나와야함.
        String q = "select u from User u where u.id in (";
        for (int i=0; i<userIds.size(); i++){
            //0부터 시작해서 -1을 해줌. 쿼리문 마지막에는 )를 닫아야하니까, 마지막에 )를 해줌
            if(i == userIds.size() -1){
                q = q + ":id" + i + ")";
            }else{
                q = q + ":id" + i + ",";
            }
        }

        Query query = em.createQuery(q, User.class);
        //in 뒤에 들어갈게 board id니까!
        for (int i = 0; i < userIds.size(); i++) {
            query.setParameter("id" + i, userIds.get(i));
        }

        List<User> userList = query.getResultList();
        System.out.println(userList);

        for (Board b : boardList) {
            for (int i = 0; i < boardList.size(); i++) {
                if (userList.get(i).getId() == b.getUser().getId()) {
                    b.setUser(userList.get(i));
                }
            }
        }

//        List<User> userList = em.createQuery(q, User.class).getResultList();   //애 크기 3

        return userList; //리턴되는 boardList에는 user가 채워져 있어야 함


    }

    public List<Board> findAllV2() {
        String q1 = "select b from Board b order by b.id desc";
        //lazy로딩하면 망한다. 인쿼리(동적쿼리)로 만들어야 함.
        List<Board> boardList = em.createQuery(q1, Board.class).getResultList();    //애 크기 4

        String q2 = ""; //boardList가 가지고 있는 유저의 개수만큼 ?를 걸어서 in 쿼리가 나와야함.
        List<User> userList = em.createQuery(q2, User.class).getResultList();   //애 크기 3

        return boardList; //리턴되는 boardList에는 user가 채워져 있어야 함
        //stream의 filter 사용

    }


    public List<Board> findAll() {
        Query query = em.createQuery("select b from Board b order by b.id desc", Board.class);
        return query.getResultList();
    }

    public Board findByIdJoinUser(int id) {
        Query query = em.createQuery("select b from Board b join fetch b.user u where b.id = :id", Board.class); //이게 바로 조인
        query.setParameter("id", id);
        return (Board) query.getSingleResult();

    }

    public Board findById(int id) {
        //id, title, content, user_id(이질감), created_at
        Board board = em.find(Board.class, id);
        return board;

    }

}
