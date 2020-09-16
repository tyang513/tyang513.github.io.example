package rule.engine.aviator.expression.code;

import com.googlecode.aviator.lexer.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.expression.lexer.token.IEventToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成
 * @author tao.yang
 * @date 2020-09-07
 */
public class OptimizeEventExpressionCodeGenerator implements IEventExpressionCodeGenerator  {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(OptimizeEventExpressionCodeGenerator.class);

    private final List<Token<?>> tokenList = new ArrayList<>();

    @Override
    public void onJoinLeft(IEventToken<?> lookhead) {

    }

    @Override
    public void onJoinRight(IEventToken<?> lookhead) {

    }
}
