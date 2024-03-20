package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.errs.exception.Exception403;
import shop.mtcoding.blog._core.errs.exception.Exception404;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;


    // TODO : 글 목록조회 API 필요
    // TODO : 글 상세보기 API 필요
    // TODO : 글 조회 API 필요


    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        //권한 체크는 생략
        boardService.글쓰기(requestDTO, sessionUser);

        return "redirect:/";
    }


    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, BoardRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.글수정(id, sessionUser.getId(), requestDTO);
        return "redirect:/board/" + id;
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.글삭제(id, sessionUser.getId());

        return "redirect:/";
    }

//    @GetMapping("/board/{id}")
//    public @ResponseBody BoardResponse.DetailDTO detail(@PathVariable Integer id, HttpServletRequest request) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
//        return boardService.글상세보기(id, sessionUser);
//

//
//        request.setAttribute("isOwner", isOwner);
//        request.setAttribute("board", board);
//        return "board/detail";
    }



