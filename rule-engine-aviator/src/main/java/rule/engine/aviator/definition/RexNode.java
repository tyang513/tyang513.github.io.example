package rule.engine.aviator.definition;

/**
 * @author tao.yang
 * @date 2020-09-02
 */
public class RexNode {

    /**
     * 一元运算符，!
     */
    private String unaryOperator;

    private String field;

    /**
     * 二元运算符 -> ADN OR 等
     */
    private String binaryOperator;

    private RexNode nextRexNode;

    public RexNode(String unaryOperator, String field) {
        this.unaryOperator = unaryOperator;
        this.field = field;
    }

    public RexNode(String unaryOperator, String field, String binaryOperator){
        this.unaryOperator = unaryOperator;
        this.field = field;
        this.binaryOperator = binaryOperator;
    }

    public RexNode(String unaryOperator, String field, String binaryOperator, RexNode nextRexNode){
        this.unaryOperator = unaryOperator;
        this.field = field;
        this.binaryOperator = binaryOperator;
        this.nextRexNode = nextRexNode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getUnaryOperator() {
        return unaryOperator;
    }

    public void setUnaryOperator(String unaryOperator) {
        this.unaryOperator = unaryOperator;
    }

    public String getBinaryOperator() {
        return binaryOperator;
    }

    public void setBinaryOperator(String binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    public RexNode getNextRexNode() {
        return nextRexNode;
    }

    public void setNextRexNode(RexNode nextRexNode) {
        this.nextRexNode = nextRexNode;
    }


}
