package shop.mtcoding.blog._core.error.ex;

//ajax 요청은 여기서 처리
public class ExceptionApi403 extends RuntimeException {
    public ExceptionApi403(String message) {
        super(message);
    }
}

