package shop.mtcoding.blog.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

//import 필요없음
@DataJpaTest
public class UserJPARepositoryTest {

    @Autowired
    private UserJPARepository userJPARepository;

    @Test
    public void findByUsernameAndPassword_test() {
        //given
        String username = "ssar";
        String password = "1234";

        //when
        userJPARepository.findByUsernameAndPassword(username, password);

        //then


    }

    @Test
    public void save_test() {
        //given
        //빌더 덕분에 생성자를 건드릴 필요가 없다!
        User user = User.builder()
                .username("happy")
                .password("1234")
                .email("happy@nate.com")
                .build();

        //when
        userJPARepository.save(user);

        //then

    }

    @Test
    public void findById_test() {
        //given
        int id = 1;

        //when
        userJPARepository.findById(id);

        //then

    }

    @Test
    public void findAll_test() {
        //given

        //when
        userJPARepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        //then

    }

    @Test
    public void findAll_paging_test() throws JsonProcessingException {
        //given
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 2, sort);

        //when
        Page<User> userPG = userJPARepository.findAll(pageable);

        //then
        ObjectMapper om = new ObjectMapper();   //objectMapper = 자바 클래스를 json으로 바꾸는 것
        String json = om.writeValueAsString(userPG);
        System.out.println(json);

    }

}
