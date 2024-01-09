import java.util.*;

import Exceptions.AmbiguousVariableException;
import Exceptions.UndefinedVariableException;
import ExpressionGrammar.ScanGrammar;
import Scanners.Scanner;
import SymbolTable.Environment;
import SymbolTable.Symbol;
import SymbolTable.funcSymbol;
import SymbolTable.varSymbol;
import SyntaxTree.AST_ARG;
import SyntaxTree.AST_ARGUMENT;
import SyntaxTree.AST_ARG_SEQ;
import SyntaxTree.AST_ASSIGNMENT;
import SyntaxTree.AST_CONTROL_FLOW;
import SyntaxTree.AST_CP;
import SyntaxTree.AST_EXPR;
import SyntaxTree.AST_FUNC;
import SyntaxTree.AST_FUNC_CALL;
import SyntaxTree.AST_FUNC_STMT_SEQ;
import SyntaxTree.AST_ID;
import SyntaxTree.AST_NUM;
import SyntaxTree.AST_Node;
import SyntaxTree.AST_OP;
import SyntaxTree.AST_PARAM;
import SyntaxTree.AST_PARAMETER;
import SyntaxTree.AST_PARAM_SEQ;
import SyntaxTree.AST_PRGM;
import SyntaxTree.AST_PRGM_SEQ;
import SyntaxTree.AST_RET;
import SyntaxTree.AST_STMT;
import SyntaxTree.AST_STMT_SEQ;
import SyntaxTree.AST_VAR_DECL;
import ThreeAddressObject.Assign_ThreeAddrObject;
import ThreeAddressObject.BinaryOP_ThreeAddrObject;
import ThreeAddressObject.ControlFlow_ThreeAddrObject;
import ThreeAddressObject.EnterFunctionMarker;
import ThreeAddressObject.ExitFunctionMarker;
import ThreeAddressObject.FunctionCall_ThreeAddrobject;
import ThreeAddressObject.Goto_ThreeAddrObject;
import ThreeAddressObject.Label_ThreeAddrObject;
import ThreeAddressObject.LoadArguement_ThreeAddrObject;
import ThreeAddressObject.RetrieveArguement_ThreeAddrObject;
import ThreeAddressObject.ReturnValue_ThreeAddrObject;
import ThreeAddressObject.ThreeAddrObject;

import java.io.FileOutputStream;
import java.lang.String;

public class AdvancedJava {
	Scanner scan;
	ScanGrammar grammar;
	String threeAddrResult;

	varSymbol.VariableScope varScope;
	Scanner.Token lookahead;
	int tempID;
	int tlabelID; // Label id for true
	int flabelID; // Label id for false
	int rlabelID; // Label id for loops
	int retlabelID; // Label id for return
	
	Environment symbolTable;
	
	public static void main(String[] args) {
		AdvancedJava parser = new AdvancedJava();
		Scanner scan = new Scanner();
		ScanGrammar grammar = new ScanGrammar();
		String eval = "s+fun((3),(2+2),5)";
		List<Scanner.Token> evalTokens = parser.getScan(eval);
		System.out.println(parser.check_expression(evalTokens, grammar));
	}
	
	private List<Scanner.Token> getScan(String eval) {
		scan = new Scanner();
		List<Scanner.Token> evalTokens = new ArrayList<>();
		
		StringBuilder sbEval = new StringBuilder(eval);
		while(sbEval.length() != 0) {
			Scanner.Token nextToken = scan.extractToken(sbEval);
			if (nextToken != null) {
				evalTokens.add(nextToken);
			}
		}
		
		return evalTokens;
	}
	
	private AST_Node getParse(List<Scanner.Token> evalTokens) {
		grammar = new ScanGrammar();
		
		// make a syntax parsing tree
		AST_Node newRoot = null;
		newRoot = ThreeAddr_program(evalTokens);
		
		return newRoot;
	}
	
	private List<ThreeAddrObject> getTracing(AST_Node root) {
		List<ThreeAddrObject> newAddrObject= new ArrayList<>();
		
		traceSyntaxTree(newAddrObject, root);
		
		return newAddrObject;
	}

	public String getThreeAddr(String eval){
		threeAddrResult = "";
		tempID = 0;
		tlabelID = 0;
		flabelID = 0;
		rlabelID = 0;
		varScope = varSymbol.VariableScope.GLOBAL;

		List<Scanner.Token> evalTokens = getScan(eval);
		
		AST_Node root = getParse(evalTokens);
		
		List<ThreeAddrObject> addrObject = getTracing(root);
		
		for (int i = 0; i < addrObject.size(); i++) {
			threeAddrResult += addrObject.get(i).toString();
		}
		return this.threeAddrResult;
	}
	
	private void traceSyntaxTree(List<ThreeAddrObject> addrObject, 
			AST_Node node) {
		if (node instanceof AST_FUNC) {
			EnterFunctionMarker funcMarker = new EnterFunctionMarker(
					((funcSymbol)((AST_FUNC) node).getInfo()).getSpace());
			funcMarker.set_src1(((AST_FUNC) node).getInfo().getName());
			addrObject.add(funcMarker);
			
			traceFunctionStatementList(addrObject, node.get_rightChild());

			ExitFunctionMarker funcUnmarker = new ExitFunctionMarker(
				((funcSymbol)((AST_FUNC) node).getInfo()).getSpace());
			// add dest
			addrObject.add(funcUnmarker);
			return;
		} else if (node == null) {
			return;
		}
		
		traceSyntaxTree(addrObject, node.get_leftChild());
		traceSyntaxTree(addrObject, node.get_rightChild());
		return;
	}
	
	private void traceFunctionStatementList(List<ThreeAddrObject> addrObject, AST_Node node) {
		AST_FUNC_STMT_SEQ newFunctionStatementSequence = (AST_FUNC_STMT_SEQ) node;
		if (newFunctionStatementSequence.isEmpty()) {
			return;
		}
		traceStatementList(addrObject, newFunctionStatementSequence.get_leftChild());
		AST_RET returnNode = (AST_RET)newFunctionStatementSequence.get_rightChild();
		
		if (returnNode.isEmpty()) {
			return;
		}
		POT(addrObject, returnNode.get_leftChild());
		ReturnValue_ThreeAddrObject newReturn = new ReturnValue_ThreeAddrObject();
		AST_EXPR newExpr = (AST_EXPR)returnNode.get_leftChild();
		if (newExpr instanceof AST_ID) {
			if(((AST_ID)newExpr).isLocal()) {
				newReturn.set_dest(newExpr.get_str_repr(), newExpr.getOffset());
			} else {
				newReturn.set_dest(newExpr.get_str_repr());
			}
		} else {
			newReturn.set_dest(newExpr.get_temp_repr(), newExpr.getOffset());
		}
		addrObject.add(newReturn);
		return;
	}
	
