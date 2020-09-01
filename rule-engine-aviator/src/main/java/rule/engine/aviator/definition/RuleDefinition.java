package rule.engine.aviator.definition;

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
    private String event;

    /**
     * aviator 表达式
     */
    private String when;

    private String then;

}
