import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.lexer.token.OperatorType;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;
import rule.engine.aviator.adapter.MemoryAdapter;
import rule.engine.aviator.function.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        AbstractAdapter adapter = new MemoryAdapter();

        // AviatorEvaluator.getInstance().addOpFunction(OperatorType.SHIFT_RIGHT, new AfterOpFunction());
        // 函数注册
        AviatorEvaluator.addFunction(new AllowlistFunction(adapter));
        AviatorEvaluator.addFunction(new BlocklistFunction(adapter));
        AviatorEvaluator.addFunction(new CCountFunction());
        AviatorEvaluator.addFunction(new DistinctFunction());
        AviatorEvaluator.addFunction(new PortraitFunction(adapter));
        AviatorEvaluator.addFunction(new SequenceFunction());
        AviatorEvaluator.addFunction(new TimeWindowFunction(adapter));

//        for (int i = 0; i < 10; i++){
//            System.out.println(UUID.randomUUID().toString().toLowerCase());
//        }
//        String timeWindowFunctionHours = "count(distinct(timeWindow('1dea7fb70053436f9652403c3107e925', '2020-09-01 06:24:00.456', 'event_1', '2', 'HOURS')))";
//        String timeWindowFunctionMINUTES = "timeWindow('1e8393b91b484ef68059ab186f3cd4c9', '2020-09-01 11:02:00.456', 'event_a', '5', 'MINUTES')";
//
//        Object evaluatorObject = AviatorEvaluator.execute(timeWindowFunctionHours);
//        if (evaluatorObject instanceof List){
//            List list = (List) evaluatorObject;
//            System.out.println(list.size());
//        } else if (evaluatorObject instanceof Long){
//            System.out.println(evaluatorObject);
//        }
//        System.out.println(OperatorType.DIV);
//        System.out.println(OperatorType.DIV.token);
//
//        System.out.println("d6a5d877-598c-4f20-8219-5acd83f2dc3a".length());
//
//        System.out.println((1387263000 << (64-41)) | (1341 << (64 - 41 - 13)) |  (5001 % 1024));
//
//        try {
//            System.out.println(DateUtils.parseDate("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        System.out.println(AviatorEvaluator.execute("(32 + 2) * 2"));
    }

}
