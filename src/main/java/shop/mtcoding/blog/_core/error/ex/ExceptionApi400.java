package shop.mtcoding.blog._core.error.ex;

//ajax 요청은 여기서 처리
public class ExceptionApi400 extends RuntimeException {
    public ExceptionApi400(String message) {
        super(message);
    }
}
