package SyntaxTree;

public class AST_EXPR extends AST_Node {
	protected String str_repr;
	protected int tempID;
	
	public void set_str_repr(String str) {
		this.str_repr = str;
	}
	
	public String get_str_repr() {
		return str_repr;
	}
	
	public String get_temp_repr() {
		return "temp" + tempID;
	}
	
	public void setTempID(int tempID) {
		this.tempID = tempID;
	}
	
	public int getTempID() {
		return tempID;
	}
	
	public String selfDeclaration() {
		return "";
	}
	
	public int getOffset() {
		return -1;
	}
	
	public String toString() {
		return get_str_repr();
	}
}
