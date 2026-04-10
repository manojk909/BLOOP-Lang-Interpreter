package Instructions;

import environment.Environment;
import Nodes.*;

public class PrintInstruction implements Instruction {

    private final Expression expression;

    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment env) {
        Object value = expression.evaluate(env);
        System.out.println(formatValue(value));
    }

    private String formatValue(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return "PrintInstruction(" + expression + ")";
    }
}
