package rule.engine.aviator.event;

import java.util.List;

/**
 * @author tao.yang
 * @date 2020-09-04
 */
public class EventExpressionStatement {

    private String type = "ExpressionStatement";

    private List<ExpressionNode> expressionBody;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ExpressionNode> getExpressionBody() {
        return expressionBody;
    }

    public void setExpressionBody(List<ExpressionNode> expressionBody) {
        this.expressionBody = expressionBody;
    }
}
