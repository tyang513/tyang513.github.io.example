import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author tao.yang
 * @date 2020-08-18
 */
public class Default {

    public static void main(String[] args) {
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('c');
        operatorStack.push('a');

        System.out.println(operatorStack.peek());
        System.out.println(operatorStack.peek());
        System.out.println(operatorStack.peek());
        System.out.println(operatorStack.peek());

        Map<String, String> map = new HashMap<>();
        map.keySet();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getKey();
        }
    }
}
