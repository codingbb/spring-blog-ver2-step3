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


    // TODO : 글 목록조회 API 필요  ->  @GetMapping("/");

    // TODO : 글 상세보기 API 필요  -> @GetMapping("/api/boards/{id}/detail")

    // TODO : 글 조회 API 필요  ->  @GetMapping("/api/boards/{id}")
    // 글상세보기 -> 글, 유저 정보, 댓글까지 다 있음
    // 글조회 -> 글만 조회 하는거라 글 수정할 때 필요


    @PostMapping("/api/boards")
    public String save(BoardRequest.SaveDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        //권한 체크는 생략
        boardService.글쓰기(requestDTO, sessionUser);

        return "redirect:/";
    }


    @PutMapping("/api/boards/{id}")
    public String update(@PathVariable Integer id, BoardRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.글수정(id, sessionUser.getId(), requestDTO);
        return "redirect:/board/" + id;
    }


    @DeleteMapping("/api/boards/{id}")
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



