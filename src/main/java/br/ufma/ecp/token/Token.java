package br.ufma.ecp.token;
public class Token {

    public final TokenType type;
    public final String lexeme;

    final int line;
  
    public Token (TokenType type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }
    
    public String toString() {
        var type = this.type.toString();
        if (type.equals("NUMBER"))
            type =  "intConst";

        if (type.equals("STRING"))
            type =  "stringConst";

        if (type.equals("IDENT"))
            type =  "identifier";

        if (TokenType.isSymbol(lexeme.charAt(0)))
            type = "symbol";

        if (TokenType.isKeyword(this.type) )
            type = "keyword";
    

        return "<"+ type +">" + lexeme + "</"+ type + ">";
    }
    
}
