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
    private final String CROSS_LINE = "-----------------------------";
    private final Integer MINIMUM_ORDER_WITHOUT_DELIVERY_FEE = 50_000;
    private final Integer DELIVERY_FEE = 2_500;

    @Transactional
    public void makeOrders(List<Order> orders) throws SoldOutException {
        for(Order order : orders){
            makeOrder(order);
        }
    }

    @Transactional
    public void makeOrder(Order order) throws SoldOutException {
        productService.decreaseStock(order.getProduct().getId(), order.getQuantity());
        // Order 저장 등
    }

    public void printOrders(List<Order> orders) throws SoldOutException {
        if(orders.isEmpty()){
            return;
        }
        int orderSum = orders.stream().mapToInt(o->o.getProduct().getPrice()*o.getQuantity()).sum();
        int total = orderSum;
        if(orderSum<MINIMUM_ORDER_WITHOUT_DELIVERY_FEE){
            total+=DELIVERY_FEE;
        }
        System.out.println("주문내역: ");
        System.out.println(CROSS_LINE);
        for(Order order : orders){
            System.out.println(order.getProduct().getName()+" - "+order.getQuantity()+"개");
        }
        System.out.println(CROSS_LINE);
        System.out.println("주문금액: "+orderSum+"원");
        if(orderSum<MINIMUM_ORDER_WITHOUT_DELIVERY_FEE){
            System.out.println("배송비: "+DELIVERY_FEE+"원");
        }
        System.out.println(CROSS_LINE);
        System.out.println("지불금액: "+total+"원");
        System.out.println(CROSS_LINE);
    }



}
