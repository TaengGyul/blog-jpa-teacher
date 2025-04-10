package shop.mtcoding.blog.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplyRequest.SaveDTO reqDTO, User sessionUser) {
        replyRepository.save(reqDTO.toEntity(sessionUser));
    }

    @Transactional
    public Integer 댓글삭제(Integer id, Integer sessionUserId) {
        Reply replyPS = replyRepository.findById(id);

        if (replyPS == null) throw new RuntimeException("댓글이 없는데 어떻게 삭제해!");

        if (!replyPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("너 댓글 아닌데?");
        }

        int boardId = replyPS.getBoard().getId();
        System.out.println("삭제 전 : " + boardId);

        replyRepository.deleteById(id);

        int boardId2 = replyPS.getBoard().getId();
        System.out.println("삭제 후 : " + boardId);

        return boardId;
    }
}
