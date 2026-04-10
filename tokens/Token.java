package tokens;

public class Token {

    private final TokenType type;
    private final String    value;
    private final int       line;
    private final int       indentLevel;

    public Token(TokenType type, String value, int line, int indentLevel) {
        this.type        = type;
        this.value       = value;
        this.line        = line;
        this.indentLevel = indentLevel;
    }

    public Token(TokenType type, String value, int line) {
        this(type, value, line, 0);
    }

    public TokenType getType()       { return type; }
    public String    getValue()      { return value; }
    public int       getLine()       { return line; }
    public int       getIndentLevel(){ return indentLevel; }

    @Override
    public String toString() {
        return "Token(" + type + ", \"" + value
             + "\", line=" + line + ", indent=" + indentLevel + ")";
    }
}
