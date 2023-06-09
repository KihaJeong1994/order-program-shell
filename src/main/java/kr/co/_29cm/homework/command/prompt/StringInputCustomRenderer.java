package kr.co._29cm.homework.command.prompt;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.springframework.shell.component.StringInput;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class StringInputCustomRenderer implements Function<StringInput.StringInputContext, List<AttributedString>> {
    @Override
    public List<AttributedString> apply(StringInput.StringInputContext context) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append(context.getName());
        builder.append(" ");
        if (context.getResultValue() != null) {
            builder.append(context.getResultValue());
        }
        else  {
            String input = context.getInput();
            if (StringUtils.hasText(input)) {
                builder.append(input);
            }
            else {
                builder.append("");
            }
        }
        return Arrays.asList(builder.toAttributedString());
    }
}