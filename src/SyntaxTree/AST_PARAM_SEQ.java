package SyntaxTree;

public class AST_PARAM_SEQ extends AST_Node {

	public AST_PARAM_SEQ addChild(AST_Node node) {
		if (leftChild == null && rightChild == null) {
			this.add_leftChild(node);
		} else if (rightChild == null) {
			this.add_rightChild(node);
		} else {
			AST_PARAM_SEQ newNode = new AST_PARAM_SEQ();
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
