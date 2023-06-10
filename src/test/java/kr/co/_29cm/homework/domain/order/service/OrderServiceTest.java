package kr.co._29cm.homework.domain.order.service;

import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.domain.order.entity.Order;
import kr.co._29cm.homework.domain.product.entity.Product;
import kr.co._29cm.homework.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;


    @Test
    @DisplayName("Multi thread 환경 하에서 동시성 테스트")
    void concurrency_test() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Product product = productService.getProductById(778422L).get();
        Order order = new Order(product, product.getStock());
        List<Order> orders = Arrays.asList(order);
        Future<?> submit1 = executorService.submit(() -> {
            orderService.makeOrders(orders);
            System.out.println("주문 성공1");
        });
        Future<?> submit2 = executorService.submit(() -> {
            orderService.makeOrders(orders);
            System.out.println("주문 성공2");
        });

        assertThrows(SoldOutException.class,()->{
            try {
                submit1.get();
                submit2.get();
            }catch (ExecutionException ex){
                System.out.println(ex.getMessage());
                throw ex.getCause();
            }
        });
    }

}