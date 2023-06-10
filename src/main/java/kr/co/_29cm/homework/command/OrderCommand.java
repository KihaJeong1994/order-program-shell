package kr.co._29cm.homework.command;

import kr.co._29cm.homework.command.prompt.StringInputCustomRenderer;
import kr.co._29cm.homework.common.exception.CustomException;
import kr.co._29cm.homework.common.exception.NoSuchProductException;
import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.domain.order.entity.Order;
import kr.co._29cm.homework.domain.order.service.OrderService;
import kr.co._29cm.homework.domain.product.entity.Product;
import kr.co._29cm.homework.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.shell.command.annotation.ExceptionResolver;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class OrderCommand extends AbstractShellComponent {

    private final ProductService productService;
    private final OrderService orderService;

    @ShellMethod(key = {"o","order"}, value = "String input", group = "Components")
    public void order() throws SoldOutException {
        boolean isFinished = false;
        List<Order> orders = new ArrayList<>();
        productService.printProductList();
        while (!isFinished){
                Product product = enterProduct();
                if(product==null){
                    isFinished = true;
                    orderService.makeOrders(orders);
                    orderService.printOrders(orders);
                    continue;
                }
                Integer quantity = enterQuantity();
                if(quantity==null){
                    isFinished = true;
                    orderService.makeOrders(orders);
                    orderService.printOrders(orders);
                    continue;
                }
                Order order = new Order(product, quantity);
                orders.add(order);
        }
    }


    @ExceptionResolver({ CustomException.class })
    CommandHandlingResult errorHandler(CustomException e) {
        return CommandHandlingResult.of(e.getMessage()+'\n', 1);
    }


    private String getInput(String s) {
        StringInput component = new StringInput(getTerminal(), s, null, new StringInputCustomRenderer());
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        StringInputContext context = component.run(StringInputContext.empty());
        return context.getResultValue();
    }

    private Product enterProduct() {
        while (true){
            String productIdStr = getInput("상품번호 :");
            if(StringUtils.hasText(productIdStr)){
                try{
                    Long productId = Long.parseLong(productIdStr);
                    return productService.getProductById(productId).orElseThrow(()->new NoSuchProductException(productId));
                }catch (NumberFormatException ex){
                    System.out.println("NumberFormatException 발생. 숫자를 입력해주세요.");
                } catch (NoSuchProductException e) {
                    System.out.println("NoSuchProductException 발생. "+productIdStr+" 상품번호를 가진 상품이 없습니다.");
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
                    if(quantity>0){
                        return quantity;
                    }else {
                        System.out.println("수량은 0보다 큰 값을 입력하여주세요.");
                    }
                }catch (NumberFormatException ex){
                    System.out.println("NumberFormatException 발생. 숫자를 입력해주세요.");
                }
            }else{
                return quantity;
            }
        }
    }




}
