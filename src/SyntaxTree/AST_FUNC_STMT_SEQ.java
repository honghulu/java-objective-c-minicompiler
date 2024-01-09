package SyntaxTree;

public class AST_FUNC_STMT_SEQ extends AST_Node {
	
	public AST_FUNC_STMT_SEQ() {
		super();
	}
	
	public AST_FUNC_STMT_SEQ(AST_STMT_SEQ param, AST_RET paramSeq) {
		this.add_leftChild(param);
		this.add_rightChild(paramSeq);
	}
	
	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
}
