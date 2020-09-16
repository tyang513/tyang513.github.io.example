package rule.engine.aviator.expression.lexer.token;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-08
 */
public class EventCharToken implements IEventToken {

    private final char ch;

    private int startIndex;

    public EventCharToken(char peek, int index) {
        this.ch = peek;
        this.startIndex = index;
    }

    @Override
    public Character getValue(Map env) {
        return ch;
    }

    @Override
    public EventTokenType getType() {
        return EventTokenType.Char;
    }

    @Override
    public String getLexeme() {
        return String.valueOf(ch);
    }

    @Override
    public int getStartIndex() {
        return startIndex;
    }

    public char getCh() {
        return ch;
    }

    @Override
    public String toString() {
        return "EventCharToken{" +
                "ch=" + ch +
                ", startIndex=" + startIndex +
                '}';
    }
}
