package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;

import java.util.Map;

/**
 * 允许名单函数
 * @author tao.yang
 * @date 2020-08-31
 */
public class AllowlistFunction extends AbstractRuleEngineFunction{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AllowlistFunction.class);

    private AbstractAdapter adapter;

    public AllowlistFunction(AbstractAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String s = FunctionUtils.getStringValue(arg1, env);
        if (adapter.allowlist(s)){
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "allowlist";
    }


}
