package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class WithinTimeWindowFunction extends AbstractRuleEngineFunction {

    private static final Logger logger = LoggerFactory.getLogger(WithinTimeWindowFunction.class);

    /**
     *
     * @param env
     * @param arg1 用户id
     * @param arg2 数字 窗口大小
     * @param arg3 时间单位 HOURS 或 MINUTES
     * @param arg4 事件表达式
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3, AviatorObject arg4) {

        String userId = (String) arg1.getValue(env);
        int windowSize = (int) arg2.getValue(env);
        String timeUnit = (String) arg3.getValue(env);
        String eventExpression = FunctionUtils.getStringValue(arg4, env);

        return super.call(env, arg1, arg2, arg3, arg4);
    }

    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return "WITHIN";
    }
}
