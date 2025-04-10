package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.ExceptionApi403;
import shop.mtcoding.blog._core.error.ex.ExceptionApi404;

@RequiredArgsConstructor
@Service
public class LoveService {
    private final LoveRepository loveRepository;

    @Transactional
    public LoveResponse.SaveDTO 좋아요(LoveRequest.SaveDTO reqDTO, Integer sessionUserId) {
        Love lovePS = loveRepository.save(reqDTO.toEntity(sessionUserId));
        Long loveCount = loveRepository.findByBoardId(reqDTO.getBoardId());
        return new LoveResponse.SaveDTO(lovePS.getId(), loveCount.intValue());
    }

    @Transactional
    public LoveResponse.DeleteDTO 좋아요취소(Integer id, Integer sessionUserId) {

        Love lovePS = loveRepository.findById(id);
        // ExceptionApi404
        if (lovePS == null) throw new ExceptionApi404("404 오류 : 요청하신 리소스를 찾을 수 없습니다.");

        // 권한 체크 (lovePS.getUser().getId() 비교 sessionUserId)
        if (!lovePS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다.");
        }

        Integer boardId = lovePS.getBoard().getId();

        loveRepository.deleteById(id);

        Long loveCount = loveRepository.findByBoardId(id);
        return new LoveResponse.DeleteDTO(loveCount.intValue());
    }

}