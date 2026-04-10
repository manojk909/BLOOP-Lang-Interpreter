package Instructions;

import java.util.List;

import environment.Environment;
import Nodes.*;
public class RepeatInstruction implements Instruction {

    private final Expression        countExpression;
    private final List<Instruction> body;

    public RepeatInstruction(Expression countExpression, List<Instruction> body) {
        this.countExpression = countExpression;
        this.body            = body;
    }

    @Override
    public void execute(Environment env) {
        Object countVal = countExpression.evaluate(env);

        if (!(countVal instanceof Double)) {
            throw new RuntimeException(
                "Runtime error: 'repeat' count must be a number, but got: \"" + countVal + "\"");
        }

        double countDouble = (Double) countVal;

        if (Double.isInfinite(countDouble) || Double.isNaN(countDouble)
                || countDouble != Math.floor(countDouble)) {
            throw new RuntimeException(
                "Runtime error: 'repeat' count must be a whole number, but got: " + countDouble);
        }

        int count = (int) countDouble;

        if (count < 0) {
            throw new RuntimeException(
                "Runtime error: 'repeat' count cannot be a negative number, but got: " + count);
        }

        for (int i = 0; i < count; i++) {
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
    }

    @Override
    public String toString() {
        return "RepeatInstruction(count=" + countExpression
             + ", body.size=" + body.size() + ")";
    }
}
