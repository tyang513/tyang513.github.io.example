package rule.engine.aviator.adapter;

import java.util.List;

/**
 * @author tao.yang
 * @date 2020-08-31
 */
public abstract class AbstractAdapter {

    /**
     * 允许名单
     * @param userId
     * @return
     */
    public abstract boolean allowlist(String userId);

    /**
     * 阻止名单
     * @param userId
     * @return
     */
    public abstract boolean blocklist(String userId);

}
