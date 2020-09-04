package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class AtTimeWindowFunction extends AbstractRuleEngineFunction {

    /**
     *
     * @param env
     * @param arg1 用户Id
     * @param arg2 时间 yyyy-MM-dd HH:mm:ss.SSS 最小为分钟级
     * @param arg3 事件表达式
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {

        String userId = FunctionUtils.getStringValue(arg1, env);
        String time = FunctionUtils.getStringValue(arg2, env);
        String eventExpression = FunctionUtils.getStringValue(arg3, env);
        return super.call(env, arg1, arg2);
    }

    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return "AT";
    }
}
