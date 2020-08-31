package rule.engine.aviator.result;

import java.util.Map;

/**
 * 匹配结果
 * @author tao.yang
 * @date 2020-08-31
 */
public class Result {

    private String ruleId;

    private String ruleName;

    private Map<String, Object> result;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
