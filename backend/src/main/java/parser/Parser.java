package parser;

import Instructions.*;
import tokens.Token;
import tokens.TokenType;
import Nodes.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens  = tokens;
        this.current = 0;
    }

    // ── Public entry point ────────────────────────────────────────────────────

    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();
        skipNewlines();
        while (!isAtEnd()) {
            Instruction instr = parseInstruction();
            if (instr != null) instructions.add(instr);
            skipNewlines();
        }
        return instructions;
    }

    // ── Instruction parsers ───────────────────────────────────────────────────

    private Instruction parseInstruction() {
        Token token = peek();
        switch (token.getType()) {
            case PUT:    return parseAssign();
            case PRINT:  return parsePrint();
            case IF:     return parseIf();
            case REPEAT: return parseRepeat();
            case NEWLINE:
                advance();
                return null;
            default:
                throw new RuntimeException(
                    "Syntax error on line " + token.getLine()
                    + ": unexpected token '" + token.getValue()
                    + "'. Expected: put, print, if, or repeat.");
        }
    }

    // put <expression> into <variableName>
    private Instruction parseAssign() {
        Token putToken = consume(TokenType.PUT, "Expected 'put'");
        Expression valueExpr = parseComparison();
        consume(TokenType.INTO,
            "Syntax error on line " + putToken.getLine()
            + ": expected 'into' after the value. Correct syntax: put <value> into <variable>");
        Token varToken = consume(TokenType.IDENTIFIER,
            "Syntax error on line " + putToken.getLine()
            + ": expected a variable name after 'into'");
        consumeNewlineOrEOF();
        return new AssignInstruction(varToken.getValue(), valueExpr);
    }

    // print <expression>
    private Instruction parsePrint() {
        consume(TokenType.PRINT, "Expected 'print'");
        Expression expr = parseComparison();
        consumeNewlineOrEOF();
        return new PrintInstruction(expr);
    }

    // if <comparison> then: block (else: block)?
    private Instruction parseIf() {
        Token ifToken    = consume(TokenType.IF, "Expected 'if'");
        int headerIndent = ifToken.getIndentLevel();

        Expression condition = parseComparison();
        consume(TokenType.THEN,
            "Syntax error on line " + ifToken.getLine()
            + ": expected 'then' after the condition");
        if (check(TokenType.COLON)) advance();
        consumeNewlineOrEOF();

        List<Instruction> thenBody = parseBlock(headerIndent);

        List<Instruction> elseBody = null;
        if (check(TokenType.ELSE) && peek().getIndentLevel() == headerIndent) {
            advance();
            if (check(TokenType.COLON)) advance();
            consumeNewlineOrEOF();
            elseBody = parseBlock(headerIndent);
        }

        return new IfInstruction(condition, thenBody, elseBody);
    }

    // repeat <expression> times: block
    private Instruction parseRepeat() {
        Token repeatToken = consume(TokenType.REPEAT, "Expected 'repeat'");
        int headerIndent  = repeatToken.getIndentLevel();

        Expression countExpr = parseComparison();
        consume(TokenType.TIMES,
            "Syntax error on line " + repeatToken.getLine()
            + ": expected 'times' after the count");
        if (check(TokenType.COLON)) advance();
        consumeNewlineOrEOF();

        List<Instruction> body = parseBlock(headerIndent);
        return new RepeatInstruction(countExpr, body);
    }

    // Parse indented block body
    private List<Instruction> parseBlock(int headerIndent) {
        List<Instruction> block = new ArrayList<>();
        skipNewlines();

        while (!isAtEnd()) {
            Token next = peek();
            if (next.getType() == TokenType.EOF)       break;
            if (next.getIndentLevel() <= headerIndent)  break;

            Instruction instr = parseInstruction();
            if (instr != null) block.add(instr);
            skipNewlines();
        }

        return block;
    }

    // ── Expression parsers (precedence enforced by call depth) ────────────────

    // comparison → expression  OPERATOR  expression
    private Expression parseComparison() {
        Expression left = parseExpression();

        if (check(TokenType.GREATER)    || check(TokenType.LESS)
                || check(TokenType.GREATER_EQ) || check(TokenType.LESS_EQ)
                || check(TokenType.EQUAL_EQUAL) || check(TokenType.NOT_EQUAL)) {
            Token op    = advance();
            Expression right = parseExpression();
            return new BinaryOpNode(left, op.getValue(), right);
        }
        return left;
    }

    // expression → term  (('+' | '-') term)*
    private Expression parseExpression() {
        Expression left = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            Token op    = advance();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, op.getValue(), right);
        }
        return left;
    }

    // term → unary (('*' | '/') unary)*
    private Expression parseTerm() {
        Expression left = parseUnary();
        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            Token op    = advance();
            Expression right = parseUnary();
            left = new BinaryOpNode(left, op.getValue(), right);
        }
        return left;
    }

    // unary → '-' unary | primary
    private Expression parseUnary() {
        if (check(TokenType.MINUS)) {
            advance();
            Expression operand = parseUnary();
            return new BinaryOpNode(new NumberNode(0), "-", operand);
        }
        return parsePrimary();
    }

    // primary → NUMBER | STRING | IDENTIFIER
    private Expression parsePrimary() {
        Token token = peek();
        switch (token.getType()) {
            case NUMBER:
                advance();
                return new NumberNode(Double.parseDouble(token.getValue()));
            case STRING:
                advance();
                return new StringNode(token.getValue());
            case IDENTIFIER:
                advance();
                return new VariableNode(token.getValue());
            default:
                throw new RuntimeException(
                    "Syntax error on line " + token.getLine()
                    + ": expected a number, string, or variable name, "
                    + "but found '" + token.getValue() + "'");
        }
    }

    // ── Token stream utilities ────────────────────────────────────────────────

    private Token peek()                    { return tokens.get(current); }
    private boolean isAtEnd()               { return peek().getType() == TokenType.EOF; }
    private boolean check(TokenType type)   { return peek().getType() == type; }

    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        Token found = peek();
        throw new RuntimeException(message
            + " (line " + found.getLine() + ", found '" + found.getValue() + "')");
    }

    private void consumeNewlineOrEOF() {
        if (check(TokenType.NEWLINE)) advance();
    }

    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) advance();
    }
}
