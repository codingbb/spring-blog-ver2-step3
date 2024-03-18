package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errs.exception.Exception403;
import shop.mtcoding.blog._core.errs.exception.Exception404;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardJPARepository boardJPARepository;

    public Board 게시글수정폼(int boardId, int sessionUserId) {
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        //2. 권한 처리
        if (sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        return board;
    }


    @Transactional
    public void 글수정(int boardId, int sessionUserId, BoardRequest.UpdateDTO requestDTO) {
        //1. 더티체킹 하기 위해 조회하고 예외처리
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        //2. 권한 처리
        if (sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        //3. 실제 글 수정 (더티체킹 함)
        board.setTitle(requestDTO.getTitle());
        board.setContent(requestDTO.getContent());
    }


    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO requestDTO, User sessionUser) {
        boardJPARepository.save(requestDTO.toEntity(sessionUser));
    }



}
