package rule.engine.aviator.expression.parser;

import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rule.engine.aviator.expression.EventExpressionEvaluatorInstance;
import rule.engine.aviator.expression.code.IEventExpressionCodeGenerator;
import rule.engine.aviator.expression.lexer.EventExpressionLexer;
import rule.engine.aviator.expression.lexer.token.EventCharToken;
import rule.engine.aviator.expression.lexer.token.IEventToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件表达式解析器
 *
 * @author tao.yang
 * @date 2020-09-07
 */
public class EventExpressionParser implements IExpressionParser {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExpressionParser.class);

    /*
     * Lookhead token
     */
    private IEventToken<?> lookhead;

    /**
     * 前一个token
     */
    private IEventToken<?> prevToken;

    /**
     * 事件计算实例
     */
    private EventExpressionEvaluatorInstance evaluatorInstance;

    /**
     * 词法解析器
     */
    private EventExpressionLexer lexer;

    /**
     * 代码生成器
     */
    private IEventExpressionCodeGenerator codeGenerator;

    private int parsedTokens;

    private int getCodeGeneratorTimes;

    /**
     * 默认的构造函数
     *
     * @param evaluatorInstance 事件表达式计算实例
     * @param lexer             词法解析器
     * @param codeGenerator     代码生成方式
     */
    public EventExpressionParser(EventExpressionEvaluatorInstance evaluatorInstance, EventExpressionLexer lexer,
                                 IEventExpressionCodeGenerator codeGenerator) {
        this.evaluatorInstance = evaluatorInstance;
        this.lexer = lexer;
        this.lookhead = this.lexer.scan();
        this.codeGenerator = codeGenerator;
    }

    public void parse() {
        statements();
        if (this.lookhead != null) {
            reportSyntaxError("unexpect token '" + currentTokenLexeme() + "'");
        }
    }

    private void statements() {
        statement();
    }

    /**
     * 语句解析
     */
    public void statement() {
        // 首先判断是否是三元表达式
        ternary();
    }

    /**
     * ->
     */
    public void eventMove() {
        this.eventNot();
        while (true) {
            if (expectChar('-')) {

            } else {
                break;
            }
        }
    }

    /**
     * AND
     */
    public void eventAnd() {
        this.eventMove();
        while (true) {
            if (expectChar('&')) {

            } else {
                break;
            }
        }
    }

    /**
     * OR
     */
    public void eventJoin() {
        this.eventAnd();
        while (true) {
            if (expectChar('|')) {
                // 生成 left 代码
                getCodeGeneratorWithTimes();
                logger.info("left code : {}", lookhead);
                move(true);
                if (expectChar('|')) {
                    move(true);
                    // 生成 right 代码
                    this.eventAnd();
                    getCodeGeneratorWithTimes();
                    logger.info("right code : {}", lookhead);
                }
            } else {
                break;
            }
        }
    }

    /**
     * !
     */
    public void eventNot() {
        this.factor();
        while (true) {
            if (expectChar('!')) {

            } else {
                break;
            }
        }
    }

    /**
     * () 用于调整运算符优先级
     */
    public void factor() {

        move(true);
        IEventToken<?> prev = this.prevToken;
    }

    /**
     * 三元表达式
     *
     * @return
     */
    public boolean ternary() {
        this.eventJoin();
        return false;
    }


    private final IEventExpressionCodeGenerator getCodeGeneratorWithTimes() {
        this.getCodeGeneratorTimes++;
        return this.codeGenerator;
    }

    private String currentTokenLexeme() {
        return this.lookhead == null ? "END_OF_STRING" : this.lookhead.getLexeme();
    }

    private boolean expectChar(final char ch) {
        if (this.lookhead == null) {
            return false;
        }
        return this.lookhead.getType() == IEventToken.EventTokenType.Char && ((EventCharToken) this.lookhead).getCh() == ch;
    }

    /**
     * 报告语法错误
     *
     * @param message
     */
    public void reportSyntaxError(final String message) {
        int index = isValidLookhead() ? this.lookhead.getStartIndex() : this.lexer.getCurrentIndex();

        if (this.lookhead != null) {
            this.lexer.pushback(this.lookhead);
        }

        String msg = "Syntax error: " + message + //
                " at " + index + //
                ", lineNumber: " + this.lexer.getLineNo() + //
                ", token : " + //
                this.lookhead + ",\nwhile parsing expression: `\n" + //
                this.lexer.getScanString() + "^^^\n`";

        ExpressionSyntaxErrorException e = new ExpressionSyntaxErrorException(msg);
        StackTraceElement[] traces = e.getStackTrace();
        List<StackTraceElement> filteredTraces = new ArrayList<>();
        for (StackTraceElement t : traces) {
            filteredTraces.add(t);
        }
        e.setStackTrace(filteredTraces.toArray(new StackTraceElement[filteredTraces.size()]));
        throw e;
    }

    private boolean isValidLookhead() {
        return this.lookhead != null && this.lookhead.getStartIndex() > 0;
    }

    public void move(final boolean analyse) {
        if (this.lookhead != null) {
            this.prevToken = this.lookhead;
            this.lookhead = this.lexer.scan(analyse);
            if (this.lookhead != null) {
                this.parsedTokens++;
            }
        } else {
            reportSyntaxError("illegal token");
        }
    }

}
