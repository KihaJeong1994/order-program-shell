package kr.co._29cm.homework.command;

import kr.co._29cm.homework.command.prompt.StringInputCustomRenderer;
import kr.co._29cm.homework.common.exception.CustomException;
import kr.co._29cm.homework.common.exception.NoSuchProductException;
import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.order.entity.Order;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.StringInput.StringInputContext;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class QuitCommand{

    @ShellMethod(key = {"q","quit"})
    public void quit() {
        System.out.println("고객님의 주문 감사합니다.");
        throw new ExitRequest();
    }

}
