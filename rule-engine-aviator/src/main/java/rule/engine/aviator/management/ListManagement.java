package rule.engine.aviator.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.adapter.AbstractAdapter;
import rule.engine.aviator.adapter.MemoryAdapter;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public class ListManagement {

    private static final Logger logger = LoggerFactory.getLogger(ListManagement.class);

    private static  ListManagement instance;

    private AbstractAdapter adapter = new MemoryAdapter();

    private ListManagement(){
        // do nothing
    }

    public static ListManagement getInstance() {
        if (instance == null){
            instance = new ListManagement();
        }
        return instance;
    }

    public String getAllowlist(){
        
        return null;
    }

}
