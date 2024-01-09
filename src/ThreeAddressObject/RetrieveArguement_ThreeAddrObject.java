package ThreeAddressObject;

public class RetrieveArguement_ThreeAddrObject extends ThreeAddrObject {
	public RetrieveArguement_ThreeAddrObject() {
		super();
	}
	
	// src1 is always local var
	public void set_src1(String strVal, int given_offset) {
		src1 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	public String toCAssembly() {
		return src1.toCString() + " = va;\n";
	}
	
	public String toString() {
		return "retrieve " + src1.toString() + "\n";
	}
}