	private void traceStatementList(List<ThreeAddrObject> addrObject, 
			AST_Node node) {
		if (node instanceof AST_ASSIGNMENT) {
			traceAssignment(addrObject, node);
			return;
		} else if (node instanceof AST_CONTROL_FLOW) {
			traceControlFlow(addrObject, node);
			return;
		} else if (node == null) {
			return;
		}
		
		traceStatementList(addrObject, node.get_leftChild());
		traceStatementList(addrObject, node.get_rightChild());
		return;
	}


	private void traceFunctionCall(List<ThreeAddrObject> addrObject, AST_Node node) {
		tarceLoadArguement(addrObject, node.get_leftChild());
		
		FunctionCall_ThreeAddrobject newFunctionCall = new FunctionCall_ThreeAddrobject();
		AST_FUNC_CALL functionCallNode = (AST_FUNC_CALL)node;
		newFunctionCall.set_src1(functionCallNode.getNumArgs());
		newFunctionCall.set_src2(functionCallNode.getRetLabel());
		newFunctionCall.set_dest(functionCallNode.getName());

		RetrieveArguement_ThreeAddrObject newRetrieveArguement = new RetrieveArguement_ThreeAddrObject();
		newRetrieveArguement.set_src1(functionCallNode.get_temp_repr(), functionCallNode.getOffset());
		addrObject.add(newFunctionCall);
		addrObject.add(newRetrieveArguement);
	}

	private void traceAssignment(List<ThreeAddrObject> addrObject, AST_Node subroot) {
		POT(addrObject, subroot.get_rightChild());
		ThreeAddrObject newAddrObject = new Assign_ThreeAddrObject();
		AST_ID dest = (subroot.get_leftChild() instanceof AST_VAR_DECL ? 
				(AST_ID) subroot.get_leftChild().get_leftChild() : (AST_ID) subroot.get_leftChild());
		if (dest.isLocal()) {
			newAddrObject.set_dest(dest.get_str_repr(), dest.getOffset());
		} else {
			newAddrObject.set_dest(dest.get_str_repr());
		}
		AST_EXPR src1 = (AST_EXPR)subroot.get_rightChild();
		if (src1 instanceof AST_ID) {
			if (((AST_ID)src1).isLocal()) {
				newAddrObject.set_src1(src1.get_str_repr(), src1.getOffset());
			} else {
				newAddrObject.set_src1(src1.get_str_repr());
			}
		} else {
			newAddrObject.set_src1(src1.get_temp_repr(), src1.getOffset());
		}
		addrObject.add(newAddrObject);
	}
	
	private void traceControlFlow(List<ThreeAddrObject> addrObject, AST_Node subroot) {
		int current_true_label_ID = tlabelID;
		int current_false_label_ID = flabelID;
		int current_loop_label_ID = rlabelID;

		if (subroot.tokenTypeEqual(Scanner.TokenType.WHILE)) {
			rlabelID++;
			ThreeAddrObject	loopAddrObj = new Label_ThreeAddrObject();
			loopAddrObj.set_src1("repeatLabel" + current_loop_label_ID);
			addrObject.add(loopAddrObj);
		}
		
		POT(addrObject, subroot.get_leftChild(), current_true_label_ID, 
				current_false_label_ID, "trueLabel" + current_true_label_ID);
		
		ThreeAddrObject gotoAddrObj = new Goto_ThreeAddrObject();
		gotoAddrObj.set_dest("falseLabel" + current_false_label_ID);
		
		ThreeAddrObject labelAddrObj = new Label_ThreeAddrObject();
		labelAddrObj.set_src1("trueLabel" + current_true_label_ID);

		addrObject.add(gotoAddrObj);
		addrObject.add(labelAddrObj);
		
		tlabelID++;
		flabelID++;
		
		traceStatementList(addrObject, subroot.get_rightChild());
		if (subroot.tokenTypeEqual(Scanner.TokenType.WHILE)) {
			gotoAddrObj = new Goto_ThreeAddrObject();
			gotoAddrObj.set_dest("repeatLabel" + current_loop_label_ID);
			addrObject.add(gotoAddrObj);
		}
		labelAddrObj = new Label_ThreeAddrObject();
		labelAddrObj.set_src1("falseLabel" + current_false_label_ID);
		addrObject.add(labelAddrObj);
	}
	
	
	private void tarceLoadArguement(List<ThreeAddrObject> addrObject, AST_Node node) {
		if (node instanceof AST_ARG) {
			POT(addrObject, node.get_leftChild());
			LoadArguement_ThreeAddrObject newLoadArguement = new LoadArguement_ThreeAddrObject();
			AST_EXPR newExpr = (AST_EXPR)node.get_leftChild();
			if (newExpr instanceof AST_ID) {
				if(((AST_ID)newExpr).isLocal()) {
					newLoadArguement.set_dest(newExpr.get_str_repr(), newExpr.getOffset());
				} else {
					newLoadArguement.set_dest(newExpr.get_str_repr());
				}
			} else {
				newLoadArguement.set_dest(newExpr.get_temp_repr(), newExpr.getOffset());
			}
			addrObject.add(newLoadArguement);
			return;
		} else if (node == null) {
			return;
		}
		
		tarceLoadArguement(addrObject, node.get_leftChild());
		tarceLoadArguement(addrObject, node.get_rightChild());
	}
	
