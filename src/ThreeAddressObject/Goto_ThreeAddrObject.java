package ThreeAddressObject;

public class Goto_ThreeAddrObject extends ThreeAddrObject {

	public Goto_ThreeAddrObject() {
		super();
	}
	
	// dest is speical var
	public void set_dest(String strVal) {
		dest = new Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return "goto " + dest.toCString() + ";\n";
	}
	
	public String toString() {
		return "GOTO: " + dest.toString() + "\n";
	}
}
