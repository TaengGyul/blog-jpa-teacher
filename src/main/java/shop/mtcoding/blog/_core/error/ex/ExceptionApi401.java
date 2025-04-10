package shop.mtcoding.blog._core.error.ex;

//ajax 요청은 여기서 처리
public class ExceptionApi401 extends RuntimeException {
    public ExceptionApi401(String message) {
        super(message);
    }
}
