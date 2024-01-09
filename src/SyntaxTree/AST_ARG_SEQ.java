package SyntaxTree;

public class AST_ARG_SEQ extends AST_Node {

	public AST_ARG_SEQ addChild(AST_Node node) {
		if (leftChild == null && rightChild == null) {
			this.add_leftChild(node);
		} else if (rightChild == null) {
			this.add_rightChild(node);
		} else {
			AST_ARG_SEQ newNode = new AST_ARG_SEQ();
			newNode.add_leftChild(this);
			newNode.add_rightChild(node);
			return newNode;
		}
		
		return this;
	}
	
	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
}
