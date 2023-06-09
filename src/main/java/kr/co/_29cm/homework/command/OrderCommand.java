package kr.co._29cm.homework.command;

import kr.co._29cm.homework.command.prompt.StringInputCustomRenderer;
import kr.co._29cm.homework.common.exception.CustomException;
import kr.co._29cm.homework.common.exception.NoSuchProductException;
import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.order.entity.Order;
import kr.co._29cm.homework.order.service.OrderService;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.shell.command.annotation.ExceptionResolver;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class OrderCommand extends AbstractShellComponent {

    private final ProductService productService;
    private final OrderService orderService;
    private final String CROSS_LINE = "-----------------------------";
    private final Integer MINIMUM_ORDER_WITHOUT_DELIVERY_FEE = 50_000;
    private final Integer DELIVERY_FEE = 2_500;

    @ShellMethod(key = {"o","order"}, value = "String input", group = "Components")
//    @Transactional
    public void order() throws SoldOutException {
        boolean isFinished = false;
        List<Order> orders = new ArrayList<>();
        productService.printProductList();
        while (!isFinished){
                Product product = enterProduct();
                Integer quantity = enterQuantity();
                if(product==null && quantity==null){
                    isFinished = true;
                    orderService.makeOrders(orders);
                    printOrders(orders);
                }else{
                    Order order = new Order(product, quantity);
                    orders.add(order);
                }
        }
    }


    @ExceptionResolver({ CustomException.class })
    CommandHandlingResult errorHandler(CustomException e) {
        return CommandHandlingResult.of(e.getMessage()+'\n', 1);
    }


    private Product enterProduct() {
        while (true){
            String productIdStr = getInput("상품번호 :");
            if(StringUtils.hasText(productIdStr)){
                try{
                    Long productId = Long.parseLong(productIdStr);
                    return productService.getProductById(productId).orElseThrow(()->new NoSuchProductException(productId));
                }catch (NumberFormatException ex){
                    System.out.println("NumberFormatException 발생. 숫자를 입력해주세요");
                } catch (NoSuchProductException e) {
                    System.out.println("NoSuchProductException 발생. "+productIdStr+" 상품번호를 가진 상품이 없습니다");
                }
            }else {
                return null;
            }
        }
    }



    private Integer enterQuantity() {
        while(true){
            Integer quantity = null;
            String quantityStr = getInput("수량 :");
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

    private String getInput(String s) {
        StringInput component = new StringInput(getTerminal(), s, null, new StringInputCustomRenderer());
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        StringInputContext context = component.run(StringInputContext.empty());
        return context.getResultValue();
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
