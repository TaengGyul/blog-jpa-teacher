package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component // Component 를 쓰면 IOC에 뜬다
@Aspect
public class GlobalValidationHandler {

    // 앞 관리하는 프록시
    @Before("@annotation(shop.mtcoding.blog._core.error.anno.MyBefore)") // 인터셉터랑 다른점 : 매개변수 정보까지 다 알 수 있다. (왜? 분석이 되서)
    public void beforeAdvice(JoinPoint jp) { // jp 안에 해당 정보가 투영되어있다. (리플렉션 되있는 것임)
        String name = jp.getSignature().getName();
        System.out.println("Before Advice :" + name);
    }

    // 뒤 관리하는 프록시
    @After("@annotation(shop.mtcoding.blog._core.error.anno.MyAfter)")
    public void afterAdvice(JoinPoint jp) {
        System.out.println("매개변수 크기 : " + jp.getArgs().length);
        String name = jp.getSignature().getName();
        System.out.println("After Advice :" + name);
    }


    // 앞,뒤 모두 관리하는 프록시
    @Around("@annotation(shop.mtcoding.blog._core.error.anno.MyAround)")
    public Object aroundAdvice(ProceedingJoinPoint jp) {
        String name = jp.getSignature().getName();
        System.out.println("Around Advice 직전 : " + name);

        try {
            Object result = jp.proceed(); // 컨트롤러 함수가 호출
            System.out.println("Around Advice 직후 : " + name);
            System.out.println("result 값 : " + result);
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}

