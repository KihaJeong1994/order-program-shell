//package kr.co._29cm.homework.common.program;
//
//import kr.co._29cm.homework.order.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Scanner;
//
//@Service
//@RequiredArgsConstructor
//public class OrderProgram {
//    private boolean running = true;
//    private final OrderService orderService;
//
//    public void run(){
//        System.out.println("상품 주문 프로그램을 시작합니다.");
//        Scanner sc = new Scanner(System.in);
//        while (running){
//            System.out.print("입력(o[order]: 주문, q[quit]: 종료) :");
//            String command = sc.nextLine();
//            if("o".equals(command)|| "order".equals(command)){
//                orderService.startOrders();
//            }else if("q".equals(command)|| "quit".equals(command)){
//                quit();
//            }else {
//                System.out.println("정해진 명령어를 입력해주세요.");
//            }
//
//        }
//        System.out.println("상품 주문 프로그램을 종료합니다.");
//    }
//
//    private void quit() {
//        running = false;
//    }
//
//}
