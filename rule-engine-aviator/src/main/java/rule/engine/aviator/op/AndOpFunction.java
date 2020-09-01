package rule.engine.aviator.op;

import rule.engine.aviator.function.AbstractRuleEngineFunction;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class AndOpFunction extends AbstractRuleEngineFunction {
    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return "AND";
    }
}
