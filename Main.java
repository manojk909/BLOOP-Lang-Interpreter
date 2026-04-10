import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import interpreter.Interpreter;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the command in correct form like this:");
            System.out.println("java Main filename.bloop");
            return;
        }

        String filePath = args[0];

        if (!filePath.endsWith(".bloop")) {
            throw new RuntimeException("Invalid file type. Expected .bloop");
        }

        String sourceCode;
        try {
            sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filePath + "\n" + e.getMessage());
        }

        Interpreter ip = new Interpreter();
        ip.run(sourceCode);
    }
}
