package SymbolTable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Exceptions.AmbiguousVariableException;
import Exceptions.UndefinedVariableException;
import Scanners.Scanner;
import Scanners.Scanner.Token;

public class Environment {
	private Map<String, Symbol> current_scope;
	private Environment prev_scope;
	private int param_space;
	private int variable_space;
	private int max_tempID;
	
	public Environment(Environment prev) {
		current_scope = new TreeMap<>();
		setPrev_scope(prev);
		param_space = 0;
		variable_space = 0;
		max_tempID = 0;
	}
	
	public void add(String id, Symbol sym) throws AmbiguousVariableException{
		// declare same key twice causes error
		if (current_scope.containsKey(id)) {
			if (current_scope.get(id) instanceof varSymbol) {
				throw new AmbiguousVariableException("Variable");
			} else {
				throw new AmbiguousVariableException("Function");
			}
		}
		
		current_scope.put(id, sym);
	}
	
	public Symbol get(String id) throws UndefinedVariableException{
		for (Environment env = this; env != null; env = env.getPrev_scope()) {
			Symbol sym_found = null;
			// get token which is not on the symbol table
			try {
				sym_found = env.current_scope.get(id);
			} catch (NullPointerException npe) {
				continue;
			}

			if (sym_found != null) {
				return sym_found;
			}
		}
		throw new UndefinedVariableException();
	}

	public Environment getPrev_scope() {
		return prev_scope;
	}

	public void setPrev_scope(Environment prev_scope) {
		this.prev_scope = prev_scope;
	}
	
	public List<String> getKeyString(){
		List<String> newString = new ArrayList<>();
		
		for (Symbol value : current_scope.values()) {
			if (value instanceof varSymbol) {
				newString.add(value.getName());
			}
		}
		newString.sort(new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
			
		});
		return newString;
	}
	
	public void set_tempID(int given_maxTempID) {
		if (given_maxTempID > max_tempID) {
			max_tempID = given_maxTempID;
		}
	}
	
	public int giveOffset() {
		return variable_space;
	}
	
	public int getOffset() {
		return ++variable_space;
	}
	
	public int getParamOffset() {
		return ++param_space;
	}
	
	public int returnParamOffset() {
		return param_space;
	}
	
	public int getScopeSpaceCost() {
		return max_tempID + variable_space;
	}
}
