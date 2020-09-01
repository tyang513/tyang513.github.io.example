package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 暂时可以使用aviator自带的count进行
 * @author tao.yang
 * @date 2020-08-31
 */
@Deprecated
public class CCountFunction extends AbstractRuleEngineFunction{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CCountFunction.class);

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Collector collector = null;
        return super.call(env, arg1);
    }

    @Override
    public String getName() {
        return "ccount";
    }
}
