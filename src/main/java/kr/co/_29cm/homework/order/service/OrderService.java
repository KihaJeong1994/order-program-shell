package kr.co._29cm.homework.order.service;

import kr.co._29cm.homework.common.exception.CustomException;
import kr.co._29cm.homework.common.exception.NoSuchProductException;
import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.order.entity.Order;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class OrderService {

    private Scanner sc = new Scanner(System.in);
    private final ProductService productService;
    private final String CROSS_LINE = "-----------------------------";
    private final Integer MINIMUM_ORDER_WITHOUT_DELIVERY_FEE = 50_000;
    private final Integer DELIVERY_FEE = 2_500;
    private final TransactionTemplate transactionTemplate;
    public void startOrders(){
        boolean isFinished = false;
        List<Order> orders = new ArrayList<>();
        productService.printProductList();
        while (!isFinished){
            try{
                Product product = enterProduct();
                Integer quantity = enterQuantity();
                if(product==null && quantity==null){
                    isFinished = true;
                    if(!isOrderingSuccessful(orders)){
                        throw new SoldOutException();
                    };
                    printOrders(orders);
                }else{
                    Order order = new Order(product, quantity);
                    orders.add(order);
                }
            }catch (CustomException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    // TODO : 동시성 테스트 추가
    private Boolean isOrderingSuccessful(List<Order> orders) {
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

    private void makeOrder(Order order) throws SoldOutException {
        productService.decreaseStock(order.getProduct().getId(), order.getQuantity());
        // Order 저장 등
    }

    private Product enterProduct() throws NoSuchProductException {
        while(true){
            System.out.print("상품번호 : ");
            String productIdStr = sc.nextLine();
            if(StringUtils.hasText(productIdStr)){
                try{
                    Long productId = Long.parseLong(productIdStr);
                    return productService.getProductById(productId).orElseThrow(()->new NoSuchProductException(productId));
                }catch (NumberFormatException ex){
                    System.out.println("NumberFormatException 발생. 숫자를 입력해주세요");
                }
            }else {
                return null;
            }
        }

    }
    private Integer enterQuantity() {
        while(true){
            Integer quantity = null;
            System.out.print("수량 : ");
            String quantityStr = sc.nextLine();
            if(StringUtils.hasText(quantityStr)){
                try{
                    quantity = Integer.parseInt(quantityStr);
                    return quantity;
                }catch (NumberFormatException ex){
                    System.out.println("NumberFormatException 발생. 숫자를 입력해주세요");
                }
            }else{
                return quantity;
            }
        }
    }

    private void printOrders(List<Order> orders) throws SoldOutException {
        if(orders.isEmpty()){
            return;
        }
        int orderSum = orders.stream().mapToInt(o->o.getProduct().getPrice()*o.getQuantity()).sum();
        int total = orderSum;
        if(orderSum<MINIMUM_ORDER_WITHOUT_DELIVERY_FEE){
            total+=DELIVERY_FEE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("주문내역: ").append('\n');
        sb.append(CROSS_LINE).append('\n');
        for(Order order : orders){
            sb.append(order.getProduct().getName()).append(" - ").append(order.getQuantity()).append("개").append('\n');
        }
        sb.append(CROSS_LINE).append('\n');
        sb.append("주문금액: ").append(orderSum).append("원").append('\n');
        if(orderSum<MINIMUM_ORDER_WITHOUT_DELIVERY_FEE){
            sb.append("배송비: ").append(DELIVERY_FEE).append("원").append('\n');
        }
        sb.append(CROSS_LINE).append('\n');
        sb.append("지불금액: ").append(total).append("원").append('\n');
        sb.append(CROSS_LINE);
        System.out.println(sb.toString());
    }


}
