package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    @Transactional
    public User save(User user) {
        em.persist(user);
        return user;
    }

    //비밀번호 해쉬 때문에 username밖에 못써서 password는 x
    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createQuery("select u from User u where u.username = :username and u.password = :password", User.class);
        query.setParameter("username", requestDTO.getUsername());
        query.setParameter("password", requestDTO.getPassword());
        return (User) query.getSingleResult();

    }

}
