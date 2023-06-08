package kr.co._29cm.homework.command.prompt;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider
{
    @Override
    public AttributedString getPrompt()
    {
        return new AttributedString(
                "입력(o[order]: 주문, q[quit]: 종료) :",
                AttributedStyle.DEFAULT.background(AttributedStyle.BLACK));
    }
}

