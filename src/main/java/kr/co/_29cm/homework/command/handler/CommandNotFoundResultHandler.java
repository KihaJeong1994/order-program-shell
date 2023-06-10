package kr.co._29cm.homework.command.handler;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.CommandNotFound;
import org.springframework.shell.result.TerminalAwareResultHandler;
import org.springframework.stereotype.Component;


@Component
public class CommandNotFoundResultHandler extends TerminalAwareResultHandler<CommandNotFound> {


    protected CommandNotFoundResultHandler(Terminal terminal) {
        super(terminal);
    }

    @Override
    protected void doHandleResult(CommandNotFound result) {
        String message = new AttributedString("정해진 명령어를 입력해주세요.",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE)).toAnsi();
        terminal.writer().println(message);
        terminal.writer().flush();
    }
}
