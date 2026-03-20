# BLOOP-Lang Interpreter

A Java-based interpreter for a custom scripting language (BLOOP), implementing tokenization, parsing, and AST-based execution. 
---

## 🔹 Overview

- Java-based interpreter for a custom scripting language (BLOOP)  
- Uses **Tokenizer → Parser → Evaluator** pipeline  
- Builds and executes programs using an **AST (Abstract Syntax Tree)**  
- Supports variables, expressions, conditionals, and loops  
- Uses a **Map-based Environment** for variable storage  
- Designed with clean **OOP principles (interfaces, modular design)**  
- Runs `.bloop` files via **command-line interface (CLI)**  
- Demonstrates core concepts of **interpreter design and parsing**

---
## What is BLOOP?

BLOOP (Beginner-Level Object-Oriented Program) is a toy language that reads like plain English. You write `.bloop` files and run them through a Java interpreter you build yourself.

```bloop
put 10 into x
put 3 into y
put x + y * 2 into result
print result

if result > 10 then:
    print "big number"

repeat 3 times:
    print "hello"
```

---

## How It Works

The interpreter is a **three-stage pipeline**. Each stage transforms its input and passes the result forward.

```
Source Code (.bloop file)
        │
        ▼
┌───────────────┐
│   Tokenizer   │  Reads characters → emits a flat List<Token>
└───────┬───────┘
        │
        ▼
┌───────────────┐
│    Parser     │  Reads tokens → builds a List<Instruction> (the AST)
└───────┬───────┘
        │
        ▼
┌───────────────┐
│   Evaluator   │  Walks the tree → executes instructions, prints output
└───────────────┘
```

1. **Tokenizer** — breaks source text into a list of tokens
2. **Parser** — builds an Abstract Syntax Tree (AST) using instruction and expression nodes
3. **Evaluator** — walks the tree and executes each instruction

---

## Project Structure

| Class | Role |
|-------|------|
| `TokenType` / `Token` | Enum + immutable token object |
| `Expression` (interface) | Implemented by `NumberNode`, `StringNode`, `VariableNode`, `BinaryOpNode` |
| `Environment` | Variable store (`Map<String, Object>`) |
| `Instruction` (interface) | Implemented by `AssignInstruction`, `PrintInstruction`, `IfInstruction`, `RepeatInstruction` |
| `Tokenizer` | Source string → `List<Token>` |
| `Parser` | `List<Token>` → `List<Instruction>` |
| `Interpreter` | Wires all three stages together |
| `Main` | CLI entry point |

---

## Setup & Run

```bash
# Compile
javac src/*.java -d out/

# Run a .bloop file
java -cp out Main program.bloop
```

---

## Sample Output

**Input (`program.bloop`):**
```bloop
put 1 into i
repeat 4 times:
    print i
    put i + 1 into i
```

**Output:**
```
1
2
3
4
```

---

## Team

| Member | Responsibility |
|--------|---------------|
| Ravi Mourya | Tokenizer |
| Manoj Kharkar | Parser + Expression nodes |
| Jamal Akhtar | Instructions + Environment + Main |

---
