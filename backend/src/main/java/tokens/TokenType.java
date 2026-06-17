package tokens;

public enum TokenType {

    // Keywords
    PUT,
    INTO,
    PRINT,
    IF,
    THEN,
    ELSE,
    REPEAT,
    TIMES,

    // Literals
    NUMBER,
    STRING,

    // Identifiers
    IDENTIFIER,

    // Arithmetic Operators
    PLUS,
    MINUS,
    STAR,
    SLASH,

    // Comparison Operators
    GREATER,
    LESS,
    GREATER_EQ,
    LESS_EQ,
    EQUAL_EQUAL,
    NOT_EQUAL,

    // Structure
    COLON,
    NEWLINE,
    EOF
}
