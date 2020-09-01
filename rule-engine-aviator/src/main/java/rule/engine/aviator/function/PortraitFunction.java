package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;

import java.util.Map;

/**
 * 画像函数:判断某个用户是属于某个标签当中
 *
 * @author tao.yang
 * @date 2020-08-31
 */
public class PortraitFunction extends AbstractRuleEngineFunction {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PortraitFunction.class);

    /**
     * 适配器
     */
    private AbstractAdapter adapter;

    /**
     * 构造函数
     * @param adapter
     */
    public PortraitFunction(AbstractAdapter adapter){
        this.adapter = adapter;
    }

    /**
     * 判断某用户是否属于该标签或画像
     *
     * @param env
     * @param arg1 用户ID
     * @param arg2 画像ID
     * @return
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

        String userId = FunctionUtils.getStringValue(arg1, env);
        String portraitId = FunctionUtils.getStringValue(arg2, env);

        return AviatorBoolean.valueOf(adapter.portrait(userId, portraitId));
    }

    @Override
    public String getName() {
        return "portrait";
    }
}
