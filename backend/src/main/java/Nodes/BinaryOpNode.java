package Nodes;

import environment.Environment;

public class BinaryOpNode implements Expression {

    private final Expression left;
    private final String     operator;
    private final Expression right;

    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left     = left;
        this.operator = operator;
        this.right    = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal  = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        switch (operator) {

            case "+":
                if (leftVal instanceof String || rightVal instanceof String) {
                    return formatValue(leftVal) + formatValue(rightVal);
                }
                return toDouble(leftVal, "left side of '+'") + toDouble(rightVal, "right side of '+'");

            case "-":
                return toDouble(leftVal, "left side of '-'") - toDouble(rightVal, "right side of '-'");

            case "*":
                return toDouble(leftVal, "left side of '*'") * toDouble(rightVal, "right side of '*'");

            case "/":
                double divisor = toDouble(rightVal, "right side of '/'");
                if (divisor == 0.0) {
                    throw new RuntimeException("Runtime error: division by zero.");
                }
                return toDouble(leftVal, "left side of '/'") / divisor;

            case ">":
                return toDouble(leftVal, "left side of '>'") > toDouble(rightVal, "right side of '>'");

            case "<":
                return toDouble(leftVal, "left side of '<'") < toDouble(rightVal, "right side of '<'");

            case ">=":
                return toDouble(leftVal, "left side of '>='") >= toDouble(rightVal, "right side of '>='");

            case "<=":
                return toDouble(leftVal, "left side of '<='") <= toDouble(rightVal, "right side of '<='");

            case "==":
                if (leftVal instanceof Double && rightVal instanceof Double) {
                    return Double.compare((Double) leftVal, (Double) rightVal) == 0;
                }
                return formatValue(leftVal).equals(formatValue(rightVal));

            case "!=":
                if (leftVal instanceof Double && rightVal instanceof Double) {
                    return Double.compare((Double) leftVal, (Double) rightVal) != 0;
                }
                return !formatValue(leftVal).equals(formatValue(rightVal));

            default:
                throw new RuntimeException("Unknown operator: '" + operator + "'");
        }
    }

    private double toDouble(Object val, String context) {
        if (val instanceof Double) return (Double) val;
        throw new RuntimeException(
            "Type error: " + context + " must be a number, but got: \"" + val + "\"");
    }

    private String formatValue(Object val) {
        if (val instanceof Double) {
            double d = (Double) val;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(val);
    }

    @Override
    public String toString() {
        return "BinaryOpNode(" + left + " " + operator + " " + right + ")";
    }
}
