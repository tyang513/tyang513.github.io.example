package rule.engine.aviator.expression.lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.expression.EventExpressionEvaluatorInstance;
import rule.engine.aviator.expression.IEventExpressionCodeGenerator;

/**
 * 事件表达式解析器
 *
 * @author tao.yang
 * @date 2020-09-07
 */
public class EventExpressionParser implements IExpressionParser {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExpressionParser.class);

    /**
     * 事件计算实例
     */
    private EventExpressionEvaluatorInstance evaluatorInstance;

    /**
     * 词法解析器
     */
    private EventExpressionLexer lexer;

    /**
     * 代码生成器
     */
    private IEventExpressionCodeGenerator codeGenerator;

    /**
     * 默认的构造函数
     *
     * @param evaluatorInstance 事件表达式计算实例
     * @param lexer             词法解析器
     * @param codeGenerator     代码生成方式
     */
    public EventExpressionParser(EventExpressionEvaluatorInstance evaluatorInstance, EventExpressionLexer lexer,
                                 IEventExpressionCodeGenerator codeGenerator) {
        this.evaluatorInstance = evaluatorInstance;
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
    }

    public void parser() {

    }
}
