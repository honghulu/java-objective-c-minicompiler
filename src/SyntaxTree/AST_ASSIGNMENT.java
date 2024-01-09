package SyntaxTree;

public class AST_ASSIGNMENT extends AST_STMT{

	public void addChild(AST_VAR_DECL decl, AST_EXPR expr) {
		this.add_leftChild(decl);
		this.add_rightChild(expr);
	}
	
	public void addChild(AST_ID id, AST_EXPR expr) {
		this.add_leftChild(id);
		this.add_rightChild(expr);
	}
	
	public String getName() {
		if (leftChild instanceof AST_VAR_DECL) {
			return ((AST_VAR_DECL)leftChild).getName();
		} else if (leftChild instanceof AST_ID){
			return ((AST_ID)leftChild).get_str_repr();
		}
		
		return null;
	}
}
