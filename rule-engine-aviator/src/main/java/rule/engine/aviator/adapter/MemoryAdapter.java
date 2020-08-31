package rule.engine.aviator.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public class MemoryAdapter extends AbstractAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MemoryAdapter.class);

    private String basePath = "/Users/yangtao/workspace/website.example/rule-engine-aviator/src/main/resources/";

    private List<String> allowlist = null;

    private List<String> blocklist = null;



    /**
     * 允许名单
     *
     * @return
     */
    @Override
    public boolean allowlist(String userId)  {
        if (allowlist == null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> allowlistMap = mapper.readValue(new File(basePath + "/allowlist.json"), Map.class);
                allowlist = (List<String>) allowlistMap.get("allowlist");
            } catch (IOException e) {
                logger.error("允许名单加载出错", e);
            }
        }
        if (allowlist == null && allowlist.isEmpty()){
            return true;
        }
        return allowlist.contains(userId);
    }

    /**
     * 阻止名单
     *
     * @return
     */
    @Override
    public boolean blocklist(String userId) {

        if (blocklist == null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> allowlistMap = mapper.readValue(new File(basePath + "/blocklist.json"), Map.class);
                blocklist = (List<String>) allowlistMap.get("allowlist");
            } catch (IOException e) {
                logger.error("允许名单加载出错", e);
            }
        }
        if (blocklist == null && blocklist.isEmpty()){
            return true;
        }
        return blocklist.contains(userId);
    }

}
