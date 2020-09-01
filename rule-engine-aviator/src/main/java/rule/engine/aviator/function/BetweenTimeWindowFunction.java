package rule.engine.aviator.function;

/**
 * @author tao.yang
 * @date 2020-09-01
 */
public class BetweenTimeWindowFunction extends AbstractRuleEngineFunction{

    /**
     * Get the function name
     *
     * @return
     */
    @Override
    public String getName() {
        return "BETWEEN";
    }
}
