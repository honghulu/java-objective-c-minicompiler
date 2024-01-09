package SyntaxTree;

public class AST_ARGUMENT extends AST_Node {

	public AST_ARGUMENT() {
		super();
	}
	
	public AST_ARGUMENT(AST_ARG arg, AST_ARG_SEQ argSeq) {
		this.add_leftChild(arg);
		this.add_rightChild(argSeq);
	}
	
	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
}
