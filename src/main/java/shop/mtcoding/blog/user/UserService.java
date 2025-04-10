package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.error.ex.Exception401;
import shop.mtcoding.blog._core.error.ex.Exception404;

import java.util.HashMap;
import java.util.Map;

// 비지니스로직, 트랜잭션처리, DTO 완료
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {
        try {
            userRepository.save(joinDTO.toEntity());
        } catch (Exception e) {
            // Exception400 (Bad Request -> 잘못된 요청입니다)
            throw new Exception400("잘못된 요청입니다.");
        }

    }

    public User 로그인(UserRequest.LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());

        if (user == null) throw new Exception401("유저네임 혹은 비밀번호가 틀렸습니다.");

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception401("유저네임 혹은 비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public Map<String, Object> 유저네임중복체크(String username) {
        User user = userRepository.findByUsername(username);
        Map<String, Object> dto = new HashMap<>();

        if (user == null) {
            dto.put("available", true);
        } else {
            dto.put("available", false);
        }
        return dto;
    }

    // 1. 영속화 시키기 (Select 조회 - pc 들어옴)
    @Transactional
    public User 회원정보수정(UserRequest.UpdateDTO updateDTO, Integer userId) {
        User user = userRepository.findById(userId);

        // Exception404
        if (user == null) throw new Exception404("죄송합니다. 해당 페이지를 찾을 수 없습니다.");
        user.update(updateDTO.getPassword(), updateDTO.getEmail()); // 영속화된 객체의 상태변경
        return user; // 리턴한 이유는 세션을 동기화해야해서!!
    } // 더티체킹 -> 상태가 변경되면 update을 날려요!!
}
