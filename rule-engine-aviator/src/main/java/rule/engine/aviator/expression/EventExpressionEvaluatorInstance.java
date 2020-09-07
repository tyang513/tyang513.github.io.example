package rule.engine.aviator.expression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.IExpression;
import rule.engine.aviator.expression.lexer.EventExpressionLexer;
import rule.engine.aviator.expression.lexer.EventExpressionParser;

/**
 * @author tao.yang
 * @date 2020-09-07
 */
public class EventExpressionEvaluatorInstance {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExpressionEvaluatorInstance.class);

    /**
     * 编译表达式
     *
     * @param expression 表达式
     * @return IExpression
     */
    public IExpression compile(String expression) {
        logger.info("编译 {}", expression);
        EventExpressionLexer lexer = new EventExpressionLexer(expression);
        IEventExpressionCodeGenerator codeGenerator = new OptimizeEventExpressionCodeGenerator();
        EventExpressionParser parser = new EventExpressionParser(this, lexer, codeGenerator);
        parser.parser();
        return null;
    }
}
