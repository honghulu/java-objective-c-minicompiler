package ThreeAddressObject;

public class LoadArguement_ThreeAddrObject extends ThreeAddrObject {
	public LoadArguement_ThreeAddrObject() {
		super();
	}
	
	// dest is local var
	public void set_dest(String strVal, int given_offset) {
		dest = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// dest is global var
	public void set_dest(String strVal) {
		dest = new Global_Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return "sp = sp - 1;\n" +
				"*(sp+1) = " + dest.toCString() + ";\n";
	}
	
	public String toString() {
		return "param " + dest.toString() + "\n";
	}
}
