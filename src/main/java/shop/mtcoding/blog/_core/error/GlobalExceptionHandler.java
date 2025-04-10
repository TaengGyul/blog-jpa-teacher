package shop.mtcoding.blog._core.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.blog._core.error.ex.*;
import shop.mtcoding.blog._core.util.Resp;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 요청입니다
    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e) {
        // 여기가 catch 한 자리
        String html = """
                <script>
                alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());
        return html;

    }

    @ExceptionHandler(ExceptionApi400.class)
    public Resp<?> exApi400(ExceptionApi400 e) {

        return Resp.fail(400, e.getMessage());

    }

    // 인증 안됨
    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e) {
        // 여기가 catch 한 자리
        String html = """
                <script>
                alert('${msg}');
                location.href = "/login-form";
                </script>
                """.replace("${msg}", e.getMessage());
        return html;

    }

    @ExceptionHandler(ExceptionApi401.class)
    public Resp<?> exApi401(ExceptionApi401 e) {

        return Resp.fail(401, e.getMessage());

    }

    // 권한 없음
    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e) {
        // 여기가 catch 한 자리
        String html = """
                <script>
                alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());
        return html;

    }

    @ExceptionHandler(ExceptionApi403.class)
    public Resp<?> exApi403(ExceptionApi403 e) {

        return Resp.fail(403, e.getMessage());

    }

    // 자원 찾을 수 없음
    @ExceptionHandler(Exception404.class)
    public String ex404(Exception404 e) {
        // 여기가 catch 한 자리
        String html = """
                <script>
                alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());
        return html;

    }

    @ExceptionHandler(ExceptionApi404.class)
    public Resp<?> exApi404(ExceptionApi404 e) {

        return Resp.fail(404, e.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public String exUnKnown(Exception e) { // 얜 무조건 하나 필요함
        String html = """
                <script>
                alert('${msg}');
                history.back();
                </script>
                """.replace("${msg}", "관리자에게 문의해주세요.");
        System.out.println("관리자님 보세요 : " + e.getMessage()); // 로그를 파일에 기록해서 나중에 봐야함
        return html;

    }

}
