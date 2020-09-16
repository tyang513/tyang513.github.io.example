package tree.binary;

/**
 * 二叉树实体
 * @author tao.yang
 * @date 2020-09-14
 */
public class BinaryTreeEntity {

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点值
     */
    private String value;

    /**
     * 左节点
     */
    private BinaryTreeEntity left;

    /**
     * 右节点
     */
    private BinaryTreeEntity right;

    /**
     * 父节点
     */
    private BinaryTreeEntity parent;

    public BinaryTreeEntity(String name, String value) {
        this(name, value, null);
    }

    /**
     * 构造函数
     * @param name
     * @param value
     * @param parent
     */
    public BinaryTreeEntity(String name, String value, BinaryTreeEntity parent) {
        this(name, value, null, null, parent);
    }

    /**
     * 构造函数
     * @param name
     * @param value
     * @param left
     * @param right
     * @param parent
     */
    public BinaryTreeEntity(String name, String value, BinaryTreeEntity left, BinaryTreeEntity right,
                            BinaryTreeEntity parent) {
        this.name = name;
        this.value = value;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BinaryTreeEntity getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeEntity left) {
        this.left = left;
    }

    public BinaryTreeEntity getRight() {
        return right;
    }

    public void setRight(BinaryTreeEntity right) {
        this.right = right;
    }

    public BinaryTreeEntity getParent() {
        return parent;
    }

    public void setParent(BinaryTreeEntity parent) {
        this.parent = parent;
    }
}

