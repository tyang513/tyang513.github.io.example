package rule.engine.aviator.expression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author tao.yang
 * @date 2020-09-07
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private static final String CURRENT_VERSION = "0.1.0";

    public static String getAviatorScriptVersion() {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = rule.engine.aviator.expression.Utils.class.getResourceAsStream("/META-INF/maven/com.googlecode.aviator/aviator/pom.properties");
            prop.load(in);
            return prop.getProperty("version", CURRENT_VERSION);
        } catch (Throwable e) {
            // ignore
            logger.warn("解析版本号出错", e);
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                // do nothing
                logger.warn("关闭输入流出错", ex);
            }
        }
        return CURRENT_VERSION;
    }

}
