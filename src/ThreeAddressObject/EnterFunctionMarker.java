package ThreeAddressObject;

public class EnterFunctionMarker extends Label_ThreeAddrObject {
	int stack_space;
	
	public EnterFunctionMarker(int totalSpaceCost) {
		super();
		stack_space = totalSpaceCost;
	}
	
	// "Label xx"
	public String toCAssembly() {
		return src1.toCString() + ":\n" + // Label for the function name
				"sp = sp - 2;\n" + // Grow the stack enough to save fp and ra
				"*(sp+2) = fp;\n" +  // Save fp (int64_t is large enough to save pointer value)
				"*(sp+1) = ra;\n" +  // Save ra
				"fp = sp;\n" + // Move frame pointer for current frame
				"sp = sp - " + stack_space + ";\n"; //'#' of locals/temps for function, Create space for locals/temps;
	}
	
	public String toString() {
		return "";
	}
}
