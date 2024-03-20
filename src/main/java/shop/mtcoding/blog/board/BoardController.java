package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.errs.exception.Exception403;
import shop.mtcoding.blog._core.errs.exception.Exception404;
import shop.mtcoding.blog._core.utils.ApiUtil;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    // TODO : 글 목록조회 API 필요  ->  @GetMapping("/");
    @GetMapping("/")
    public ResponseEntity<?> main() {
        List<BoardResponse.MainDTO> respDTO = boardService.글목록조회();
        return ResponseEntity.ok(new ApiUtil(respDTO));
    }

    // TODO : 글 상세보기 API 필요  -> @GetMapping("/api/boards/{id}/detail")
    @GetMapping("/api/boards/{id}/detail")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.글상세보기(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil(board));
    }

    // TODO : 글 조회 API 필요  ->  @GetMapping("/api/boards/{id}")
    // 글상세보기 -> 글, 유저 정보, 댓글까지 다 있음 +)동사 추가함 detail
    // 글조회 -> 글만 조회 하는거라 글 수정할 때 필요
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Board board = boardService.글조회(id);
        return ResponseEntity.ok(new ApiUtil(board));
    }



    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody BoardRequest.SaveDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        //권한 체크는 생략
        Board board = boardService.글쓰기(requestDTO, sessionUser);

        return ResponseEntity.ok(new ApiUtil(board));
    }


    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody BoardRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.글수정(id, sessionUser.getId(), requestDTO);
        return ResponseEntity.ok(new ApiUtil(board));
    }


    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.글삭제(id, sessionUser.getId());

        return ResponseEntity.ok(new ApiUtil(null));
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



