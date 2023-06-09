package kr.co._29cm.homework.order.service;

import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.order.entity.Order;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.repository.ProductRepository;
import kr.co._29cm.homework.product.service.ProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;


    @Test
    void concurrency_test() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Product product = productService.getProductById(778422L).get();
        Order order = new Order(product, product.getStock());
        List<Order> orders = Arrays.asList(order);
        Future<?> submit1 = executorService.submit(() -> {
            orderService.makeOrders(orders);
        });
        Future<?> submit2 = executorService.submit(() -> {
            orderService.makeOrders(orders);
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