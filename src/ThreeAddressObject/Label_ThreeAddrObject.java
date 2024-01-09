package ThreeAddressObject;

public class Label_ThreeAddrObject extends ThreeAddrObject {

	public Label_ThreeAddrObject() {
		super();
	}
	
	// src1 is special var
	public void set_src1(String strVal){
		src1 = new Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return src1.toCString() + ":\n";
	}
	
	public String toString() {
		return src1.toString() + "\n";
	}
}
