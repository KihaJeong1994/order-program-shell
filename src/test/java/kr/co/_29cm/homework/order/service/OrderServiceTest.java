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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;


    @Test
    @Disabled
    void concurrency_test() throws InterruptedException {
        Product productById = productService.getProductById(778422L).get();
        Order order = new Order(productById, 7);
        List<Order> orders = Arrays.asList(order);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch (2);
        AtomicBoolean result1 = new AtomicBoolean(true);
        AtomicBoolean result2 = new AtomicBoolean(true);


        assertThrows(SoldOutException.class,()->{
            try {
                executorService.execute(() -> {
                    try{
                        result1.set(orderService.makeOrders(orders));
                        latch.countDown();
                    }catch (SoldOutException e){
                        System.out.println(e.getMessage());
                        latch.countDown();
                        throw e;
                    }
                });
                executorService.execute(() -> {
                    try{
                        result2.set(orderService.makeOrders(orders));
                        latch.countDown();
                    }catch (SoldOutException e){
                        System.out.println(e.getMessage());
                        latch.countDown();
                        throw e;
                    }
                });
            }catch (SoldOutException e){

                throw e;
            }

            latch.await();
        });
    }

    @Test
    void rollback_test() throws SoldOutException {
        Product productById1 = productService.getProductById(782858L).get();
        Product productById = productService.getProductById(778422L).get();
        Order order1 = new Order(productById1, 8);
        Order order = new Order(productById, 8);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order);
        assertThrows(SoldOutException.class,()->orderService.makeOrders(orders));
    }



}