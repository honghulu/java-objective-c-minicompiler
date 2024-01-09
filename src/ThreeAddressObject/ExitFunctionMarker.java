package ThreeAddressObject;

public class ExitFunctionMarker extends Goto_ThreeAddrObject {
	int stack_space;
	//add return later
	
	public ExitFunctionMarker(int totalSpaceCost) {
		super();
		stack_space = totalSpaceCost;
	}
	
	// "GOTO xx"
	public String toCAssembly() {
		return "sp = sp + " + stack_space + ";\n" + // '#' of locals/temps for function, Shrink the stack
				"fp = *(sp+2);\n" + // Restore fp
				"ra = *(sp+1);\n" + // Restore ra
				"sp = sp + 2;\n" + // Shrink stack again
				"goto *ra;\n"; // Return back to who ever called us";;
	}
	
	public String toString() {
		return "";
	}
}
