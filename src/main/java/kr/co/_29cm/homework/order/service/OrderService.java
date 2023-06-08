package kr.co._29cm.homework.order.service;

import kr.co._29cm.homework.common.exception.CustomException;
import kr.co._29cm.homework.common.exception.NoSuchProductException;
import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.order.entity.Order;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;

    @Transactional
    public Boolean makeOrders(List<Order> orders) {
        return transactionTemplate.execute(status -> {
            try {
                for(Order order : orders) makeOrder(order);
                return true;
            } catch (SoldOutException e) {
                status.setRollbackOnly();
                return false;
            }
        });
    }

    @Transactional
    public void makeOrder(Order order) throws SoldOutException {
        productService.decreaseStock(order.getProduct().getId(), order.getQuantity());
        // Order 저장 등
    }



}
