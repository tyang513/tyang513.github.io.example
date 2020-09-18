import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class ReversePolishNotationExampleTest {

    @org.junit.jupiter.api.Test
    void transfer() {
        String expression = "3+4-2";
        String actual = ReversePolishNotationExample.transfer(expression);
        String expected = "34+2-";
        Assert.assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void transferPriority() {
        String expression = "3+4-2"; // 5
        String actual = ReversePolishNotationExample.transferPriority(expression);
        String expected = "34+2-"; // 5
        Assert.assertEquals(expected, actual);

        expression = "9-(5+2)"; // 2
        actual = ReversePolishNotationExample.transferPriority(expression);
        expected = "952+-"; // 2
        Assert.assertEquals(expected, actual);

        expression = "3+4-((2+5)-6)+7-(9-8)"; // 12
        actual = ReversePolishNotationExample.transferPriority(expression);
        expected = "34+25+6--7+98--"; // 12 // "34+25+6-7+98---" 计算出来是0 这个值是错误的
        Assert.assertEquals(expected, actual);

        expression = "3+4-2+5-6+7-9-8"; // -6
        actual = ReversePolishNotationExample.transferPriority(expression);
        expected = "34+2-5+6-7+9-8-"; // -6
        Assert.assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void transferPriorityMultiplyAndDivide() {
        String expression = "3+4-2";
        String actual = ReversePolishNotationExample.transferPriorityMultiplyAndDivide(expression);
        String expected = "34+2-";
        Assert.assertEquals(expected, actual);

        expression = "9-(5+2)";
        actual = ReversePolishNotationExample.transferPriorityMultiplyAndDivide(expression);
        expected = "952+-";
        Assert.assertEquals(expected, actual);

        expression = "3+4-((2+5)-6)+7-(9-8)";
        actual = ReversePolishNotationExample.transferPriorityMultiplyAndDivide(expression);
        expected = "34+25+6--7+98--";
        Assert.assertEquals(expected, actual);

        expression = "3+4-2+5-6+7-9-8";
        actual = ReversePolishNotationExample.transferPriorityMultiplyAndDivide(expression);
        expected = "34+2-5+6-7+9-8-";
        Assert.assertEquals(expected, actual);
    }
}