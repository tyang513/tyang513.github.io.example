package rule.engine.aviator.function;

import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;
import rule.engine.aviator.utils.DateUtils;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 时间窗口
 * 根据事件的发生时间进行窗口的划分，窗口大小最小为分钟级别。
 * timeWindow使用K/V存储进行存储,窗口的Key为: userId_eventCode_time(time 需要根据事件发生时间的(分钟)%窗口的大小)
 * DAYS
 * HOURS
 * MINUTES
 *
 * @author tao.yang
 * @date 2020-08-31
 */
public class TimeWindowFunction extends AbstractRuleEngineFunction {

    private static final Logger logger = LoggerFactory.getLogger(TimeWindowFunction.class);

    private AbstractAdapter adapter;

    /**
     *
     * @param adapter
     */
    public TimeWindowFunction(AbstractAdapter adapter){
        this.adapter = adapter;
    }

    /**
     * 判断某事件在时间窗口内的次数
     * @param env  上下文环境
     * @param arg1 用户Id
     * @param arg2 事件发生时间
     * @param arg3 事件编码
     * @param arg4 时间窗口,如果是小时，则不能超过24小时，如果是分钟，则不能超过 60分钟
     * @param arg5 HOURS MINUTES
     * @return Sequence 返回时间窗口内的数据集合
     */
    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1, final AviatorObject arg2,
                              final AviatorObject arg3, final AviatorObject arg4, final AviatorObject arg5) {

        String userId = FunctionUtils.getStringValue(arg1, env);
        String eventTime = FunctionUtils.getStringValue(arg2, env);
        String eventId = FunctionUtils.getStringValue(arg3, env);
        int timeWindowSize = Integer.valueOf(FunctionUtils.getStringValue(arg4, env));
        String timeUnit = FunctionUtils.getStringValue(arg5, env);

        Calendar calendar = null;
        try {
            Date eventTimestamp = DateUtils.toDate(eventTime, "yyyy-MM-dd HH:mm:ss.SSS");
            calendar = Calendar.getInstance();
            calendar.setTime(eventTimestamp);
        } catch (ParseException e) {
            String message = "timeWindow 函数传入的参数 "+ eventTime + " 日期格式不为 yyyy-MM-dd HH:mm:ss.SSS";
            logger.error(message, e);
            throw new IllegalArgumentException("timeWindow 函数传入的参数 "+ eventTime + " 日期格式不为 yyyy-MM-dd HH:mm:ss.SSS", e);
        }

        String timeWindowId = null;
        if (TimeUnit.HOURS.equals(TimeUnit.valueOf(timeUnit))){
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int windowOffset = hours / timeWindowSize;
            timeWindowId = userId + "_" + eventId + "_" + windowOffset + "_" + timeUnit;
        }
        else if (TimeUnit.MINUTES.equals(TimeUnit.valueOf(timeUnit))){
            int minutes = calendar.get(Calendar.MINUTE);
            int windowOffset = minutes / timeWindowSize;
            timeWindowId = userId + "_" + eventId + "_" + windowOffset + "_" + timeUnit;
        } else {
            throw new IllegalArgumentException("timeWindow 函数传入的参数 timeUnit  单位不对,只能是 HOURS MINUTES ");
        }

        logger.info("timeWindow id = {}", timeWindowId);

        List<String> returnList = adapter.timeWindow(timeWindowId, eventId);
        return AviatorRuntimeJavaType.valueOf(returnList);
    }

    @Override
    public String getName() {
        return "timeWindow";
    }
}
