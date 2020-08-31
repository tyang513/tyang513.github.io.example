import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;
import rule.engine.aviator.adapter.MemoryAdapter;
import rule.engine.aviator.function.*;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        AbstractAdapter adapter = new MemoryAdapter();

        // 函数注册
        AviatorEvaluator.addFunction(new AllowlistFunction(adapter));
        AviatorEvaluator.addFunction(new BlocklistFunction(adapter));
        AviatorEvaluator.addFunction(new CCountFunction());
        AviatorEvaluator.addFunction(new DistinctFunction());
        AviatorEvaluator.addFunction(new PortraitFunction());
        AviatorEvaluator.addFunction(new SequenceFunction());
        AviatorEvaluator.addFunction(new TimeWindowFunction());

        System.out.println(AviatorEvaluator.execute("blocklist('12f2823bfa8645dd9e9257f007c5bbf4')"));
    }

}
