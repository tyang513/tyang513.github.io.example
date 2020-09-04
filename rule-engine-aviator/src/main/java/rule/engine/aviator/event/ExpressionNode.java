package rule.engine.aviator.event;

/**
 * @author tao.yang
 * @date 2020-09-04
 */
public abstract class ExpressionNode {

    /**
     * 节点类型
     */
    private String type;

    /**
     * 左侧节点
     */
    private ExpressionNode left;

    /**
     * 操作符
     */
    private String operator;

    /**
     * 右侧节点
     */
    private ExpressionNode right;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }
}
