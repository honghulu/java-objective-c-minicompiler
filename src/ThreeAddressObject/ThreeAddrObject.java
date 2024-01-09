package ThreeAddressObject;

public abstract class ThreeAddrObject {
	protected String str_repr;
	
	protected Operand src1;
	protected Operand src2;
	protected Operand dest;
	
	public void set_src1(int constant) {
	}
	
	public void set_src1(String strVal) {
	}
	
	public void set_src1(String strVal, int given_offset) {
	}
	
	public void set_src2(int constant) {
	}
	
	public void set_src2(String strVal) {
	}
	
	public void set_src2(String strVal, int given_offset) {
	}
	
	public void set_dest(int constant) {
	}
	
	public void set_dest(String strVal) {
	}
	
	public void set_dest(String strVal, int given_offset) {
	}
	
	public String toCAssembly() {
		return "";
	}
	
	public String toString() {
		return "";
	}
}
