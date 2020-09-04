package rule.engine.aviator.expression.lexer.token;

/**
 * 操作符 token
 * @author tao.yang
 * @date 2020-09-04
 */
public class EventOperatorToken {

    private final EventOperatorType operatorType;


    public EventOperatorToken(EventOperatorType operatorType) {
        this.operatorType = operatorType;
    }
}
