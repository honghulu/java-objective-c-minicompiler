package SyntaxTree;

import SymbolTable.Symbol;

public class AST_FUNC_CALL extends AST_EXPR {
	private String funcName;
	private Symbol funcInfo;
	private int retLabel;
	private int numArgs;
	private int offset;

	public AST_FUNC_CALL(int id, int retlabelID, AST_ID name) {
		tempID = id;
		retLabel = retlabelID;
		funcName = name.get_str_repr();
		funcInfo = name.getInfo();
		numArgs = 0;
	}
	
	public AST_FUNC_CALL(int retlabelID, AST_ID name, AST_ARGUMENT argument) {
		retLabel = retlabelID;
		funcName = name.get_str_repr();
		funcInfo = name.getInfo();
		this.add_leftChild(argument);
		countArgs(this.leftChild);
	}
	
	private void countArgs(AST_Node node) {
		if (node instanceof AST_ARG) {
			numArgs += 1;
			return;
		} else if (node == null) {
			return;
		}
		countArgs(node.leftChild);
		countArgs(node.rightChild);
	}

	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
	
	public void setOffset(int given_offset) {
		offset = given_offset + tempID;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getNumArgs() {
		return numArgs;
	}
	
	public int getRetrieveID() {
		return tempID;
	}
	
	public String getRetLabel() {
		return "retLabel" + retLabel;
	}
	
	public String getName() {
		return funcName;
	}
}