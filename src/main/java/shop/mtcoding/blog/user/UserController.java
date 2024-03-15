package shop.mtcoding.blog.user;

import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog._core.errs.exception.Exception400;
import shop.mtcoding.blog._core.errs.exception.Exception401;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRepository;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository;
    private final HttpSession session;

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        userRepository.update(sessionUser.getId(), requestDTO);

        return "redirect:/";
    }

    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        //id는 세션에서 들고오자!
        User user = userRepository.findById(sessionUser.getId());
        request.setAttribute("user", user);

        return "user/update-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        try {
            userRepository.save(requestDTO.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new Exception400("동일한 유저네임이 존재합니다.");
        }

//        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }


    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        try {
            User sessionUser = userRepository.findByUsernameAndPassword(requestDTO);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";

        } catch (EmptyResultDataAccessException e) {
            throw new Exception401("유저네임 혹은 비밀번호가 틀렸어요");
        }

    }


    @GetMapping("/join-form")
    public String joinForm() {


        return "user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }



    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
