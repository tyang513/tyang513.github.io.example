import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

/**
 * 后缀表示法
 *
 * @author tao.yang
 * @date 2020-09-17
 */
public class ReversePolishNotationExample {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ReversePolishNotationExample.class);

    /**
     * 运算符
     */
    private static final char[] OPS = {'+', '-', '*', '/'};

    /**
     * 翻译表达式,只支持 + - 或 * / 。并且不支持使用括号调整优先级
     *
     * @return
     */
    public static String transfer(String expression) {
        CharacterIterator iterator = new StringCharacterIterator(expression);
        Stack<Character> operatorStack = new Stack<>();
        Stack<Character> temporaryResultStack = new Stack<>();
        for (char peek = iterator.current(); ; peek = iterator.next()) {
            // 结尾跳出
            if (peek == CharacterIterator.DONE) {
                break;
            }
            // 过滤空格,制表符，换行、回车字符
            if (peek == ' ' || peek == '\t' || peek == '\r' || peek == '\n') {
                continue;
            }
            if (Character.isDigit(peek)) {
                Character operator = operatorStack.size() != 0 ? operatorStack.pop() : null;
                temporaryResultStack.push(peek);
                if (operator != null) {
                    temporaryResultStack.push(operator);
                }
            }
            if (isBinaryOP(peek)) {
                operatorStack.push(peek);
            }
        }

        StringBuffer sb = new StringBuffer();
        while (temporaryResultStack.size() > 0) {
            char c = temporaryResultStack.pop();
            sb.append(c);
        }

        return sb.reverse().toString();
    }

    /**
     * 翻译表达式,支持 + - 或 * / 并且支持括号调整优先级
     *
     * @return
     */
    public static String transferPriority(String expression) {
        CharacterIterator iterator = new StringCharacterIterator(expression);
        Stack<Character> operatorStack = new Stack<>();
        Stack<Character> temporaryResultStack = new Stack<>();

        for (char peek = iterator.current(); ; peek = iterator.next()) {
            // 结尾跳出
            if (peek == CharacterIterator.DONE) {
                break;
            }
            // 过滤空格,制表符，换行、回车字符
            if (peek == ' ' || peek == '\t' || peek == '\r' || peek == '\n') {
                continue;
            }
            if (Character.isDigit(peek)) {
                temporaryResultStack.push(peek);
                Character operator = operatorStack.size() != 0 ? operatorStack.peek() : null;
                // 这里写错了 TODO
                if (operator != null && operator != '(') {
                    char oper = operatorStack.pop();
                    temporaryResultStack.push(oper);
                }
            }

            // 操作符 和 括号判断
            if (isBinaryOP(peek) || peek == '(' || peek == ')') {
                Character operator = operatorStack.size() != 0 ? operatorStack.peek() : null;
                if (operator == null || peek == '(') {
                    // 如果栈为空或 栈顶为 左括号，直接入栈
                    operatorStack.push(peek);
                } else if (operator != null && peek == ')') {
                    // 当前字符为 右括号，取出操作符，直到 ')' 为止，丢弃当前括号
                    while (operatorStack.size() > 0) {
                        char c = operatorStack.pop();
                        if (c == '(') {
                            break;
                        }
                        temporaryResultStack.push(c);
                    }
                } else {
                    operatorStack.push(peek);
                }
            }
        }

        // 将剩余的操作符加入临时堆栈
        while (operatorStack.size() > 0) {
            char c = operatorStack.pop();
            temporaryResultStack.push(c);
        }

        // 翻转
        StringBuffer sb = new StringBuffer();
        while (temporaryResultStack.size() > 0) {
            char c = temporaryResultStack.pop();
            sb.append(c);
        }
        return sb.reverse().toString();
    }

    /**
     * 翻译表达式,支持 + - 或 * / 并且支持括号调整优先级
     *
     * @return
     */
    public static String transferPriorityMultiplyAndDivide(String expression) {
        CharacterIterator iterator = new StringCharacterIterator(expression);
        Stack<Character> operatorStack = new Stack<>();
        Stack<Character> temporaryResultStack = new Stack<>();

        for (char peek = iterator.current(); ; peek = iterator.next()) {
            // 结尾跳出
            if (peek == CharacterIterator.DONE) {
                break;
            }
            // 过滤空格,制表符，换行、回车字符
            if (peek == ' ' || peek == '\t' || peek == '\r' || peek == '\n') {
                continue;
            }

            // 数字直接入栈
            if (Character.isDigit(peek)) {
                temporaryResultStack.push(peek);
            }

            // 操作符 和 括号判断
            if (isBinaryOP(peek) || peek == '(' || peek == ')') {
                Character operator = operatorStack.size() != 0 ? operatorStack.peek() : null;

                // 如果栈为空或 栈顶为 左括号，直接入栈
                if (operator == null || peek == '(') {
                    operatorStack.push(peek);
                }
                // 当前字符为 右括号，取出操作符，直到 ')' 为止，丢弃当前括号
                else if (operator != null && peek == ')') {

                    while (operatorStack.size() > 0) {
                        char c = operatorStack.pop();
                        if (c == '(') {
                            break;
                        }
                        temporaryResultStack.push(c);
                    }
                }
                // 如果栈顶有操作符则取出，放入中间结果栈。同时，将当前运算符放到operatorStack
                else if (operator != null) {
                    char oper = operatorStack.peek();
                    if (oper != '(') {
                        oper = operatorStack.pop();
                        temporaryResultStack.push(oper);
                    }
                    operatorStack.push(peek);
                }
                // 其它情况直接入栈 operatorStack
                else {
                    operatorStack.push(peek);
                }
            }
        }

        // 将剩余的操作符加入临时堆栈
        while (!operatorStack.isEmpty()) {
            char c = operatorStack.pop();
            temporaryResultStack.push(c);
        }

        // 翻转
        StringBuffer sb = new StringBuffer();
        while (!temporaryResultStack.isEmpty()) {
            char c = temporaryResultStack.pop();
            sb.append(c);
        }
        return sb.reverse().toString();
    }

    /**
     * 是否为操作符号
     *
     * @param ch
     * @return
     */
    public static boolean isBinaryOP(final char ch) {
        for (char tmp : OPS) {
            if (tmp == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * 运算符的优先级比较
     *
     * @param op1
     * @param op2
     * @return 返回 1 则 op1 < op2
     * 若返回 0 则 op1 = op2
     * 若返回 -1,则 op1 > op2
     */
    private static int operatorPriorityCompare(char op1, char op2) {
        switch (op1) {
            case '+':
            case '-':
                return op2 == '*' || op2 == '/' ? 1 : 0;
            case '*':
            case '/':
                return op2 == '+' || op2 == '-' ? -1 : 0;
        }
        return 0;
    }


    public static void main(String[] args) {
        String expression = "3+4-((2+5)-6)+7-(9-8)";
        String actual = ReversePolishNotationExample.transferPriority(expression);
        String expected = "34+25+6--7+98--";

        logger.info("expression = {} actual = {} expected = {}", expression, actual, expected);

    }
}
