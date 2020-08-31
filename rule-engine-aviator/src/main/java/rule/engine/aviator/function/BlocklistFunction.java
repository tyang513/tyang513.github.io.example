package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;

import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public class BlocklistFunction extends AbstractRuleEngineFunction{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(BlocklistFunction.class);

    private AbstractAdapter adapter;

    public BlocklistFunction(AbstractAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String userId = FunctionUtils.getStringValue(arg1, env);
        if (adapter.blocklist(userId)){
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "blocklist";
    }
}
