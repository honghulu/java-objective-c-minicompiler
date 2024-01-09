package Scanners;
public class Scanner{
	public enum TokenType{
		NUM, 
		ID, 
		PLUS, MINUS, MUL, DIV, NE, EQ, LTE, GTE, LT, GT, OP, CP, ASSIGN, AND, OR,
		INT, END, OCB, CCB, COMMA, IF, WHILE, VOID, PUBLIC, PRIVATE, CLASS, RETURN;
	}

	public class Token{
		public TokenType tokenType;
		public String tokenVal;
		
		public Token(TokenType tokenType, String tokenVal){
			this.tokenType = tokenType;
			this.tokenVal = tokenVal;
		}
		
		public String toGrammarFont() {
			String newFont = null;
			
			if (this.tokenType.equals(Scanner.TokenType.NUM)) {
				newFont = "num";
			} else if (this.tokenType.equals(Scanner.TokenType.ID)) {
				newFont = "id";
			} else {
				newFont = this.tokenVal;
			}
			
			return newFont;
		}
		
		public String toString(){
			return "|" + this.tokenType + ": " + this.tokenVal + "|";
		}
	}

	public Token extractToken(StringBuilder stream){
		TokenType thisTokenType = null;
		String tokenValue = "";
		char charInput = stream.charAt(0);

		//NUM
		if (Character.isDigit(charInput)) {
			thisTokenType = TokenType.NUM;
			tokenValue = charInput + "";
			
			int index;
			for (index = 0;index < stream.length() - 1;index++) {
				if (!Character.isDigit(stream.charAt(index + 1))) {
					break;
				}
				tokenValue += stream.charAt(index + 1);
			}
			stream = stream.delete(0, index + 1);
		//PLUS
		} else if (charInput == '+') {
			thisTokenType = TokenType.PLUS;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//MINUS
		} else if (charInput == '-') {
			thisTokenType = TokenType.MINUS;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//MUL
		} else if (charInput == '*') {
			thisTokenType = TokenType.MUL;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//DIV
		} else if (charInput == '/') {
			thisTokenType = TokenType.DIV;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//NE
		} else if (stream.length() >= 2 && stream.substring(0, 2).equals("!=")) {
			thisTokenType = TokenType.NE;
			tokenValue = charInput + "" + stream.charAt(1);
			stream = stream.delete(0, 2);
		//IS
		} else if (charInput == '=') {
			//EQ
			if (stream.length() > 1 && stream.charAt(1) == '=') {
				thisTokenType = TokenType.EQ;
				tokenValue = "" + charInput + stream.charAt(1);
				stream = stream.delete(0, 2);
			} else {
				thisTokenType = TokenType.ASSIGN;
				tokenValue = charInput + "";
				stream = stream.deleteCharAt(0);
			}
		//LT
		} else if (charInput == '<') {
			//LTE
			if (stream.length() > 1 && stream.charAt(1) == '=') {
				thisTokenType = TokenType.LTE;
				tokenValue = "" + charInput + stream.charAt(1);
				stream = stream.delete(0, 2);
			} else {
				thisTokenType = TokenType.LT;
				tokenValue = charInput + "";
				stream = stream.deleteCharAt(0);
			}
		//GT
		} else if (charInput == '>') {
			//GTE
			if (stream.length() > 1 && stream.charAt(1) == '=') {
				thisTokenType = TokenType.GTE;
				tokenValue = "" + charInput + stream.charAt(1);
				stream = stream.delete(0, 2);
			} else {
				thisTokenType = TokenType.GT;
				tokenValue = charInput + "";
				stream = stream.deleteCharAt(0);
			}
		//OP
		} else if (charInput == '(') {
			thisTokenType = TokenType.OP;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//CP
		} else if (charInput == ')') {
			thisTokenType = TokenType.CP;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//AND
		} else if (stream.length() >= 2 && stream.substring(0, 2).equals("&&")) {
			thisTokenType = TokenType.AND;
			tokenValue = stream.substring(0, 2);
			stream = stream.delete(0, 2);
		//OR
		} else if (stream.length() >= 2 && stream.substring(0, 2).equals("||")) {
			thisTokenType = TokenType.OR;
			tokenValue = stream.substring(0, 2);
			stream = stream.delete(0, 2);
		//INT
		} else if (stream.length() >= 3 && stream.substring(0, 3).equals("int")) {
			thisTokenType = TokenType.INT;
			tokenValue = stream.substring(0, 3);
			stream = stream.delete(0, 3);
		//END
		} else if (charInput == ';') {
			thisTokenType = TokenType.END;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//OCB
		} else if (charInput == '{') {
			thisTokenType = TokenType.OCB;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//CCB
		} else if (charInput == '}') {
			thisTokenType = TokenType.CCB;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//COMMA
		} else if (charInput == ',') {
			thisTokenType = TokenType.COMMA;
			tokenValue = charInput + "";
			stream = stream.deleteCharAt(0);
		//IF
		} else if (stream.length() >= 2 && stream.substring(0, 2).equals("if")) {
			thisTokenType = TokenType.IF;
			tokenValue = stream.substring(0, 2);
			stream = stream.delete(0, 2);
		//WHILE
		} else if (stream.length() >= 5 && stream.substring(0, 5).equals("while")) {
			thisTokenType = TokenType.WHILE;
			tokenValue = stream.substring(0, 5);
			stream = stream.delete(0, 5);
		//VOID
		} else if (stream.length() >= 4 && stream.substring(0, 4).equals("void")) {
			thisTokenType = TokenType.VOID;
			tokenValue = stream.substring(0, 4);
			stream = stream.delete(0, 4);
		//PUBLIC
		} else if (stream.length() >= 6 && stream.substring(0, 6).equals("public")) {
			thisTokenType = TokenType.PUBLIC;
			tokenValue = stream.substring(0, 6);
			stream = stream.delete(0, 6);
		//PRIVATE
		} else if (stream.length() >= 7 && stream.substring(0, 7).equals("private")) {
			thisTokenType = TokenType.PRIVATE;
			tokenValue = stream.substring(0, 7);
			stream = stream.delete(0, 7);
		//CLASS
		} else if (stream.length() >= 5 && stream.substring(0, 5).equals("class")) {
			thisTokenType = TokenType.CLASS;
			tokenValue = stream.substring(0, 5);
			stream = stream.delete(0, 5);
		//RETURN
		} else if (stream.length() >= 6 && stream.substring(0, 6).equals("return")) {
			thisTokenType = TokenType.RETURN;
			tokenValue = stream.substring(0, 6);
			stream = stream.delete(0, 6);
		//ID
		} else if (Character.isLetter(charInput)) {
			thisTokenType = TokenType.ID;
			
			int index;
			for (index = 0;index < stream.length();index++) {
				if (!Character.isLetterOrDigit(stream.charAt(index))) {
					break;
				}
			}
			
			tokenValue = stream.substring(0, index);
			stream = stream.delete(0, index);
		//SPACE
		} else if (charInput == ' ' || charInput == '\n' || charInput == '\t') {
			int index;
			for (index = 0;index < stream.length() - 1;index++) {
				if (stream.charAt(index + 1) != ' ' && stream.charAt(index + 1) != '\n' && stream.charAt(index + 1) != '\t') {
					break;
				}
			}
			stream = stream.delete(0, index + 1);
			return null;
		//ELSE
		} else {
			System.err.println("Invaild Input " + stream.charAt(0) + " is found");
			stream = stream.deleteCharAt(0);
			return null;
		}
		return new Token(thisTokenType, tokenValue);
	}

	public String extractTokens(String arg){
		String result = "";
		StringBuilder sbArg = new StringBuilder(arg);
		while(sbArg.length() != 0) {
			Token nextToken = extractToken(sbArg);
			if (nextToken != null) {
				result += nextToken.toString();
			}
		}
		return result;
	}

}
