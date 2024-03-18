package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errs.exception.Exception400;
import shop.mtcoding.blog._core.errs.exception.Exception401;
import shop.mtcoding.blog._core.errs.exception.Exception404;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserJPARepository userJPARepository;

    @Transactional
    public User 회원수정(int id, UserRequest.UpdateDTO requestDTO) {
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다."));

        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());
        return user;

    }   //더티체킹


    public User 회원수정폼(int id) {
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다."));
        return user;

    }


    //조회라 트랜젝션 안 붙여도 됨!
    public User 로그인(UserRequest.LoginDTO requestDTO) {
        //나중에 해시 비교하는 이런 코드 여기에 들어옴
        User sessionUser = userJPARepository.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        return sessionUser;

    }


    @Transactional
    public void 회원가입(UserRequest.JoinDTO requestDTO) {
        // 1. 유효성 검사 -> 컨트롤러 책임 x

        // 2. 유저네임 중복검사 (서비스 체크) - DB연결 필요
        Optional<User> userOP = userJPARepository.findByUsername(requestDTO.getUsername());

        //isPresent가 있으면 비정상
        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다.");
        }


        userJPARepository.save(requestDTO.toEntity());
    }

}
