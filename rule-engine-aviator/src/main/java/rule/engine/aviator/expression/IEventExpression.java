package rule.engine.aviator.expression;

import java.util.List;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-08
 */
public interface IEventExpression {

    /**
     * Execute expression with environment
     *
     * @param env
     *            Binding variable environment
     * @return
     */
    public abstract Object execute(Map<String, Object> env);

    /**
     * Execute expression with empty environment
     *
     * @return
     */
    public abstract Object execute();

    /**
     * Returns this expression's all variable names in order when using
     * AviatorEvaluator.EVAL mode,else returns empty set
     *
     * @see com.googlecode.aviator.AviatorEvaluator#EVAL
     * @return
     */
    public List<String> getVariableNames();

}
