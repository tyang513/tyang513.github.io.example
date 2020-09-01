package rule.engine.aviator.function;


import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class SelectFunction extends AbstractRuleEngineFunction {

    private static final Logger logger = LoggerFactory.getLogger(SelectFunction.class);

    /**
     *
     * @param env 环境
     * @param arg1 函数
     * @param arg2 时间窗口函数返回数据
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {



        return super.call(env, arg1, arg2);
    }

    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return null;
    }
}
