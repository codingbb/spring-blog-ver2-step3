package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errs.exception.Exception403;
import shop.mtcoding.blog._core.errs.exception.Exception404;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardJPARepository boardJPARepository;

    public Board 글조회(int boardId, int sessionUserId) {
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        //2. 권한 처리
//        if (sessionUserId != board.getUser().getId()) {
//            throw new Exception403("게시글을 수정페이지로 이동 할 권한이 없습니다");
//        }

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


    @Transactional
    public void 글삭제(Integer boardId, Integer sessionUserId) {
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        if (sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글 삭제 권한 없음!");
        }
        boardJPARepository.deleteById(boardId);

    }

    public List<Board> 글목록조회() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return boardJPARepository.findAll(sort);

    }

    // board, isOwner
    public Board 글상세보기(int boardId, User sessionUser) {
        Board board = boardJPARepository.findByIdJoinUser(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        boolean isBoardOwner = false;
        if(sessionUser != null){
            if(sessionUser.getId() == board.getUser().getId()){
                isBoardOwner = true;
            }
        }

        board.setBoardOwner(isBoardOwner);

        board.getReplies().forEach(reply -> {
            boolean isReplyOwner = false;

            if(sessionUser != null){
                if(reply.getUser().getId() == sessionUser.getId()){
                    isReplyOwner = true;
                }
            }
            reply.setReplyOwner(isReplyOwner);
        });



        return board;
    }

//    // board와 isOwner를 응답해야 하는데, 메소드는 1개밖에 응답하지 못함. -> 하나의 덩어리로 만들어서 줘야함
//    public void 글상세보기(Integer boardId, User sessionUser) {
//        Board board = boardJPARepository.findByIdJoinUser(boardId)
//                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
//
//        // 로그인을 하고 게시글의 주인이면 isOwne가 true가 된다 !
//        boolean isOwner = false;
//        if (sessionUser != null) {
//            if (sessionUser.getId() == board.getUser().getId()) {
//                isOwner = true;
//            }
//        }
//    }

}
