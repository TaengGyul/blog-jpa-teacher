package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;

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

    public BoardResponse.DetailDTO 글상세보기(Integer id, Integer userId) {
        BoardResponse.DetailDTO detailDTO = boardRepository.findDetail(id, userId);
        return detailDTO;
    }
}
