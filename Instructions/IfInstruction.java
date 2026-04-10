package Instructions;

import java.util.List;

import environment.Environment;
import Nodes.*;

public class IfInstruction implements Instruction {

    private final Expression       condition;
    private final List<Instruction> thenBody;
    private final List<Instruction> elseBody;   // null if no else clause

    public IfInstruction(Expression condition, List<Instruction> thenBody) {
        this(condition, thenBody, null);
    }

    public IfInstruction(Expression condition,
                         List<Instruction> thenBody,
                         List<Instruction> elseBody) {
        this.condition = condition;
        this.thenBody  = thenBody;
        this.elseBody  = elseBody;
    }

    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);

        if (!(result instanceof Boolean)) {
            throw new RuntimeException(
                "Runtime error: 'if' condition must be a comparison (e.g. x > 5), "
                + "but got: " + result);
        }

        if ((Boolean) result) {
            for (Instruction instr : thenBody) {
                instr.execute(env);
            }
        } else if (elseBody != null) {
            for (Instruction instr : elseBody) {
                instr.execute(env);
            }
        }
    }

    @Override
    public String toString() {
        return "IfInstruction(condition=" + condition
             + ", thenBody.size=" + thenBody.size()
             + (elseBody != null ? ", elseBody.size=" + elseBody.size() : "") + ")";
    }
}
