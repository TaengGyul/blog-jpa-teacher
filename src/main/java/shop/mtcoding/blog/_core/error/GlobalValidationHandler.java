package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog._core.error.ex.Exception400;

import java.util.List;

// 관점 Aspect, PointCut, Advice
@Aspect //관점 관리
@Component
public class GlobalValidationHandler {

    // 우리는 400번만 할 거임
    // 이걸 Point Cut이라고 함
    // 딴 데서는 이렇게 못 씀, 여기서만 가능!
    // 관심사를 분리 시킴 => 이게 AOP
    // PostMapping 혹은 PutMapping이 붙어있는 메서드를 실행하기 직전에 Advice를 호출하라.
    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")

    public void badRequestAdvice(JoinPoint jp) { // jp는 실행될 실제 메서드의 모든 것을 투영하고 있다.
        Object[] args = jp.getArgs(); // 메서드의 매개변수들
        for (Object arg : args) { // 매개변수의 개수만큼 반복 (어노테이션은 제외)

            // 공통 모듈 : 이 밑에는 바뀔 수가 없음.
            // Errors Type이 매개변수에 존재하고!!
            if (arg instanceof Errors) {
                System.out.println("에러 400 처리 필요함!!");
                Errors errors = (Errors) arg;

                // Error가 존재한다면!!
                if (errors.hasErrors()) {
                    List<FieldError> fError = errors.getFieldErrors();

                    for (FieldError fieldError : fError) {
                        throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage()); // getField() = 필드명
                    }
                }
            }
        }
    }
}
