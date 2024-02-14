package br.ufma.ecp.token;

public class Token {

    public final TokenType type;
    public final String lexeme;

    final int line;

    public Token(TokenType type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public String toString() {
        var type = this.type.toString().toLowerCase();
        if (type.equals("number"))
            type = "integerConstant";

        if (type.equals("string"))
            type = "stringConstant";

        if (type.equals("ident"))
            type = "identifier";

        if (TokenType.isSymbol(lexeme.charAt(0)))
            type = "symbol";

        if (TokenType.isKeyword(this.type))
            type = "keyword";

        String escapedLexeme = lexeme
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        return "<" + type + "> " + escapedLexeme + " </" + type + ">";
    }

}
