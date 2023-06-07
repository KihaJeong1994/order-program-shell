package kr.co._29cm.homework.common.exception.handler;//package kr.co._29cm.homework.common.exception.handler;
//
//
//import kr.co._29cm.homework.common.exception.CustomException;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//
//@Aspect
//@Component
//public class GlobalExceptionHandler {
//
//    @AfterThrowing(pointcut = "execution(* kr.co._29cm.homework.*.*(..))",throwing = "ex")
//    public void handlerError(CustomException ex){
//        System.out.println("get into handler");
//        System.out.println(ex.getMessage());
//    }
//}
