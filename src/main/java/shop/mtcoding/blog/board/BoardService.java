package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.ExceptionApi403;
import shop.mtcoding.blog.love.Love;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;
    private final ReplyRepository replyRepository;

    // TODO
    public void 글수정하기() {

    }

    @Transactional
    public void 글삭제하기(Integer boardId) {
        // 1. 게시글이 존재하는지 확인
        Board board = boardRepository.findById(boardId);

        if (!board.getUser().getId().equals()) {
            throw new ExceptionApi403("해당 게시글을 삭제할 권한이 없습니다.");
        }

        // 2. 삭제
        boardRepository.delete(boardId);

    }


    public List<Board> 글목록보기(Integer userId) {
        if (userId == null) {
            return boardRepository.findAll();
        } else {
            return boardRepository.findAll(userId);
        }
    }

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    public BoardResponse.DetailDTO 글상세보기(Integer id, Integer sessionUserId) {
        Board board = boardRepository.findByIdJoinUserAndReplies(id);

        Love love = loveRepository.findByUserIdAndBoardId(sessionUserId, id);
        Long loveCount = loveRepository.findByBoardId(id);

        Integer loveId = love == null ? null : love.getId();
        Boolean isLove = love == null ? false : true;


        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(board, sessionUserId, isLove, loveCount.intValue(), loveId);
        return detailDTO;
    }


}
