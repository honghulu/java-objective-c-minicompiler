package SyntaxTree;

public class AST_PARAMETER extends AST_Node {
	
	public AST_PARAMETER() {
		super();
	}
	
	public AST_PARAMETER(AST_PARAM param, AST_PARAM_SEQ paramSeq) {
		this.add_leftChild(param);
		this.add_rightChild(paramSeq);
	}
	
	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
}
