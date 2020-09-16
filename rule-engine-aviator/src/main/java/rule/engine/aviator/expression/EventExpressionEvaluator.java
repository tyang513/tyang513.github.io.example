package rule.engine.aviator.expression;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-07
 */
public class EventExpressionEvaluator {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExpressionEvaluator.class);

    /**
     * Aviator version
     */
    public static final String VERSION = Utils.getAviatorScriptVersion();

    private static class StaticHolder {
        private static EventExpressionEvaluatorInstance INSTANCE = new EventExpressionEvaluatorInstance();
    }

    /**
     * Get the default evaluator instance
     *
     * @since 4.0.0
     * @return
     */
    public static EventExpressionEvaluatorInstance getInstance() {
        return EventExpressionEvaluator.StaticHolder.INSTANCE;
    }

    public static IEventExpression compile(final String expression) {
        return getInstance().compile(expression);
    }

    /**
     * Execute a text expression without caching and env map.
     *
     * @param expression
     * @return
     */
    public static Object execute(final String expression) {
        return execute(expression, (Map<String, Object>) null);
    }

    /**
     * Execute a text expression without caching
     *
     * @param expression
     * @param env
     * @return
     */
    public static Object execute(final String expression, final Map<String, Object> env) {

        compile(expression);

        return null;
    }

    public static void main(String[] args) {
        EventExpressionEvaluator.execute("e1 || e2");
    }

}
