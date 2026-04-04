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

## 📁 Project Structure

```
bloop/
├── src/
│   ├── bloop/
│   │   ├── Bloop.java          ← CLI entry point
│   │   ├── Lexer.java          ← Tokenizer
│   │   ├── Token.java          ← Token class
│   │   ├── TokenType.java      ← Token enum
│   │   ├── Parser.java         ← AST builder
│   │   ├── Interpreter.java    ← Tree executor
│   │   └── BloopError.java     ← Error messages
│   │
│   └── bloop/ast/
│       ├── ASTNode.java        ← Interface
│       ├── AssignNode.java     ← put x into y
│       ├── PrintNode.java      ← print z
│       ├── IfNode.java         ← if / else
│       ├── RepeatNode.java     ← repeat N times
│       ├── BinaryOpNode.java   ← x + y * 2
│       ├── CompareNode.java    ← z > 30
│       ├── NumberNode.java     ← 10, 3.14
│       ├── StringNode.java     ← "hello"
│       └── VariableNode.java   ← x, y, z
│
├── examples/
│   ├── hello.bloop
│   ├── math.bloop
│   ├── conditions.bloop
│   └── loops.bloop
│
├── tests/
│   └── bloop/BloopTest.java
│
└── README.md
```

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

## 👥 Team

| Member | Role | Branch |
|--------|------|--------|
| **Ravi Mourya** | Tokenizer (Lexer + Tokens) | `feature/lexer` |
| **Manoj Kharkar** | Parsing (AST + Parser) | `feature/parser-ast` |
| **Jamal Akhtar** | Interpreter + CLI + Examples | `feature/interpreter` |

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher — check with `java -version`
- Git — check with `git --version`
- IDE: IntelliJ IDEA Community Edition (recommended)

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-team/bloop-lang.git
cd bloop-lang
```

### Step 2 — Compile All Source Files

Run from inside the `bloop/` root folder:

```bash
javac -d out src/bloop/*.java src/bloop/ast/*.java
```

This creates an `out/` folder containing all compiled `.class` files.

### Step 3 — Write a `.bloop` Program

Create `examples/hello.bloop` and write your BLOOP code.

### Step 4 — Run the Program

```bash
java -cp out bloop.Bloop examples/hello.bloop
```

### Step 5 — Run the Test Suite

```bash
javac -d out src/bloop/*.java src/bloop/ast/*.java tests/bloop/BloopTest.java
java -cp out bloop.BloopTest
```

### Step 6 — Package as a JAR (Optional)

```bash
jar cfe bloop.jar bloop.Bloop -C out .
java -jar bloop.jar examples/hello.bloop
```
