package rule.engine.aviator.op;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.event.Event;

import java.util.List;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class AfterOpFunction extends AbstractFunction {

    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(AfterOpFunction.class);

    /**
     * 默认的构造函数
     */
    public AfterOpFunction() {
        // do nothing
    }

    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1, final AviatorObject arg2, final AviatorObject arg3) {
        Event event1 = (Event) arg1.getValue(env);
        Event event2 = (Event) arg2.getValue(env);
        List<Object> eventList = (List<Object>) arg3.getValue(env);

        // 2 表示两个参数
        AviatorFunction function = FunctionUtils.getFunction(arg1, env, 2);

        return AviatorBoolean.TRUE;
    }

    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return "after";
    }
}
