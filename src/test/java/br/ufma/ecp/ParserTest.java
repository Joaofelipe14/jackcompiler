package br.ufma.ecp;

import static org.junit.Assert.assertEquals;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class ParserTest extends TestSupport {

    @Test
    public void testParseTermInteger() {
        var input = "10;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();
        var expectedResult = """
                <term>
                <integerConstant> 10 </integerConstant>
                </term>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseTermIdentifer() {
        var input = "varName;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();

        var expectedResult = """
                <term>
                <identifier> varName </identifier>
                </term>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseTermString() {
        var input = "\"Hello World\"";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();

        var expectedResult = """
                <term>
                <stringConstant> Hello World </stringConstant>
                </term>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseExpressionSimple() {
        var input = "10+20";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseExpression();

        var expectedResult = """
                <expression>
                <term>
                <integerConstant> 10 </integerConstant>
                </term>
                <symbol> + </symbol>
                <term>
                <integerConstant> 20 </integerConstant>
                </term>
                </expression>
                """;

        var result = parser.XMLOutput();
        result = result.replaceAll("\r", "");
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseLetSimple() {
        var input = "let var1 = 10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
        var expectedResult = """
                  <letStatement>
                    <keyword> let </keyword>
                    <identifier> var1 </identifier>
                    <symbol> = </symbol>
                    <expression>
                      <term>
                      <integerConstant> 10 </integerConstant>
                      </term>
                      <symbol> + </symbol>
                      <term>
                      <integerConstant> 20 </integerConstant>
                      </term>
                      </expression>
                    <symbol> ; </symbol>
                  </letStatement>
                """;
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseLetSimpleConchentes() {
        var input = "let var1[1] = 10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
        var expectedResult = """
                  <letStatement>
                  <keyword> let </keyword>
                  <identifier> var1 </identifier>
                  <symbol> [ </symbol>
                  <expression>
                  <term>
                  <integerConstant> 1 </integerConstant>
                  </term>
                  </expression>
                  <symbol> ] </symbol>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                    <integerConstant> 10 </integerConstant>
                    </term>
                    <symbol> + </symbol>
                    <term>
                    <integerConstant> 20 </integerConstant>
                    </term>
                    </expression>
                  <symbol> ; </symbol>
                </letStatement>
                          """;
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test

    public void testParseSubroutineCall() {
        // subroutineName '(' expressionList? ')' | (className|varName)
        // '.' subroutineName '(' expressionList? ')'
        var input = "hello()";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseSubroutineCall();

        var expectedResult = """
                <identifier> hello </identifier>
                <symbol> ( </symbol>
                <symbol> ) </symbol>
                """;

        var result = parser.XMLOutput();
        result = result.replaceAll("\r", "");
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);

    }

    @Test
    public void testParseDo() {
        var input = "do hello();";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseDo();

        var expectedResult = """
                  <doStatement>
                  <keyword> do </keyword>
                  <subroutineCall>
                  <identifier> hello </identifier>
                  <symbol> ( </symbol>
                  <symbol> ) </symbol>
                  </subroutineCall>
                  <symbol> ; </symbol>
                </doStatement>
                      """;
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseClassVarDec() {
        var input = "field Square square;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseClassVarDec();
        var expectedResult = """
            <classVarDec>
            <keyword> field </keyword>
            <identifier> Square </identifier>
            <identifier> square </identifier>
            <symbol> ; </symbol>
          </classVarDec>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseIf() {
      var input = "if (direction = 1) { do square.moveUp(); }";
      var expectedResult = """
          <ifStatement>
          <keyword> if </keyword>
          <symbol> ( </symbol>
          <expression>
            <term>
              <identifier> direction </identifier>
            </term>
            <symbol> = </symbol>
            <term>
              <integerConstant> 1 </integerConstant>
            </term>
          </expression>
          <symbol> ) </symbol>
          <symbol> { </symbol>
          <statements>
            <doStatement>
              <keyword> do </keyword>
              <identifier> square </identifier>
              <symbol> . </symbol>
              <identifier> moveUp </identifier>
              <symbol> ( </symbol>
              <expressionList>
              </expressionList>
              <symbol> ) </symbol>
              <symbol> ; </symbol>
            </doStatement>
          </statements>
          <symbol> } </symbol>
        </ifStatement>
              """;

      var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
      parser.parseIf();
      var result = parser.XMLOutput();
      expectedResult = expectedResult.replaceAll("  ", "");
      result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
      assertEquals(expectedResult, result);
  }


  @Test
    public void testParseSubroutineDec() {
        var input = """
            constructor Square new(int Ax, int Ay, int Asize) {
                let x = Ax;
                let y = Ay;
                let size = Asize;
                do draw();
                return this;
             }
                """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseSubroutineDec();
        var expectedResult = """
            <subroutineDec>
            <keyword> constructor </keyword>
            <identifier> Square </identifier>
            <identifier> new </identifier>
            <symbol> ( </symbol>
            <parameterList>
              <keyword> int </keyword>
              <identifier> Ax </identifier>
              <symbol> , </symbol>
              <keyword> int </keyword>
              <identifier> Ay </identifier>
              <symbol> , </symbol>
              <keyword> int </keyword>
              <identifier> Asize </identifier>
            </parameterList>
            <symbol> ) </symbol>
            <subroutineBody>
              <symbol> { </symbol>
              <statements>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> x </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Ax </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> y </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Ay </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> size </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Asize </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <doStatement>
                  <keyword> do </keyword>
                  <identifier> draw </identifier>
                  <symbol> ( </symbol>
                  <expressionList>
                  </expressionList>
                  <symbol> ) </symbol>
                  <symbol> ; </symbol>
                </doStatement>
                <returnStatement>
                  <keyword> return </keyword>
                  <expression>
                    <term>
                      <keyword> this </keyword>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </returnStatement>
              </statements>
              <symbol> } </symbol>
            </subroutineBody>
          </subroutineDec>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }



}