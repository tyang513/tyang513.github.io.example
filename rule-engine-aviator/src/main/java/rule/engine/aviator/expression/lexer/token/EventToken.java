package rule.engine.aviator.expression.lexer.token;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-04
 */
public class EventToken<T> implements IEventToken {

    private String lexeme;

    private int startIndex;

    public EventToken(String lexeme, int startIndex){
        this.lexeme = lexeme;
        this.startIndex = startIndex;
    }

    @Override
    public Object getValue(Map env) {
        return lexeme;
    }

    @Override
    public EventTokenType getType() {
        return EventTokenType.Event;
    }

    @Override
    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public int getStartIndex() {
        return this.startIndex;
    }

    @Override
    public String toString() {
        return "EventToken{" +
                "lexeme='" + lexeme + '\'' +
                ", startIndex=" + startIndex +
                '}';
    }
}
