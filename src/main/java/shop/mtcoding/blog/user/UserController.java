package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.error.anno.MyAfter;
import shop.mtcoding.blog._core.error.anno.MyAround;
import shop.mtcoding.blog._core.error.anno.MyBefore;
import shop.mtcoding.blog._core.error.ex.Exception400;
import shop.mtcoding.blog._core.util.Resp;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @MyAround
    @GetMapping("/v2/around")
    public @ResponseBody String around() {
        return "good";
    }

    // ViewResolver -> prefix = /templates/ -> suffix = .mustache
    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // update user_tb set password = ?, email = ? where id = ?
        User user = userService.회원정보수정(updateDTO, sessionUser.getId());

        // 세션 동기화
        session.setAttribute("sessionUser", user);

        return "redirect:/";
    }

    @GetMapping("/api/check-username-available/{username}")
    public @ResponseBody Resp<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> dto = userService.유저네임중복체크(username);
        return Resp.ok(dto);
    }

    @MyBefore
    @GetMapping("/join-form")
    public String joinForm() {
        System.out.println("joinForm 호출됨");
        return "user/join-form";
    }

    @MyAfter
    @PostMapping("/join")
    // 리플렉션 사용한 거임 다만, 직접 만든게 아니라 만들어져있는 걸 사용한 것
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) { // @Vaild를 붙이는 건 문법이다! 붙이지 않으면 리플렉션이 되지 않는다, 유효성 검사는 DTO 책임!, 유효성 검사는 PostMapping, PutMapping 일 때만 사용한다 -> 왜? body 데이터가 있으니까!
        System.out.println("join 호출됨");
        if (errors.hasErrors()) {
            List<FieldError> fError = errors.getFieldErrors();

            for (FieldError fieldError : fError) {
                throw new Exception400(fieldError.getField() + ": " + fieldError.getDefaultMessage()); // getField() = 필드명
            }
        }

        // 유효성 검사
//        Boolean r1 = Pattern.matches("^[a-zA-Z0-9]{2,20}$", joinDTO.getUsername());
//        Boolean r2 = Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{6,20}$", joinDTO.getPassword());
//        Boolean r3 = Pattern.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", joinDTO.getEmail());
//
//        if (!r1) throw new Exception400("유저네임은 2~20자이며, 특수문자, 한글이 포함될 수 없습니다.");
//        if (!r2) throw new Exception400("패스워드는 6~20자이며, 특수문자, 영어 대문자, 소문자, 숫자가 포함되어야 하며 공백이 있을 수 없습니다.");
//        if (!r3) throw new Exception400("이메일 형식에 맞게 적어주세요.");

        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpServletResponse response) { // Errors errors를 넣을 땐 무조건 DTO 옆에 둬야지 옮겨진다!!! **매우 중요**
        if (errors.hasErrors()) {
            List<FieldError> fError = errors.getFieldErrors();

            for (FieldError fieldError : fError) {
                throw new Exception400(fieldError.getField() + ": " + fieldError.getDefaultMessage()); // getField() = 필드명
            }
        }

        //System.out.println(loginDTO);
        User sessionUser = userService.로그인(loginDTO);
        session.setAttribute("sessionUser", sessionUser);

        if (loginDTO.getRememberMe() == null) {
            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0); // 즉시 만료
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("username", loginDTO.getUsername());
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }

        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login-form";
    }
}



