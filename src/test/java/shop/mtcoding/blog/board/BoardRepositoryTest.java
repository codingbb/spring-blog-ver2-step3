package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRepository;
import shop.mtcoding.blog.user.User;

import java.util.Arrays;
import java.util.List;

@Import(BoardRepository.class)
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void updateById_test() {
        //given

        int id = 1;
        String title = "title1";
        String content = "content1";

        //when
        boardRepository.updateById(id, title, content);
        em.flush(); //업데이트가 된 건지 확인이 안 되기 때문에 (트랜젝션 종료 후 쿼리가 날아가서)
                    //실제 코드는 작성할 필요가 없다. 트랜젝션 종료될 거라!
                    //em.flush를 꼭 해줘야함!

        //then

    }


    @Test
    public void deleteById_test() {
        //given
        int id = 1;

        //when
        boardRepository.deleteById(id); //delete쿼리 발동함!

        //then
        System.out.println("deleteById_test : " + boardRepository.findAll().size());

    }



    @Test
    public void userlist_test() {
        //given


        //when
        List<User> userList = boardRepository.findAllV2_v2();
        System.out.println(userList);

        //then
    }

    //쿼리문을 만들어야함. ? 개수를 정해주는 것! id가 몇 개 들어올지 모르니까!
    @Test
    public void randomquery_test(){
//        int[] ids = {1, 2, 3, 4};
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // select u from User u where u.id in (?,?);
        String q = "select u from User u where u.id in (";
        for (int i=0; i<ids.size(); i++){
            //0부터 시작해서 -1을 해줌. 쿼리문 마지막에는 )를 닫아야하니까, 마지막에 )를 해줌
            if(i == ids.size() -1){
                q = q + ":id" + i + ")";
            }else{
                q = q + ":id" + i + ",";
            }
        }
        System.out.println(q);
    }

    @Test
    public void findAll_custom_inquery2_test() {
        List<Board> boardList = boardRepository.findAll();

        List<Integer> userIds = boardList.stream().mapToInt(board ->
            board.getUser().getId()).distinct().boxed().toList();

        System.out.println(userIds);

    }


    @Test
    public void findAll_custom_inquery_test() {
        List<Board> boardList = boardRepository.findAll();

        int[] userIds = boardList.stream().mapToInt(board -> board.getUser().getId()).distinct().toArray();
        for (int i : userIds){
            System.out.println(i);
        }

        // select * from user_tb where id in (3,2,1,1);
        // select * from user_tb where id in (3,2,1);
    }

    @Test
    public void findAll_lazyloading_test() {
        List<Board> boardList = boardRepository.findAll();
        boardList.forEach(board -> {
            System.out.println(board.getUser().getUsername()); //lazy loading
        });
    }

    @Test
    public void findAll_test() {
        //given

        //when
        List<Board> boardList = boardRepository.findAll();

        //then
    }

    @Test
    public void findById_test() {
        int id = 1;
        System.out.println("start - 1");
        Board board = boardRepository.findById(id);
        System.out.println("start - 2");
        System.out.println(board.getUser().getId());
        System.out.println("start - 3");
        System.out.println(board.getUser().getUsername());

    }

}