	private void POT(List<ThreeAddrObject> addrObject, AST_Node node) {
		ThreeAddrObject newAddrObject;
		if (node instanceof AST_FUNC_CALL) {
			traceFunctionCall(addrObject, node);
			return;
		}
		
		if (node.get_leftChild() == null && node.get_rightChild() == null) {
			if (node.type.equals(Scanner.TokenType.NUM)) {
				newAddrObject = new Assign_ThreeAddrObject();
				newAddrObject.set_src1(((AST_NUM)node).getDigitVal());
				newAddrObject.set_dest(((AST_NUM) node).get_temp_repr(), ((AST_NUM)node).getOffset());
				addrObject.add(newAddrObject);
			}
			return;
		}
		
		POT(addrObject, node.get_leftChild());
		POT(addrObject, node.get_rightChild());
		newAddrObject = new BinaryOP_ThreeAddrObject(((AST_OP)node).get_str_repr());
		
		AST_EXPR leftChild = (AST_EXPR)node.get_leftChild();
		if (leftChild instanceof AST_ID) {
			if (((AST_ID)leftChild).isLocal()) {
				newAddrObject.set_src1(leftChild.get_str_repr(), leftChild.getOffset());
			} else {
				newAddrObject.set_src1(leftChild.get_str_repr());
			}
		} else {
			newAddrObject.set_src1(leftChild.get_temp_repr(), leftChild.getOffset());
		}

		AST_EXPR rightChild = (AST_EXPR)node.get_rightChild();
		if (rightChild instanceof AST_ID) {
			if (((AST_ID)rightChild).isLocal()) {
				newAddrObject.set_src2(rightChild.get_str_repr(), rightChild.getOffset());
			} else {
				newAddrObject.set_src2(rightChild.get_str_repr());
			}
		} else {
			newAddrObject.set_src2(rightChild.get_temp_repr(), rightChild.getOffset());
		}
		
		newAddrObject.set_dest(((AST_EXPR)node).get_temp_repr(), ((AST_EXPR)node).getOffset());
		addrObject.add(newAddrObject);
	}
	
	private void POT(List<ThreeAddrObject> addrObject, AST_Node node, 
			int trueLabel, int falseLabel, String labelDest) {
		ThreeAddrObject newAddrObject;
		
		if (node instanceof AST_FUNC_CALL) {
			traceFunctionCall(addrObject, node);
			return;
		}
		
		if (node.get_leftChild() == null && node.get_rightChild() == null) {
			if (node.type.equals(Scanner.TokenType.NUM)) {
				newAddrObject = new Assign_ThreeAddrObject();
				newAddrObject.set_src1(((AST_NUM)node).getDigitVal());
				newAddrObject.set_dest(((AST_NUM) node).get_temp_repr(), ((AST_NUM)node).getOffset());
				addrObject.add(newAddrObject);
			}
			return;
		}
		
		if (node.type.equals(Scanner.TokenType.AND)) {
			tlabelID++;
			
			ThreeAddrObject gotoAddrObj = new Goto_ThreeAddrObject();
			gotoAddrObj.set_dest("falseLabel" + falseLabel);
			
			ThreeAddrObject labelAddrObj = new Label_ThreeAddrObject();
			labelAddrObj.set_src1("trueLabel" + tlabelID);
			
			POT(addrObject, node.get_leftChild(), tlabelID, falseLabel, "trueLabel" + tlabelID);
			addrObject.add(gotoAddrObj);
			addrObject.add(labelAddrObj);
			POT(addrObject, node.get_rightChild(), trueLabel, falseLabel, labelDest);
			return;
		} else if (node.type.equals(Scanner.TokenType.OR)) {
			flabelID++;
			
			ThreeAddrObject gotoAddrObj = new Goto_ThreeAddrObject();
			gotoAddrObj.set_dest("falseLabel" + flabelID);
			
			ThreeAddrObject labelAddrObj = new Label_ThreeAddrObject();
			labelAddrObj.set_src1("falseLabel" + flabelID);
			
			POT(addrObject, node.get_leftChild(), trueLabel, flabelID, "trueLabel" + trueLabel);
			addrObject.add(gotoAddrObj);
			addrObject.add(labelAddrObj);
			POT(addrObject, node.get_rightChild(), trueLabel, falseLabel, labelDest);
			return;
		}
		
		POT(addrObject, node.get_leftChild());
		POT(addrObject, node.get_rightChild());
		
		newAddrObject = new ControlFlow_ThreeAddrObject(((AST_OP)node).get_str_repr());
		
		AST_EXPR leftChild = (AST_EXPR)node.get_leftChild();
		if (leftChild instanceof AST_ID) {
			if (((AST_ID)leftChild).isLocal()) {
				newAddrObject.set_src1(leftChild.get_str_repr(), leftChild.getOffset());
			} else {
				newAddrObject.set_src1(leftChild.get_str_repr());
			}
		} else {
			newAddrObject.set_src1(leftChild.get_temp_repr(), leftChild.getOffset());
		}

		AST_EXPR rightChild = (AST_EXPR)node.get_rightChild();
		if (rightChild instanceof AST_ID) {
			if (((AST_ID)rightChild).isLocal()) {
				newAddrObject.set_src2(rightChild.get_str_repr(), rightChild.getOffset());
			} else {
				newAddrObject.set_src2(rightChild.get_str_repr());
			}
		} else {
			newAddrObject.set_src2(rightChild.get_temp_repr(), rightChild.getOffset());
		}
		
		newAddrObject.set_dest(labelDest);
		addrObject.add(newAddrObject);
	}

	public void codeGen(String eval, String fileName) {
		tempID = 0;
		tlabelID = 0;
		flabelID = 0;
		rlabelID = 0;
		varScope = varSymbol.VariableScope.GLOBAL;
		FileOutputStream outputC = null;

		List<Scanner.Token> evalTokens = getScan(eval);
		
		AST_Node root = getParse(evalTokens);
		
		List<ThreeAddrObject> addrObject = getTracing(root);
		
		try {
			outputC = new FileOutputStream(fileName);
			
			outputC.write(("#include <stdio.h>\n" + 
					"#include <inttypes.h>\n\n" + 
					"int main(int argc, char **argv){\n").getBytes());
			
			// write globals
			outputC.write(("int64_t ").getBytes());
			List<String> varList = symbolTable.getKeyString();
			for (int i=0;i<varList.size() - 1;i++) {
				outputC.write((varList.get(i) + " = 0, ").getBytes());
			}
			outputC.write((varList.get(varList.size() - 1) + " = 0;\n").getBytes());
			
			outputC.write(("int64_t r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, va = 0;\n" + 
					"int64_t stack[100];\n" + 
					"int64_t *sp = &stack[99];\n" + 
					"int64_t *fp = &stack[99];\n" + 
					"int64_t *ra = &&exit;\n" + 
					"goto mainEntry;\n").getBytes());
			
			for (int i=0;i<addrObject.size();i++) {
				outputC.write(addrObject.get(i).toCAssembly().getBytes());
			}
			
			outputC.write(("exit:\n" + 
					"return reserved;\n" + 
					"}\n").getBytes());
			outputC.close();
		} catch (Exception e){
			System.out.println(11);
			e.getStackTrace();
		}
	}
	
