package com.talkingdata.framework.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkingdata.framework.gateway.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * JSON工具转换类
 * @author tao.yang
 * @date 2019-11-15
 */
public class ObjectMapperUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtils.class);

    /**
     * json转换
     */
    private static final ObjectMapper mapper = new ObjectMapper();
    {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
    }

    /**
     * 私有的构造函数,该类静态方法类
     */
    private ObjectMapperUtils(){
        // do nothing
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String object2String(Object value) throws CommonException {
        try {
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            logger.error("对象json转换异常", e);
            throw new CommonException(e);
        }
    }

}
