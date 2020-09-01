package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * distinc 函数
 * @author tao.yang
 * @date 2020-08-31
 */
public class DistinctFunction extends AbstractRuleEngineFunction{

    private static final Logger logger = LoggerFactory.getLogger(DistinctFunction.class);

    /**
     * @param env
     * @param arg1
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Object obj1 =  arg1.getValue(env);
        if (obj1 instanceof List){
            Set set = new HashSet();
            List list = (List) obj1;
            set.addAll(list);
            return AviatorRuntimeJavaType.valueOf(set);
        }
        return AviatorNil.NIL;
    }

    @Override
    public String getName() {
        return "distinct";
    }
}
