package interpreter;

import Instructions.*;
import environment.Environment;
import tokens.Token;
import tokens.Tokenizer;
import parser.Parser;

import java.util.List;

public class Interpreter {

    public void run(String sourceCode) {
        try {
            Tokenizer         tokenizer    = new Tokenizer(sourceCode);
            List<Token>       tokens       = tokenizer.tokenize();

            Parser            parser       = new Parser(tokens);
            List<Instruction> instructions = parser.parse();

            Environment env = new Environment();
            for (Instruction instruction : instructions) {
                instruction.execute(env);
            }

        } catch (RuntimeException e) {
            System.err.println("BLOOP Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
