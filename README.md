# BLOOP Language Interpreter

A lightweight, tree-walking interpreter for **BLOOP** — a simple, English-like programming language implemented in Java. BLOOP supports variables, arithmetic, string operations, conditionals, and loops, making it ideal as a learning project for understanding how interpreters work.

---

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [BLOOP Language Reference](#bloop-language-reference)
- [Sample Programs](#sample-programs)
- [Architecture Overview](#architecture-overview)
- [Error Handling](#error-handling)

---

## Features

- **Variables** — store numbers and strings
- **Arithmetic** — `+`, `-`, `*`, `/` with correct operator precedence
- **String concatenation** — using `+`
- **Comparisons** — `>`, `<`, `>=`, `<=`, `==`, `!=`
- **Conditionals** — `if / then / else` blocks
- **Loops** — `repeat N times` blocks
- **Nesting** — blocks can be nested arbitrarily deep (indent-based scoping)
- **Comments** — lines starting with `#` are ignored
- **Unary minus** — negative number literals and expressions

---

## Project Structure

```
BLOOP-Lang-Interpreter-main/
│
├── Main.java                        # Entry point — reads .bloop file and runs interpreter
│
├── tokens/
│   ├── Token.java                   # Token data class (type, value, line, indent level)
│   ├── TokenType.java               # Enum of all token types (keywords, operators, literals)
│   └── Tokenizer.java               # Lexer — converts raw source text into a token list
│
├── parser/
│   └── Parser.java                  # Recursive-descent parser — builds AST instruction list
│
├── Nodes/
│   ├── Expression.java              # Interface for all expression nodes
│   ├── BinaryOpNode.java            # Arithmetic and comparison operations
│   ├── NumberNode.java              # Numeric literal
│   ├── StringNode.java              # String literal
│   └── VariableNode.java            # Variable reference
│
├── Instructions/
│   ├── Instruction.java             # Interface for all executable instructions
│   ├── AssignInstruction.java       # put <expr> into <variable>
│   ├── PrintInstruction.java        # print <expr>
│   ├── IfInstruction.java           # if <condition> then / else
│   └── RepeatInstruction.java       # repeat <count> times
│
├── interpreter/
│   └── Interpreter.java             # Orchestrates tokenize → parse → execute pipeline
│
├── environment/
│   └── Environment.java             # Variable store (name → value map)
│
└── samples/
    ├── program1.bloop               # Arithmetic and variables
    ├── program2.bloop               # String output
    ├── program3.bloop               # Conditional (if / then)
    ├── program4.bloop               # Conditional (if / then) — duplicate
    ├── bonus_else.bloop             # if / else branching
    ├── bonus_nested.bloop           # Nested loops and conditionals
    └── bonus_extended.bloop         # Extended operators, string concat, unary minus
```

---

## Getting Started

### Prerequisites

- **Java 8 or higher** — verify with `java -version`

### Compile

From the project root directory:

```bash
javac -d out $(find . -name "*.java")
```

Or compile manually in dependency order:

```bash
mkdir -p out
javac -d out tokens/*.java Nodes/*.java environment/*.java Instructions/*.java interpreter/*.java Main.java
```

### Run a BLOOP Program

```bash
java -cp out Main samples/program1.bloop
```

Replace `samples/program1.bloop` with the path to any `.bloop` file.

> **Note:** The interpreter only accepts files with the `.bloop` extension.

---

## BLOOP Language Reference

### Variables

Variables are dynamically typed and hold either a number or a string. Use `put ... into` to assign:

```
put 42 into x
put "hello" into greeting
```

### Arithmetic

Standard arithmetic operators are supported with conventional precedence (`*` and `/` before `+` and `-`):

```
put 10 + 3 * 2 into result    # result = 16
put x - 5 into y
put 100 / 4 into quarter
```

### Print

Output a value or expression to the console:

```
print x
print "Hello, World!"
print "Score: " + score
```

### String Concatenation

Use `+` to join strings. If either operand is a string, the result is a string:

```
put "Hello " + "World" into msg
print "Value: " + 42
```

### Conditionals

```
if score > 50 then:
    print "Pass"

if score >= 90 then:
    print "Grade: A"
else:
    print "Grade: B"
```

The `else` block is optional. The colon (`:`) after `then` and `else` is also optional.

### Loops

Repeat a block a fixed number of times:

```
repeat 5 times:
    print "Hello"
```

The count can be a variable or any numeric expression:

```
put 3 into n
repeat n times:
    print "Looping"
```

### Nesting

`if` and `repeat` blocks can be nested to any depth. Nesting is determined by **indentation** (spaces or tabs):

```
put 1 into i
repeat 5 times:
    if i > 3 then:
        print "big"
    else:
        print "small"
    put i + 1 into i
```

### Operators Reference

| Operator | Description              | Works on        |
|----------|--------------------------|-----------------|
| `+`      | Addition / Concatenation | Numbers, Strings|
| `-`      | Subtraction              | Numbers         |
| `*`      | Multiplication           | Numbers         |
| `/`      | Division                 | Numbers         |
| `>`      | Greater than             | Numbers         |
| `<`      | Less than                | Numbers         |
| `>=`     | Greater than or equal    | Numbers         |
| `<=`     | Less than or equal       | Numbers         |
| `==`     | Equal                    | Numbers, Strings|
| `!=`     | Not equal                | Numbers, Strings|

---

## Sample Programs

### program1.bloop — Arithmetic

```
put 10 into x
put 3 into y
put x + y * 2 into result
print result
```

**Output:** `16`

### bonus_else.bloop — If / Else

```
put 90 into score
if score > 80 then:
    print "Grade: A"
else:
    print "Grade: F"
```

**Output:** `Grade: A`

### bonus_nested.bloop — Nested Blocks

---

## Architecture Overview

The interpreter uses a classic three-phase pipeline:

```
Source Code (.bloop)
        │
        ▼
   [ Tokenizer ]         tokens/Tokenizer.java
   Converts raw text into a flat list of Token objects.
   Tracks line numbers and indentation levels.
        │
        ▼
    [ Parser ]           parser/Parser.java
   Recursive-descent parser.
   Consumes the token list and builds a list of Instruction objects.
   Expression nodes (AST) are constructed for all expressions.
        │
        ▼
  [ Interpreter ]        interpreter/Interpreter.java
   Walks the instruction list and calls execute(env) on each.
   Expressions are evaluated recursively via evaluate(env).
   All variables are stored in an Environment (a HashMap).
        │
        ▼
     Output / Errors
```

**Key design patterns:**
- `Instruction` interface — each statement type implements `execute(Environment)`
- `Expression` interface — each expression node implements `evaluate(Environment)` and returns an `Object` (either `Double` or `String`)
- `Environment` — a simple `LinkedHashMap<String, Object>` that maintains insertion order

---

## Error Handling

BLOOP provides descriptive error messages for common mistakes:

| Error Type       | Example Message |
|------------------|-----------------|
| Unknown character | `Tokenizer error on line 3: unexpected character '@'` |
| Unclosed string   | `Tokenizer error on line 2: string literal is not closed — missing closing '"'` |
| Syntax error      | `Syntax error on line 5: expected 'into' after the value` |
| Undefined variable | `Runtime error: variable not defined: 'x'. Did you forget to assign it with 'put ... into x'?` |
| Type mismatch     | `Type error: left side of '-' must be a number, but got: "hello"` |
| Division by zero  | `Runtime error: division by zero.` |
| Non-integer repeat count | `Runtime error: 'repeat' count must be a whole number, but got: 2.5` |

All errors are printed to `stderr` and the interpreter exits with code `1`.
