package rule.engine.aviator.expression.lexer.token;

/**
 * @author tao.yang
 * @date 2020-09-02
 */
public enum EventOperatorType {

    AFTER("->", 2),

    AND("AND", 2),

    OR("OR", 2),

    NOT("!", 1),

    FUNC("()", Integer.MAX_VALUE),;

    public final String token;

    public final int operandCount;

    EventOperatorType(final String token, final int operandCount) {
        this.token = token;
        this.operandCount = operandCount;
    }

}
