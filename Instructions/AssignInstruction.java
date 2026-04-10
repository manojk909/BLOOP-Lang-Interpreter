package Instructions;

import environment.Environment;
import Nodes.*;

public class AssignInstruction implements Instruction {

    private final String     variableName;
    private final Expression expression;

    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression   = expression;
    }

    @Override
    public void execute(Environment env) {
        Object value = expression.evaluate(env);
        env.set(variableName, value);
    }

    @Override
    public String toString() {
        return "AssignInstruction(" + variableName + " = " + expression + ")";
    }
}
