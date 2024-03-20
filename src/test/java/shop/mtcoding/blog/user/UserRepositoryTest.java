package shop.mtcoding.blog.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(UserRepository.class)   //IoC 등록 코드
@DataJpaTest    //Datasource/(connection pool), EntityManager
public class UserRepositoryTest {

    @Autowired  //애를 걸면 userRepository를 DI 걸 수 있다. 테스트라 new가 안 된다는 듯
    private UserRepository userRepository;

    @Test
    public void findById_test() {
        //given
        int id = 1;

        //when
        userRepository.findById(id);

        //then
    }


    @Test
    public void findByUsername_test() {
        //given
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setUsername("ssar");
        requestDTO.setPassword("1234");

        //when
        User user = userRepository.findByUsernameAndPassword(requestDTO);

        //then
        Assertions.assertThat(user.getUsername()).isEqualTo("ssar");


    }
}
