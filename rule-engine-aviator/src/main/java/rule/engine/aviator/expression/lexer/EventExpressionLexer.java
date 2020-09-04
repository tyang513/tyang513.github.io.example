package rule.engine.aviator.expression.lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.expression.lexer.token.EventToken;
import rule.engine.aviator.expression.lexer.token.IEventToken;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;

/**
 * 事件表达式-词法分析器 从表达式中扫描 Token(单词)
 *
 * @author tao.yang
 * @date 2020-09-02
 */
public class EventExpressionLexer {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExpressionLexer.class);
    /**
     * 字符迭代器
     */
    private final CharacterIterator iterator;
    /**
     * current char
     */
    private char peek;
    private String expression;

    /**
     * 链表 token的类型只有事件
     */
    private LinkedList<IEventToken<?>> tokenBuffer;

    /**
     * 构造函数
     *
     * @param expression 表达式
     */
    public EventExpressionLexer(String expression) {
        this.iterator = new StringCharacterIterator(expression);
        this.expression = expression;
        this.peek = this.iterator.current();
    }

    public IEventToken<?> scan() {
        return this.scan(true);
    }

    public IEventToken<?> scan(final boolean analyse) {

        if (this.tokenBuffer != null && !this.tokenBuffer.isEmpty()) {
            return this.tokenBuffer.pop();
        }

        if (this.peek == 'e' || Character.isDigit(this.peek)) {
            StringBuffer sb = new StringBuffer();
            int startIndex = this.iterator.getIndex();
            do {
                sb.append(this.peek);
                nextChar();
            } while (Character.isDigit(this.peek));
            return new EventToken(sb.toString(), startIndex);
        }

        return null;
    }

    public void nextChar() {
        this.peek = this.iterator.next();
    }

    public static void main(String[] args) {
        EventExpressionLexer lexer = new EventExpressionLexer("e1 -> (e2 AND !e3) -> e4");
        System.out.println(lexer.scan());
    }

}
