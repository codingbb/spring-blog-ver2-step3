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

    private final UserService userService;
    private final UserRepository userRepository;
    private final HttpSession session;


    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원수정폼(sessionUser.getId());
        //User user = userRepository.findById(id);
        request.setAttribute("user", user);

        return "user/update-form";
    }


    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User newSessionUser = userService.회원수정(sessionUser.getId(), requestDTO);
        session.setAttribute("sessionUser", newSessionUser);

        return "redirect:/";
    }


    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        userService.회원가입(requestDTO);

        return "redirect:/";
    }


    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        User sessionUser = userService.로그인(requestDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
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
