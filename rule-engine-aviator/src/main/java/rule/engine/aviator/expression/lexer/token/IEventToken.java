package rule.engine.aviator.expression.lexer.token;

import java.io.Serializable;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-04
 */
public interface IEventToken<T> extends Serializable {
    enum EventTokenType {
        Event, Operator, Char
    }

    T getValue(Map<String, Object> env);

    IEventToken.EventTokenType getType();

    String getLexeme();

    int getStartIndex();
}
