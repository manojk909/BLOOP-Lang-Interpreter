// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package tokens;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
   private final String source;
   private int pos;
   private int line;
   private int currentLineIndent;

   public Tokenizer(String var1) {
      this.source = var1;
      this.pos = 0;
      this.line = 1;
      this.currentLineIndent = this.measureIndentAt(0);
   }

   public List<Token> tokenize() {
      ArrayList var1 = new ArrayList();

      while(this.pos < this.source.length()) {
         char var2 = this.source.charAt(this.pos);
         if (var2 != ' ' && var2 != '\t' && var2 != '\r') {
            if (var2 != '\n') {
               if (var2 != '#') {
                  if (var2 != '"') {
                     if (!Character.isDigit(var2)) {
                        if (!Character.isLetter(var2) && var2 != '_') {
                           switch (var2) {
                              case '!':
                                 if (this.pos + 1 < this.source.length() && this.source.charAt(this.pos + 1) == '=') {
                                    var1.add(this.tok(TokenType.NOT_EQUAL, "!="));
                                    this.pos += 2;
                                    break;
                                 }

                                 throw new RuntimeException("Tokenizer error on line " + this.line + ": unexpected '!'. Did you mean '!='?");
                              case '"':
                              case '#':
                              case '$':
                              case '%':
                              case '&':
                              case '\'':
                              case '(':
                              case ')':
                              case ',':
                              case '.':
                              case '0':
                              case '1':
                              case '2':
                              case '3':
                              case '4':
                              case '5':
                              case '6':
                              case '7':
                              case '8':
                              case '9':
                              case ';':
                              default:
                                 throw new RuntimeException("Tokenizer error on line " + this.line + ": unexpected character '" + var2 + "'");
                              case '*':
                                 var1.add(this.tok(TokenType.STAR, "*"));
                                 ++this.pos;
                                 break;
                              case '+':
                                 var1.add(this.tok(TokenType.PLUS, "+"));
                                 ++this.pos;
                                 break;
                              case '-':
                                 var1.add(this.tok(TokenType.MINUS, "-"));
                                 ++this.pos;
                                 break;
                              case '/':
                                 var1.add(this.tok(TokenType.SLASH, "/"));
                                 ++this.pos;
                                 break;
                              case ':':
                                 var1.add(this.tok(TokenType.COLON, ":"));
                                 ++this.pos;
                                 break;
                              case '<':
                                 if (this.pos + 1 < this.source.length() && this.source.charAt(this.pos + 1) == '=') {
                                    var1.add(this.tok(TokenType.LESS_EQ, "<="));
                                    this.pos += 2;
                                    break;
                                 }

                                 var1.add(this.tok(TokenType.LESS, "<"));
                                 ++this.pos;
                                 break;
                              case '=':
                                 if (this.pos + 1 < this.source.length() && this.source.charAt(this.pos + 1) == '=') {
                                    var1.add(this.tok(TokenType.EQUAL_EQUAL, "=="));
                                    this.pos += 2;
                                    break;
                                 }

                                 ++this.pos;
                                 break;
                              case '>':
                                 if (this.pos + 1 < this.source.length() && this.source.charAt(this.pos + 1) == '=') {
                                    var1.add(this.tok(TokenType.GREATER_EQ, ">="));
                                    this.pos += 2;
                                 } else {
                                    var1.add(this.tok(TokenType.GREATER, ">"));
                                    ++this.pos;
                                 }
                           }
                        } else {
                           var1.add(this.readWord());
                        }
                     } else {
                        var1.add(this.readNumber());
                     }
                  } else {
                     var1.add(this.readString());
                  }
               } else {
                  while(this.pos < this.source.length() && this.source.charAt(this.pos) != '\n') {
                     ++this.pos;
                  }
               }
            } else {
               if (!var1.isEmpty() && ((Token)var1.get(var1.size() - 1)).getType() != TokenType.NEWLINE) {
                  var1.add(new Token(TokenType.NEWLINE, "\\n", this.line, this.currentLineIndent));
               }

               ++this.line;
               ++this.pos;
               this.currentLineIndent = this.measureIndentAt(this.pos);
            }
         } else {
            ++this.pos;
         }
      }

      var1.add(new Token(TokenType.EOF, "", this.line, 0));
      return var1;
   }

   private Token tok(TokenType var1, String var2) {
      return new Token(var1, var2, this.line, this.currentLineIndent);
   }

   private int measureIndentAt(int var1) {
      int var2 = 0;

      while(var1 < this.source.length()) {
         char var3 = this.source.charAt(var1);
         if (var3 == ' ') {
            ++var2;
            ++var1;
         } else {
            if (var3 != '\t') {
               break;
            }

            var2 += 4;
            ++var1;
         }
      }

      return var2;
   }

   private Token readString() {
      int var1 = this.line;
      int var2 = this.currentLineIndent;
      ++this.pos;

      StringBuilder var3;
      for(var3 = new StringBuilder(); this.pos < this.source.length() && this.source.charAt(this.pos) != '"'; var3.append(this.source.charAt(this.pos++))) {
         if (this.source.charAt(this.pos) == '\n') {
            ++this.line;
         }
      }

      if (this.pos >= this.source.length()) {
         throw new RuntimeException("Tokenizer error on line " + var1 + ": string literal is not closed — missing closing '\"'");
      } else {
         ++this.pos;
         return new Token(TokenType.STRING, var3.toString(), var1, var2);
      }
   }

   private Token readNumber() {
      int var1 = this.line;
      int var2 = this.currentLineIndent;
      StringBuilder var3 = new StringBuilder();

      for(boolean var4 = false; this.pos < this.source.length() && (Character.isDigit(this.source.charAt(this.pos)) || this.source.charAt(this.pos) == '.' && !var4); var3.append(this.source.charAt(this.pos++))) {
         if (this.source.charAt(this.pos) == '.') {
            var4 = true;
         }
      }

      return new Token(TokenType.NUMBER, var3.toString(), var1, var2);
   }

   private Token readWord() {
      int var1 = this.line;
      int var2 = this.currentLineIndent;
      StringBuilder var3 = new StringBuilder();

      while(this.pos < this.source.length() && (Character.isLetterOrDigit(this.source.charAt(this.pos)) || this.source.charAt(this.pos) == '_')) {
         var3.append(this.source.charAt(this.pos++));
      }

      switch (var3.toString()) {
         case "put" -> {
            return new Token(TokenType.PUT, var4, var1, var2);
         }
         case "into" -> {
            return new Token(TokenType.INTO, var4, var1, var2);
         }
         case "print" -> {
            return new Token(TokenType.PRINT, var4, var1, var2);
         }
         case "if" -> {
            return new Token(TokenType.IF, var4, var1, var2);
         }
         case "then" -> {
            return new Token(TokenType.THEN, var4, var1, var2);
         }
         case "else" -> {
            return new Token(TokenType.ELSE, var4, var1, var2);
         }
         case "repeat" -> {
            return new Token(TokenType.REPEAT, var4, var1, var2);
         }
         case "times" -> {
            return new Token(TokenType.TIMES, var4, var1, var2);
         }
         default -> {
            return new Token(TokenType.IDENTIFIER, var4, var1, var2);
         }
      }
   }
}