	// public class id {...} is a program
	// public class id {} is a program
	// private class id {...} is a program
	// private class id {} is a program
	private AST_PRGM ThreeAddr_program (List<Scanner.Token> evalTokens) {
		if (!check_program(evalTokens)) {
			System.err.println("Invaild Program");
			System.exit(1);
		}
		
		AST_PRGM newProgram = new AST_PRGM(evalTokens.get(2));
		symbolTable = new Environment(null);
		evalTokens = evalTokens.subList(4, evalTokens.size() - 1);
		newProgram.add_leftChild(ThreeAddr_prgm_list(evalTokens));
		return newProgram;
	}
	
	private boolean check_program (List<Scanner.Token> evalTokens) {
		return evalTokens.size() >= 5 && 
				(evalTokens.get(0).tokenType.equals(Scanner.TokenType.PUBLIC) || 
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.PRIVATE)) && 
				evalTokens.get(1).tokenType.equals(Scanner.TokenType.CLASS) && 
				evalTokens.get(2).tokenType.equals(Scanner.TokenType.ID) &&
				evalTokens.get(3).tokenType.equals(Scanner.TokenType.OCB) &
				evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB);
	}
	
	// ... is a prgm_list
	// epsilon is a prgm_list
	// ...; is a var_decl
	// ...} is a function
	// ..{...}......{..{..}..} is a function
	private AST_PRGM_SEQ ThreeAddr_prgm_list (List<Scanner.Token> evalTokens) {
		if (!check_prgm_list(evalTokens)) {
			System.err.println("Invaild Program List");
			System.exit(1);
		}
		AST_PRGM_SEQ newSeq = new AST_PRGM_SEQ();
		
		int stmt_start = 0;
		for (int i=0;i<evalTokens.size();i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.END)) {
				AST_VAR_DECL var = ThreeAddr_var_decl(evalTokens.subList(stmt_start, i++));
				newSeq = newSeq.addChild(var);
				stmt_start = i;
			} else if(evalTokens.get(i).tokenType.equals(Scanner.TokenType.CCB)) {
				AST_FUNC func = ThreeAddr_func(evalTokens.subList(stmt_start, ++i));
				newSeq = newSeq.addChild(func);
				stmt_start = i--;
			} else if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.OCB)) {
				int cb_count = 1;
				for (int j=i + 1;j<evalTokens.size();j++) {
					if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.OCB)) {
						cb_count++;
					} else if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.CCB)) {
						cb_count--;
						if (cb_count == 0) {
							AST_FUNC func = ThreeAddr_func(evalTokens.subList(stmt_start, ++j));
							newSeq = newSeq.addChild(func);
							stmt_start = j--;
							i = j;
							break;
						}
					}
				}
			}
		}
		
		if (newSeq.isEmpty()) {
			return null;
		}
		return newSeq;
	}
	
	private boolean check_prgm_list (List<Scanner.Token> evalTokens) {
		if (evalTokens.isEmpty()) {
			return true;
		}
		
		int cb_count = 0;
		boolean seen_ocb = false;
		for (int i=0;i<evalTokens.size();i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.OCB)) {
				seen_ocb = true;
				cb_count++;
			} else if (seen_ocb && evalTokens.get(i).tokenType.equals(Scanner.TokenType.CCB)) {
				cb_count--;
				if (cb_count == 0) {
					seen_ocb = false;
				}
			}
		}
		return cb_count == 0 && (evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.END) ||
				evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB));
	}
	
	// int id is a var_decl
	private AST_VAR_DECL ThreeAddr_var_decl (List<Scanner.Token> evalTokens) {
		if (!check_var_decl(evalTokens)) {
			System.err.println("Invaild Variable Declaration");
			System.exit(1);
		}
		
		AST_VAR_DECL newVar = new AST_VAR_DECL(evalTokens.get(0), evalTokens.get(1));
		Scanner.Token name = evalTokens.get(1);
		
		Symbol varInfo = new varSymbol(evalTokens.get(0), name.tokenVal, 
				symbolTable.getOffset(), varScope);
		try {
			symbolTable.add(name.toString(), varInfo);
		} catch (AmbiguousVariableException ave) {
			System.err.println(ave);
			System.exit(1);
		}
		
		AST_ID newID = new AST_ID(name);
		newID.addValue(varInfo);
		newVar.add_leftChild(newID);
		return newVar;
	 }
	
	private boolean check_var_decl (List<Scanner.Token> evalTokens) {
		return evalTokens.size() == 2 && 
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT) &&
				evalTokens.get(1).tokenType.equals(Scanner.TokenType.ID);
	}

	// void id () {...} is a func
	// void id () {} is a func
	private AST_FUNC ThreeAddr_func (List<Scanner.Token> evalTokens) {
		if (!check_func(evalTokens)) {
			System.err.println("Invaild Function");
			System.exit(1);
		}
		
		varScope = varSymbol.VariableScope.LOCAL;
		Scanner.Token name = evalTokens.get(1);
		// add ret_type
		Symbol funcInfo = new funcSymbol(evalTokens.get(0), name.tokenVal);
		AST_FUNC newFunction = new AST_FUNC(name, funcInfo);
		
		// add into symbol table
		try {
			symbolTable.add(name.toString(), funcInfo);
		} catch (AmbiguousVariableException ave) {
			System.err.println(ave);
			System.exit(1);
		}
		
		// enter function scope --------------------------------------------
		symbolTable = new Environment(symbolTable);
		
		newFunction.add_leftChild(ThreeAddr_parameter(evalTokens.subList(
				3, searchFirstToken(evalTokens,Scanner.TokenType.OCB) - 1)));
		Set_parameter(newFunction.get_leftChild());
		newFunction.add_rightChild(ThreeAddr_func_stmt_list(
				evalTokens.subList(searchFirstToken(
						evalTokens,Scanner.TokenType.OCB) + 1, evalTokens.size() - 1)));
				
		Set_offset(newFunction);
		varScope = varSymbol.VariableScope.GLOBAL;
		// pass the predict space to function
		((funcSymbol)funcInfo).setSpace(symbolTable.getScopeSpaceCost());
		// initialize local scope and change the current scope to global scope
		symbolTable = symbolTable.getPrev_scope();
		return newFunction;
	}

	private void Set_parameter(AST_Node node) {
		if (node instanceof AST_PARAM) {
			AST_ID child = (AST_ID)node.get_leftChild();
			Symbol idInfo = new varSymbol(child.getInfo().getTypeToken(), child.get_str_repr(),
					child.getOffset() - symbolTable.returnParamOffset() - 3, varScope);
			child.addValue(idInfo);

			try {
				symbolTable.add(child.getID().toString(), idInfo);
			} catch (AmbiguousVariableException ave) {
				System.err.println(ave);
				System.exit(1);
			}
			
			return;
		} else if (node == null) {
			return;
		}
		
		Set_parameter(node.get_leftChild());
		Set_parameter(node.get_rightChild());
	}

	private boolean check_func (List<Scanner.Token> evalTokens) {
		if (evalTokens.size() < 6) {
			return false;
		}
		
		if (!check_ret_type(evalTokens.subList(0, 1))) {
			return false;
		}
		
		int indexOfOCB = searchFirstToken(evalTokens, Scanner.TokenType.OCB);
		if (indexOfOCB == -1) {
			return false;
		}
		
		return  evalTokens.get(1).tokenType.equals(Scanner.TokenType.ID) &&
				evalTokens.get(2).tokenType.equals(Scanner.TokenType.OP) &&
				evalTokens.get(indexOfOCB - 1).tokenType.equals(Scanner.TokenType.CP) &&
				evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB);
	}
	
	private boolean check_ret_type (List<Scanner.Token> evalTokens) {
		return evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT) || 
						evalTokens.get(0).tokenType.equals(Scanner.TokenType.VOID);
	}
	
	private void Set_offset(AST_Node node) {
		if (node instanceof AST_NUM) {
			((AST_NUM)node).setOffset(symbolTable.giveOffset());
			return;
		} else if (!(node instanceof AST_CP) && (node instanceof AST_OP)) {
			((AST_OP)node).setOffset(symbolTable.giveOffset());
		} else if (node instanceof AST_FUNC_CALL) {
			((AST_FUNC_CALL)node).setOffset(symbolTable.giveOffset());
		} else if (node == null) {
			return;
		}
		
		Set_offset(node.get_leftChild());
		Set_offset(node.get_rightChild());
	}
	
	//  |int id | int id, ... is a parameter
	private AST_PARAMETER ThreeAddr_parameter (List<Scanner.Token> evalTokens) {
		if (evalTokens.isEmpty()) {
			return new AST_PARAMETER();
		}
		
		// has one parameter ---------------------------
		int indexOfComma = searchFirstToken(evalTokens, Scanner.TokenType.COMMA);
		if (indexOfComma == -1) {
			return new AST_PARAMETER(ThreeAddr_param(evalTokens), new AST_PARAM_SEQ());
		}
		
		// has multiple parameters -------------------------------
		
		return new AST_PARAMETER(ThreeAddr_param(evalTokens.subList(0, 
				indexOfComma)), ThreeAddr_param_list(evalTokens.subList(indexOfComma, 
						evalTokens.size())));
	}

	// int id is a parameter
	private AST_PARAM ThreeAddr_param (List<Scanner.Token> evalTokens) {
		if (!check_param(evalTokens)) {
			System.err.println("Invaild Parameter");
			System.exit(1);
		}
		
		AST_PARAM newParam = new AST_PARAM(evalTokens.get(0), evalTokens.get(1));
		Scanner.Token name = evalTokens.get(1);
		
		Symbol varInfo = new varSymbol(evalTokens.get(0), name.tokenVal, 
				symbolTable.getParamOffset(), varScope);
		
		AST_ID newID = new AST_ID(name);
		newID.addValue(varInfo);
		newParam.add_leftChild(newID);
		return newParam;
 	}
	
	private boolean check_param (List<Scanner.Token> evalTokens) {
		return evalTokens.size() == 2 && 
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT) &&
				evalTokens.get(1).tokenType.equals(Scanner.TokenType.ID);
	}
	
	private AST_PARAM_SEQ ThreeAddr_param_list (List<Scanner.Token> evalTokens) {
		if (!check_param_list(evalTokens)) {
			System.err.println("Invaild Parameter List");
			System.exit(1);
		}
		
		AST_PARAM_SEQ newParamSeq = new AST_PARAM_SEQ();
		
		for (int i=0;i<evalTokens.size();i+=3) {
			newParamSeq = newParamSeq.addChild(ThreeAddr_param(
					evalTokens.subList(i+1, i+3)));
		}
		
		return newParamSeq;
 	}
	
	private boolean check_param_list (List<Scanner.Token> evalTokens) {
		if (evalTokens.size() < 3 || evalTokens.size() % 3 != 0) {
			return false;
		}
		
		for (int i=0;i<evalTokens.size();i+=3) {
			if (!evalTokens.get(i).tokenType.equals(Scanner.TokenType.COMMA)) {
				return false;
			}
		}
		
		return true;
	}
	
	private AST_FUNC_STMT_SEQ ThreeAddr_func_stmt_list(List<Scanner.Token> evalTokens) {
		if (evalTokens.isEmpty()) {
			return new AST_FUNC_STMT_SEQ();
		}
		
		// only has statement list -----------------------------
		int indexOfReturn = searchFirstToken(evalTokens,Scanner.TokenType.RETURN);
		if (indexOfReturn == -1) {
			return new AST_FUNC_STMT_SEQ(ThreeAddr_stmt_list(evalTokens),
					new AST_RET());
		}
		
		// only has return --------------------------------------
		if (indexOfReturn == 0) {
			return new AST_FUNC_STMT_SEQ(new AST_STMT_SEQ(), ThreeAddr_ret(evalTokens));
		}
		
		// has both
		return new AST_FUNC_STMT_SEQ(ThreeAddr_stmt_list(evalTokens.subList(0, indexOfReturn)),
				ThreeAddr_ret(evalTokens.subList(indexOfReturn, evalTokens.size())));
	}
	
	private AST_RET ThreeAddr_ret (List<Scanner.Token> evalTokens) {
		if (!check_ret(evalTokens)) {
			System.err.println("Invaild Return Statement");
			System.exit(1);
		}
		
		AST_RET newReturn = new AST_RET();
		newReturn.add_leftChild(ThreeAddr_expr(evalTokens.subList(1, evalTokens.size() - 1)));
		tempID = 0;
		return newReturn;
	}
	
	private boolean check_ret (List<Scanner.Token> evalTokens) {
		return evalTokens.size() >= 3 && evalTokens.get(evalTokens.size()
				- 1).tokenType.equals(Scanner.TokenType.END);
	}

	// epsilon is a stmt_list
	// ... is a stmt_list
	private AST_STMT_SEQ ThreeAddr_stmt_list(List<Scanner.Token> evalTokens) {
		if (!check_stmt_list(evalTokens)) {
			System.err.println("Invaild Statement List");
			System.exit(1);
		}
		AST_STMT_SEQ newSeq = new AST_STMT_SEQ();
		
		int stmt_start = 0;
		for (int i=0;i<evalTokens.size();i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.END) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.CCB)) {
				AST_STMT stmt = ThreeAddr_stmt(evalTokens.subList(stmt_start, ++i));
				newSeq = newSeq.addChild(stmt);
				stmt_start = i--;
			} else if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.OCB)) {
				int cb_count = 1;
				for (int j=i + 1;j<evalTokens.size();j++) {
					if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.OCB)) {
						cb_count++;
					} else if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.CCB)) {
						cb_count--;
						if (cb_count == 0) {
							AST_STMT stmt = ThreeAddr_stmt(evalTokens.subList(stmt_start, ++j));
							newSeq = newSeq.addChild(stmt);
							stmt_start = j--;
							i = j;
							break;
						}
					}
				}
			}
		}
		
		if (newSeq.isEmpty()) {
			return null;
		}
		return newSeq;
	}
	
	private boolean check_stmt_list(List<Scanner.Token> evalTokens) {
		if (evalTokens.isEmpty()) {
			return true;
		}
		
		int cb_count = 0;
		boolean seen_ocb = false;
		for (int i=0;i<evalTokens.size();i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.OCB)) {
				seen_ocb = true;
				cb_count++;
			} else if (seen_ocb && evalTokens.get(i).tokenType.equals(Scanner.TokenType.CCB)) {
				cb_count--;
				if (cb_count == 0) {
					seen_ocb = false;
				}
			}
		}
		return cb_count == 0 && (evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.END) ||
				evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB));
	}
	
	// ...} is a stmt
	// ...; is a stmt
	// ... {...} or ... {..{...}..} is a stmt
	private AST_STMT ThreeAddr_stmt(List<Scanner.Token> evalTokens) {
		if (!check_stmt(evalTokens)) {
			System.err.println("Invaild Statement");
			System.exit(1);
		}

		AST_STMT newStmt = new AST_STMT();
		if (evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT)) {
			if (evalTokens.size() <= 3) {
				newStmt.add_leftChild(ThreeAddr_var_decl(evalTokens.subList(0, evalTokens.size() - 1)));
			} else {
				newStmt.add_leftChild(ThreeAddr_assignment(evalTokens));
			}
		} else if (evalTokens.get(0).tokenType.equals(Scanner.TokenType.ID)) {
			newStmt.add_leftChild(ThreeAddr_assignment(evalTokens));
		} else if (evalTokens.get(0).tokenType.equals(Scanner.TokenType.IF) ||
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.WHILE)) {
			newStmt.add_leftChild(ThreeAddr_control_flow(evalTokens));
		}
		
		return newStmt;
	}
	
	private boolean check_stmt(List<Scanner.Token> evalTokens) {
		if (((evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT) ||
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.ID)) 
				&& evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.END)) ||
				((evalTokens.get(0).tokenType.equals(Scanner.TokenType.IF) ||
				evalTokens.get(0).tokenType.equals(Scanner.TokenType.WHILE))
				&& evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB))) {
			return true;
		}
		return false;
	}
	
	// int id = ... is an assignment
	// id = ... is an assignment
	private AST_ASSIGNMENT ThreeAddr_assignment(List<Scanner.Token> evalTokens) {
		if (!check_assignment(evalTokens)) {
			System.err.println("Invaild Assignment");
			System.exit(1);
		}
		
		AST_ASSIGNMENT newAssign = new AST_ASSIGNMENT();
		if (evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT)) {
			newAssign.addChild(ThreeAddr_var_decl(evalTokens.subList(0, 2)),
					ThreeAddr_expr(evalTokens.subList(3, evalTokens.size() - 1)));
		} else {
			Symbol variable = null;
			try {
				variable = symbolTable.get(evalTokens.get(0).toString());
			} catch (UndefinedVariableException uve) {
				System.err.println(uve);
				System.exit(1);
			}
			AST_ID newID = new AST_ID(evalTokens.get(0));
			// store value of id into the node
			newID.addValue(variable);
			
			newAssign.addChild(newID, 
					ThreeAddr_expr(evalTokens.subList(2, evalTokens.size() - 1)));
		}

		tempID = 0;
		return newAssign;
	}

	private boolean check_assignment(List<Scanner.Token> evalTokens) {
		return 	(evalTokens.get(0).tokenType.equals(Scanner.TokenType.INT) &&
				evalTokens.get(1).tokenType.equals(Scanner.TokenType.ID) &&
				evalTokens.get(2).tokenType.equals(Scanner.TokenType.ASSIGN)) || 
				(evalTokens.get(0).tokenType.equals(Scanner.TokenType.ID) && 
				evalTokens.get(1).tokenType.equals(Scanner.TokenType.ASSIGN)) && 
				(searchFirstToken(evalTokens, Scanner.TokenType.LT) == -1 &&
				searchFirstToken(evalTokens, Scanner.TokenType.LTE) == -1 &&
				searchFirstToken(evalTokens, Scanner.TokenType.GT) == -1 &&
				searchFirstToken(evalTokens, Scanner.TokenType.GTE) == -1 &&
				searchFirstToken(evalTokens, Scanner.TokenType.EQ) == -1 &&
				searchFirstToken(evalTokens, Scanner.TokenType.NE) == -1 && 
				searchFirstToken(evalTokens, Scanner.TokenType.AND) == -1 && 
				searchFirstToken(evalTokens, Scanner.TokenType.OR) == -1);
	}

	// if ... is a control_flow
	// while ... is a control_flow
	private AST_CONTROL_FLOW ThreeAddr_control_flow(List<Scanner.Token> evalTokens) {
		if (!check_control_flow(evalTokens)) {
			System.err.println("Invaild Control Flow");
			System.exit(1);
		}
		
		AST_CONTROL_FLOW newFlow = new AST_CONTROL_FLOW(evalTokens.get(0));
		
		AST_EXPR newExpr = ThreeAddr_expr(evalTokens.subList(2, 
				searchFirstToken(evalTokens, Scanner.TokenType.OCB) - 1));
		tempID = 0;
		
		newFlow.addChild(newExpr,
				ThreeAddr_stmt_list(evalTokens.subList(searchFirstToken(
						evalTokens, Scanner.TokenType.OCB) + 1, 
						evalTokens.size() - 1)));
		return newFlow;
	}
	  
	private boolean check_control_flow(List<Scanner.Token> evalTokens) {
		if (evalTokens.size() < 6) {
			return false;
		}
		
		int indexOfOCB = searchFirstToken(evalTokens, Scanner.TokenType.OCB);
		// ...} is not a control flow
		if (indexOfOCB == -1) {
			return false;
		}
		
		boolean seen_ce = false;
		for (int i = 2 ;i < searchFirstToken(evalTokens, Scanner.TokenType.OCB) - 1;i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.LT) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.LTE) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.GT) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.GTE) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.EQ) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.NE)) {
				if (!seen_ce) {
					seen_ce = true;
				} else {
					System.err.println("Boolean value is incomparable to an integer");
					System.exit(1);
				}
			} else if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.OR) ||
					evalTokens.get(i).tokenType.equals(Scanner.TokenType.AND)) {
				if (seen_ce) {
					seen_ce = false;
				} else {
					System.err.println("Cannot combine non-boolean value");
					System.exit(1);
				}
			}
		}
		
		return seen_ce && evalTokens.get(1).tokenType.equals(Scanner.TokenType.OP) &&
				evalTokens.get(indexOfOCB - 1).tokenType.equals(Scanner.TokenType.CP) &&
				evalTokens.get(evalTokens.size() - 1).tokenType.equals(Scanner.TokenType.CCB);
	}
	
	private int searchFirstToken(List<Scanner.Token> list, Scanner.TokenType tokenType) {
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).tokenType.equals(tokenType)) {
				return i;
			}
		}
		return index;
	}
	
	private AST_EXPR ThreeAddr_expr(List<Scanner.Token> evalTokens) {
		if (!check_expression(evalTokens, grammar)) {
			System.err.println("Invaild Expression");
			System.exit(1);
		}
		
		Iterator<Scanner.Token> list = evalTokens.iterator();
		if (list.hasNext()) {
			lookahead = list.next();
		}
		AST_EXPR newNode = ThreeAddr_BCE(list);
		
		symbolTable.set_tempID(tempID);
		return newNode;
	}

	private boolean check_expression(List<Scanner.Token> evalTokens, 
			ScanGrammar grammar) {
		if (!checkSymbolcount(evalTokens, Scanner.TokenType.OP, Scanner.TokenType.CP)) {
			return false;
		}
		
		if (!grammar.first("expr").contains(evalTokens.get(0).toGrammarFont())) {
			return false;
		}
		
		for (int i = 1; i < evalTokens.size(); i++) {
			if (!grammar.follow(evalTokens.get(i - 1).toGrammarFont()
					).contains(evalTokens.get(i).toGrammarFont())) {
				return false;
			}
		}

		return grammar.follow(evalTokens.get(evalTokens.size() - 1).toGrammarFont()
				).contains("$");
	}

	private boolean checkSymbolcount(List<Scanner.Token> evalTokens, Scanner.TokenType open, Scanner.TokenType close) {
		int count = 0;
		for (int i=0;i<evalTokens.size();i++) {
			// special case id ( )
			if (evalTokens.get(i).tokenType.equals(open) && i>0 && 
					evalTokens.get(i-1).tokenType.equals(Scanner.TokenType.ID) &&
					i<evalTokens.size()-1 && evalTokens.get(i+1).tokenType.equals(
							close)) {
				i += 1;
				continue;
			}
			// not allowed ()
			if (evalTokens.get(i).tokenType.equals(open) && i<evalTokens.size()-1
					&& evalTokens.get(i+1).tokenType.equals(close)) {
				return false;
			}
			
			if (evalTokens.get(i).tokenType.equals(open)) {
				count += 1;
			} else if (evalTokens.get(i).tokenType.equals(close)) {
				count -= 1;
				if (count < 0) {
					return false;
				}
			}
		}
		return count == 0;
	}
	
	private AST_EXPR ThreeAddr_BCE(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = ThreeAddr_EQ(list);
		
		while (list.hasNext()) {
			if (lookahead.tokenType.equals(Scanner.TokenType.AND)) {
				AST_Node node = newNode;
				// Scanner.TokenType.AND, "&&"
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_EQ(list));
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.OR)) {
				AST_Node node = newNode;
				// Scanner.TokenType.OR, "||"
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_EQ(list));
				continue;
			}
			break;
		}
		return newNode;
	}
	
	private AST_EXPR ThreeAddr_EQ(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = ThreeAddr_CE(list);
		
		while (list.hasNext()) {
			if (lookahead.tokenType.equals(Scanner.TokenType.EQ)) {
				AST_Node node = newNode;
				// Scanner.TokenType.EQ, "=="
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_CE(list));
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.NE)) {
				AST_Node node = newNode;
				// Scanner.TokenType.NE, "!="
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_CE(list));
				continue;
			}
			break;
		}
		return newNode;
	}
	
	private AST_EXPR ThreeAddr_CE(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = ThreeAddr_E(list);
		
		while (list.hasNext()) {
			if (lookahead.tokenType.equals(Scanner.TokenType.LT)) {
				AST_Node node = newNode;
				// Scanner.TokenType.LT, "<"
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_E(list));
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.GT)) {
				AST_Node node = newNode;
				// Scanner.TokenType.GT, ">"
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_E(list));
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.LTE)) {
				AST_Node node = newNode;
				// Scanner.TokenType.LTE, <=
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_E(list));
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.GTE)) {
				AST_Node node = newNode;
				// Scanner.TokenType.GTE, >=
				newNode = new AST_CP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_E(list));
				continue;
			}
			break;
		}
		return newNode;
	}

	private AST_EXPR ThreeAddr_E(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = ThreeAddr_T(list);
		
		while (list.hasNext()) {
			if (lookahead.tokenType.equals(Scanner.TokenType.PLUS)) {
				AST_Node node = newNode;
				// Scanner.TokenType.PLUS, +
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_T(list));
				newNode.setTempID(tempID++);
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.MINUS)) {
				AST_Node node = newNode;
				// Scanner.TokenType.MINUS, -
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_T(list));
				newNode.setTempID(tempID++);
				continue;
			}
			break;
		}
		return newNode;
	}

	private AST_EXPR ThreeAddr_T(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = ThreeAddr_F(list);
		
		while (list.hasNext()) {
			if (lookahead.tokenType.equals(Scanner.TokenType.MUL)) {
				AST_Node node = newNode;
				// Scanner.TokenType.MUL, *
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_F(list));
				newNode.setTempID(tempID++);
				continue;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.DIV)) {
				AST_Node node = newNode;
				// Scanner.TokenType.DIV, "/"
				newNode = new AST_OP(lookahead.tokenType, lookahead.tokenVal);
				lookahead = list.next();
				newNode.add_leftChild(node);
				newNode.add_rightChild(ThreeAddr_F(list));
				newNode.setTempID(tempID++);
				continue;
			}
			break;
		}
		return newNode;
	}

	private AST_EXPR ThreeAddr_F(Iterator<Scanner.Token> list) {
		AST_EXPR newNode = null;
		
		if (lookahead.tokenType.equals(Scanner.TokenType.OP)) {
			lookahead = list.next();
			newNode = ThreeAddr_BCE(list);
			
			// not sure if ... follows ), e.g. )1
			if (list.hasNext()) {
				lookahead = list.next();
			}
		} else if (lookahead.tokenType.equals(Scanner.TokenType.ID)) {
			Symbol variable = null;
			try {
				variable = symbolTable.get(lookahead.toString());
			} catch (UndefinedVariableException uve) {
				System.out.println(lookahead.toString());
				System.err.println(uve);
				System.exit(1);
			}
			newNode = new AST_ID(lookahead);
			// store value of id into the node
			((AST_ID)newNode).addValue(variable);
			
			// not sure if ... follows id, e.g. x+
			if (list.hasNext()) {
				lookahead = list.next();
				if (lookahead.tokenType.equals(Scanner.TokenType.OP)) {
					newNode = ThreeAddr_func_call(list, (AST_ID)newNode);
					if (list.hasNext()) {
						lookahead = list.next();
					}
				}
			}
		} else if (lookahead.tokenType.equals(Scanner.TokenType.NUM)) {
			newNode = new AST_NUM(tempID++, 
					Integer.valueOf(lookahead.tokenVal));
			
			// not sure if ... follows num, e.g. 1+
			if (list.hasNext()) {
				lookahead = list.next();
			}
		}
		return newNode;
	}

	private AST_FUNC_CALL ThreeAddr_func_call(Iterator<Scanner.Token> list,
			AST_ID funcName) {
		List<Scanner.Token> evalTokens = get_argument(list);
		if (evalTokens.isEmpty()) {
			return new AST_FUNC_CALL(tempID++, retlabelID++, funcName);
		}

		AST_FUNC_CALL newFuncCall = new AST_FUNC_CALL(retlabelID++, funcName, ThreeAddr_argument(evalTokens));
		newFuncCall.setTempID(tempID++);
		return newFuncCall;
	}

	// move list pointer on CP and turn the middle part into a list of tokens
	private List<Scanner.Token> get_argument(Iterator<Scanner.Token> list) {
		List<Scanner.Token> newEvalTokens = new ArrayList<>();
		lookahead = list.next();
		if (lookahead.tokenType.equals(Scanner.TokenType.CP)) {
			return newEvalTokens;
		}
		int numOP = 1;
		while (list.hasNext() && numOP > 0) {
			newEvalTokens.add(lookahead);
			lookahead = list.next();
			if (lookahead.tokenType.equals(Scanner.TokenType.OP)) {
				numOP += 1;
			} else if (lookahead.tokenType.equals(Scanner.TokenType.CP)) {
				numOP -= 1;
			}
		}
		return newEvalTokens;
	}
	
	private int findNextComma (List<Scanner.Token> evalTokens) {
		for (int i=0;i<evalTokens.size();i++) {
			if (i + 2 < evalTokens.size()) {
				if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.ID)) {
					if (evalTokens.get(i+1).tokenType.equals(Scanner.TokenType.OP)) {
						int count = 1;
						for (int j = i + 2; j < evalTokens.size(); j++) {
							if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.OP)) {
								count += 1;
							} else if (evalTokens.get(j).tokenType.equals(Scanner.TokenType.CP)) {
								count -= 1;
							} 
							if (count == 0) {
								i = j;
								break;
							}
						}
					}
				}
			} 
			
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.COMMA)) {
				return i;
			}
		}
		return -1;
	}
	
	private AST_ARGUMENT ThreeAddr_argument(List<Scanner.Token> evalTokens) {
		int indexOfComma = findNextComma(evalTokens); 
		if (indexOfComma == -1) {
			return new AST_ARGUMENT(ThreeAddr_arg(evalTokens), new AST_ARG_SEQ());
		}
		
		return new AST_ARGUMENT(ThreeAddr_arg(evalTokens.subList(0, indexOfComma)),
				ThreeAddr_arg_list(evalTokens.subList(indexOfComma, evalTokens.size())));
	}
	
	private AST_ARG	ThreeAddr_arg(List<Scanner.Token> evalTokens) {
		AST_ARG newArg = new AST_ARG();
		
		newArg.add_leftChild(ThreeAddr_expr(evalTokens));
		tempID = 0;
		return newArg;
	}
	
	private AST_ARG_SEQ	ThreeAddr_arg_list(List<Scanner.Token> evalTokens) {
		if (!check_arg_list(evalTokens)) {
			System.err.println("Invaild Argument List");
			System.exit(1);
		}
		
		AST_ARG_SEQ newArgSeq = new AST_ARG_SEQ();
		
		evalTokens = evalTokens.subList(1, evalTokens.size());
		
		for (int i = findNextComma(evalTokens); i != -1; i = findNextComma(evalTokens)) {
			newArgSeq = newArgSeq.addChild(ThreeAddr_arg(evalTokens.subList(0, i)));
			evalTokens = evalTokens.subList(i + 1, evalTokens.size());
		}
		newArgSeq = newArgSeq.addChild(ThreeAddr_arg(evalTokens.subList(0,
				evalTokens.size())));
		
		return newArgSeq;
	}
	// , ... , ...
	private boolean check_arg_list(List<Scanner.Token> evalTokens) {
		boolean seen_comma = false;
		
		for (int i=0;i<evalTokens.size();i++) {
			if (evalTokens.get(i).tokenType.equals(Scanner.TokenType.COMMA)) {
				if (seen_comma == false) {
					seen_comma = true;
				} else {
					return false;
				}
			} else {
				seen_comma = false;
			}
		}
		return seen_comma == false;
	}
}
