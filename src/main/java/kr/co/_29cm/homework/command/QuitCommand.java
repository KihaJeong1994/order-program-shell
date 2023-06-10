package kr.co._29cm.homework.command;

import org.springframework.shell.ExitRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class QuitCommand{

    @ShellMethod(key = {"q","quit"})
    public void quit() {
        System.out.println("고객님의 주문 감사합니다.");
        throw new ExitRequest();
    }

}
