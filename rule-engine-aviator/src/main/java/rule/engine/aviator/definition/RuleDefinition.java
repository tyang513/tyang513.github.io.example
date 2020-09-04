package rule.engine.aviator.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class RuleDefinition {

    private String ruleId;

    private String ruleName;

    /**
     * 自定义表达式，需要转换成 aviator表达式。 例如：e1 -> (e2 AND !e3) -> e4
     */
    private List<RexNode> eventList;

    /**
     * aviator 表达式
     */
    private String when;

    private String then;

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

    public List<RexNode> getEventList() {
        return eventList;
    }

    public void setEventList(List<RexNode> eventList) {
        this.eventList = eventList;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getThen() {
        return then;
    }

    public void setThen(String then) {
        this.then = then;
    }

    public static void main(String[] args) {
        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setRuleId("100001");
        ruleDefinition.setRuleName("用户进入搜索后，反复浏览宝贝详情，但没有下单就退出搜索，发送PUSH消息领红包。");

        // e1 -> (e2 AND !e3) -> e4
        List<RexNode> eventList = new ArrayList<>();
        eventList.add(new RexNode(null, "e1", "->"));
        eventList.add(new RexNode(null, "e2", "ADN", new RexNode("!", "e3")));

    }



}
