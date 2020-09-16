package dargon.ch2;

import java.io.IOException;

/**
 * @author tao.yang
 * @date 2020-09-10
 */
public class Parser {

    private static int lookahead;

    public Parser() throws IOException {
        lookahead = System.in.read();
    }

    void expr() throws Exception {
        term();
        while (true) {
            if (lookahead == '+') {
                match('+');
                term();
                System.out.write('+');
            } else if (lookahead == '-') {
                match('-');
                term();
                System.out.write('-');
            } else {
                break;
            }
        }
    }

    void term() throws Exception {
        if (Character.isDigit((char) lookahead)) {
            System.out.write((char) lookahead);
            match(lookahead);
        } else {
            throw new Exception("Syntax Error");
        }
    }

    void match(int t) throws Exception {
        if (lookahead == t) {
            lookahead = System.in.read();
        } else {
            throw new Exception("Syntax Error");
        }
    }

    public static class Postfix {
        public static void main(String[] args) throws Exception {
            Parser parser = new Parser();
            parser.expr();
            System.out.write('\n');
        }
    }

}
