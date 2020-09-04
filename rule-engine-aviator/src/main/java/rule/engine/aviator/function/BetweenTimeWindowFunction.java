package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class BetweenTimeWindowFunction extends AbstractRuleEngineFunction{

    /**
     *
     * @param env
     * @param arg1 用户Id
     * @param arg2 开始时间 必须是 日期 yyyy-MM-dd HH:mm:ss.SSS
     * @param arg3 结束时间 必须是 日期 yyyy-MM-dd HH:mm:ss.SSS
     * @param arg4 表达式 事件表达式
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3, AviatorObject arg4) {
        String userId = FunctionUtils.getStringValue(arg1, env);
        String startTime = FunctionUtils.getStringValue(arg2, env);
        String endTime = FunctionUtils.getStringValue(arg3, env);
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
        return "BETWEEN";
    }
}
