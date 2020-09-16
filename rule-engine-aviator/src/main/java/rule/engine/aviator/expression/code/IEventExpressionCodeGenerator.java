package rule.engine.aviator.expression.code;

import rule.engine.aviator.expression.lexer.token.IEventToken;

/**
 * @author tao.yang
 * @date 2020-09-07
 */
public interface IEventExpressionCodeGenerator {

    public void onJoinLeft(IEventToken<?> lookhead);


    public void onJoinRight(IEventToken<?> lookhead);

}
