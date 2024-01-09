import static org.junit.Assert.assertTrue;

import java.io.*;
import java.util.*;

import org.junit.Test;

public class AdvancedJavaTest{

	@Test
	public static void TestThreeAddrGen(){
	    System.out.println("*******************************************");
	    System.out.println("Testing Three Address Generation");
	
	    String eval = "public class test {int y; int x; void main() { x = 12; y = 8 * (9-3); if(y < x && y != x){ } } }\n";
	    AdvancedJava parser = new AdvancedJava();
	    String result = "temp0 = 12\n" + 
	    		"x = temp0\n" + 
	    		"temp0 = 8\n" + 
	    		"temp1 = 9\n" + 
	    		"temp2 = 3\n" + 
	    		"temp3 = temp1 - temp2\n" + 
	    		"temp4 = temp0 * temp3\n" + 
	    		"y = temp4\n" + 
	    		"IF_LT: y, x, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"IF_NE: y, x, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 0 pass!");
	    
	    eval = "public class test { void main() {int x = 9+4;}}\n";
	    parser = new AdvancedJava();
	    result = "temp0 = 9\n" + 
	    		"temp1 = 4\n" + 
	    		"temp2 = temp0 + temp1\n" +
	    		"x = temp2\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 1 pass!");
	    
	    eval = "public class test {void main() {int x = 101 - 2;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 101\n" + 
	    		"temp1 = 2\n" + 
	    		"temp2 = temp0 - temp1\n" + 
	    		"x = temp2\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 2 pass!");
	    
	    eval = "public class test {void main() {int x = 17 * 8;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 17\n" + 
	    		"temp1 = 8\n" + 
	    		"temp2 = temp0 * temp1\n" + 
	    		"x = temp2\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 3 pass!");
	    
	    eval = "public class test {void main() {int x = 10 * 10;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 10\n" + 
	    		"temp1 = 10\n" + 
	    		"temp2 = temp0 * temp1\n" + 
	    		"x = temp2\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 4 pass!");
	    
	    eval = "public class test {void main() {int x = 5 - 3 * 7;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 5\n" + 
	    		"temp1 = 3\n" + 
	    		"temp2 = 7\n" + 
	    		"temp3 = temp1 * temp2\n" + 
	    		"temp4 = temp0 - temp3\n" + 
	    		"x = temp4\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 5 pass!");
	    
	    eval = "public class test {void main() {int x = 5 - 3 * 7 - 4;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 5\n" + 
	    		"temp1 = 3\n" + 
	    		"temp2 = 7\n" + 
	    		"temp3 = temp1 * temp2\n" + 
	    		"temp4 = temp0 - temp3\n" + 
	    		"temp5 = 4\n" + 
	    		"temp6 = temp4 - temp5\n" + 
	    		"x = temp6\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 6 pass!");
	    
	    eval = "public class test {void main() {int x = 2 * 8 / 4;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 2\n" + 
	    		"temp1 = 8\n" + 
	    		"temp2 = temp0 * temp1\n" + 
	    		"temp3 = 4\n" + 
	    		"temp4 = temp2 / temp3\n" + 
	    		"x = temp4\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 7 pass!");
	    
	    eval = "public class test {void main() {int x = 3 + (4 - 9);}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"temp2 = 9\n" + 
	    		"temp3 = temp1 - temp2\n" + 
	    		"temp4 = temp0 + temp3\n" + 
	    		"x = temp4\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 8 pass!");
	    
	    eval = "public class test {void main() {}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 9 pass!");
	    
	    eval = "public class test {void main() {int xx = 3;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"xx = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 10 pass!");
	    
	    eval = "public class test {void main() {int xx = 3;\n" + 
	    		"int yx = 12;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"xx = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"yx = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 11 pass!");
	    
	    eval = "public class test {void main() {int xx = 3;\n" + 
	    		"int yx = xx;}}";
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"xx = temp0\n" + 
	    		"yx = xx\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 12 pass!");
	    
	    eval = "public class test {void main() {int xx = 3 + 4 * 19 / (2 + 4);\n" + 
	    		"\n" + 
	    		"int yx = xx * 15;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"temp2 = 19\n" + 
	    		"temp3 = temp1 * temp2\n" + 
	    		"temp4 = 2\n" + 
	    		"temp5 = 4\n" + 
	    		"temp6 = temp4 + temp5\n" + 
	    		"temp7 = temp3 / temp6\n" + 
	    		"temp8 = temp0 + temp7\n" + 
	    		"xx = temp8\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = xx * temp0\n" + 
	    		"yx = temp1\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 13 pass!");
	    
	    eval = "public class test {void main() {int xx = 3; int yooo = 12; int t11 = 15; int names23 = 9; int h2t4y78e = 43;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"xx = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"yooo = temp0\n" + 
	    		"temp0 = 15\n" + 
	    		"t11 = temp0\n" + 
	    		"temp0 = 9\n" + 
	    		"names23 = temp0\n" + 
	    		"temp0 = 43\n" + 
	    		"h2t4y78e = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 14 pass!");
	    
	    eval = "public class test {void main() { if (3 < 4){}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 15 pass!");
	
	    eval = "public class test {void main() { if (3 <= 4){int zebra12 = 88;\n" + 
	    		"\n" + 
	    		"}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LTE: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 88\n" + 
	    		"zebra12 = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 16 pass!");
	    
	    eval = "public class test {void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;\n" + 
	    		"\n" + 
	    		"}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 57\n" + 
	    		"te3 = temp0\n" + 
	    		"temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 88\n" + 
	    		"zebra12 = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 17 pass!");
	    
	    eval = "public class test {void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;\n" + 
	    		"\n" + 
	    		"} if(9082 >= te3){}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 57\n" + 
	    		"te3 = temp0\n" + 
	    		"temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 88\n" + 
	    		"zebra12 = temp0\n" + 
	    		"falseLabel0\n" + 
	    		"temp0 = 9082\n" + 
	    		"IF_GTE: temp0, te3, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"falseLabel1\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 18 pass!");
	    
	    eval = "public class test {void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;\n" + 
	    		"\n" + 
	    		"} if(9082 >= te3){}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 57\n" + 
	    		"te3 = temp0\n" + 
	    		"temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 88\n" + 
	    		"zebra12 = temp0\n" + 
	    		"falseLabel0\n" + 
	    		"temp0 = 9082\n" + 
	    		"IF_GTE: temp0, te3, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"falseLabel1\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 19 pass!");
	    
	    eval = "public class test {int te3; void main() {if (3 < 4){if(9082 >= te3){}}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 9082\n" + 
	    		"IF_GTE: temp0, te3, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 20 pass!");
	    
	    eval = "public class test {int te3; int y4t; void main() {if (3 < 4){if(9082 >= te3){}} int y4t = 34;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"temp1 = 4\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 9082\n" + 
	    		"IF_GTE: temp0, te3, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"falseLabel0\n" + 
	    		"temp0 = 34\n" + 
	    		"y4t = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 21 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) {}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 22 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) {int yeeee7 = 44;}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 44\n" + 
	    		"yeeee7 = temp0\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 23 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) { if(9 == 8){} }}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 8\n" + 
	    		"IF_EQ: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 24 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) { if(9 == 8){int zx = 98;} }}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 8\n" + 
	    		"IF_EQ: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"temp0 = 98\n" + 
	    		"zx = temp0\n" + 
	    		"falseLabel1\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 25 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) { while(9 == 8){}}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"repeatLabel1\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 8\n" + 
	    		"IF_EQ: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"GOTO: repeatLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 26 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) { while(9 == 8){ if(2 != 9){} }}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"repeatLabel1\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 8\n" + 
	    		"IF_EQ: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"temp0 = 2\n" + 
	    		"temp1 = 9\n" + 
	    		"IF_NE: temp0, temp1, trueLabel2\n" + 
	    		"GOTO: falseLabel2\n" + 
	    		"trueLabel2\n" + 
	    		"falseLabel2\n" + 
	    		"GOTO: repeatLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 27 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98) { while(9 == 8){ if(2 != 9){} } if(9 < 2){} }}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"repeatLabel1\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 8\n" + 
	    		"IF_EQ: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"temp0 = 2\n" + 
	    		"temp1 = 9\n" + 
	    		"IF_NE: temp0, temp1, trueLabel2\n" + 
	    		"GOTO: falseLabel2\n" + 
	    		"trueLabel2\n" + 
	    		"falseLabel2\n" + 
	    		"GOTO: repeatLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"temp0 = 9\n" + 
	    		"temp1 = 2\n" + 
	    		"IF_LT: temp0, temp1, trueLabel3\n" + 
	    		"GOTO: falseLabel3\n" + 
	    		"trueLabel3\n" + 
	    		"falseLabel3\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 28 pass!");
	    
	    eval = "public class test {void main() {while((17 - (9 + 2)) < 17) {}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 17\n" + 
	    		"temp1 = 9\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = temp1 + temp2\n" + 
	    		"temp4 = temp0 - temp3\n" + 
	    		"temp5 = 17\n" + 
	    		"IF_LT: temp4, temp5, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 29 pass!");
	    
	    eval = "public class test { int x; int y; void main(){} void main2(){} int z;}";	
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 30 pass!");
	    
	    eval = "private class test { void main2(){} int x; int y; void main(){}}";	
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 31 pass!");
	    
	    eval = "private class test { void main2(){} int x; int y; void main(){}}";	
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 32 pass!");
	    
	    eval = "public class test { void main2(){int x = 3; x = 42;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 3\n" + 
	    		"x = temp0\n" + 
	    		"temp0 = 42\n" + 
	    		"x = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 33 pass!");
	    
	    eval = "private class test { void main(){int x = 45; int y = 27; x = y; } }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 45\n" + 
	    		"x = temp0\n" + 
	    		"temp0 = 27\n" + 
	    		"y = temp0\n" + 
	    		"x = y\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 34 pass!");
	    
	    eval = "private class test { int z; void main2(){ z = 12 ; } }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 12\n" + 
	    		"z = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 35 pass!");
	    
	    eval = "private class test { int z; void main2(){ z = 12 ; } }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 12\n" + 
	    		"z = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 36 pass!");
	    
	    eval = "private class test { int z; void main2(){z = 14;} int x; void main3(){x = 12; z = x;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 14\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"x = temp0\n" + 
	    		"z = x\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 37 pass!");
	    
	    eval = "public class test { int t; void main2(){t = 12; int t; t = 14;} void main(){int z; z = t;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 12\n" + 
	    		"t = temp0\n" + 
	    		"temp0 = 14\n" + 
	    		"t = temp0\n" + 
	    		"z = t\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 38 pass!");
	    
	    eval = "private class test { void one(){int z = 12;}\n" + 
	    		"int z; void two(){z = 15;}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 12\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 15\n" + 
	    		"z = temp0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 39 pass!");
	    
	    eval = "private class test { int z; void main(){z = 19; if(z < 12){}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 19\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"IF_LT: z, temp0, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 40 pass!");
	    
	    eval = "private class test { int z; void main(){z = 19; if(z < 12){z = 8;}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 19\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"IF_LT: z, temp0, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 8\n" + 
	    		"z = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 41 pass!");
	    
	    eval = "public class test { int z; void main(){z = 19; if(z < 12 && 3 <= 2){z = 8;}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 19\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"IF_LT: z, temp0, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"temp1 = 3\n" + 
	    		"temp2 = 2\n" + 
	    		"IF_LTE: temp1, temp2, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 8\n" + 
	    		"z = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 42 pass!");
	    
	    eval = "private class test { int z; void main(){z = 19; if(z < 12 || 3 <= 2){z = 8;}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 19\n" + 
	    		"z = temp0\n" + 
	    		"temp0 = 12\n" + 
	    		"IF_LT: z, temp0, trueLabel0\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"temp1 = 3\n" + 
	    		"temp2 = 2\n" + 
	    		"IF_LTE: temp1, temp2, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 8\n" + 
	    		"z = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 43 pass!");
	    
	    eval = "public class test {void main() {if(15 < 98 && (2 < 3)) {}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = 3\n" + 
	    		"IF_LT: temp2, temp3, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 44 pass!");
	    
	    eval = "public class test {void main() {if(15 < 98 && 2 < 3 || 4 > 5) {}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel1\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = 3\n" + 
	    		"IF_LT: temp2, temp3, trueLabel0\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"temp4 = 4\n" + 
	    		"temp5 = 5\n" + 
	    		"IF_GT: temp4, temp5, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 45 pass!");
	    
	    eval = "public class test {void main() {while(15 < 98 && 2 < 3) {}}}";	
	    parser = new AdvancedJava();
	    result = "repeatLabel0\n" + 
	    		"temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = 3\n" + 
	    		"IF_LT: temp2, temp3, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"GOTO: repeatLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 46 pass!");
	    
	    eval = "public class test {void main() {if(15 < 98 && (2 < 3)) {if(2 >3 || 15 > 98){}}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = 3\n" + 
	    		"IF_LT: temp2, temp3, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 2\n" + 
	    		"temp1 = 3\n" + 
	    		"IF_GT: temp0, temp1, trueLabel2\n" + 
	    		"GOTO: falseLabel2\n" + 
	    		"falseLabel2\n" + 
	    		"temp2 = 15\n" + 
	    		"temp3 = 98\n" + 
	    		"IF_GT: temp2, temp3, trueLabel2\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"trueLabel2\n" + 
	    		"falseLabel1\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 47 pass!");
	    
	    eval = "public class test {int y; int x; void main() { x = 12; y = 8 * (9-3); if(y < x && y != x){ } } }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 12\n" + 
	    		"x = temp0\n" + 
	    		"temp0 = 8\n" + 
	    		"temp1 = 9\n" + 
	    		"temp2 = 3\n" + 
	    		"temp3 = temp1 - temp2\n" + 
	    		"temp4 = temp0 * temp3\n" + 
	    		"y = temp4\n" + 
	    		"IF_LT: y, x, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"IF_NE: y, x, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 48 pass!");
	    
	    eval = "public class test {void main() { if(15 < 98 && 2 < 3 && 3 < 4){ } } }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel2\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel2\n" + 
	    		"temp2 = 2\n" + 
	    		"temp3 = 3\n" + 
	    		"IF_LT: temp2, temp3, trueLabel1\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel1\n" + 
	    		"temp4 = 3\n" + 
	    		"temp5 = 4\n" + 
	    		"IF_LT: temp4, temp5, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 49 pass!");
	    
	    eval = "public class test {void main() {if(15 < 98) {int z;}}}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 15\n" + 
	    		"temp1 = 98\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 50 pass!");
	    
	    eval = "public class test {int z; int z2; int z3; int z4; int z5; int z6; int z7;}";	
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 51 pass!");
	    
	    eval = "public class test { int x; int y; int reserved; int mainEntry() { reserved = 0; if(2 < 3) {reserved = 42;}} }";	
	    parser = new AdvancedJava();
	    result = "temp0 = 0\n" + 
	    		"reserved = temp0\n" + 
	    		"temp0 = 2\n" + 
	    		"temp1 = 3\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 42\n" + 
	    		"reserved = temp0\n" + 
	    		"falseLabel0\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 52 pass!");
	    
	    eval = "public class test { void mainEntry() { int res = 14; if(2 < 3 || 9 < 10) {res = 42;} res = res + 1; }}";	
	    parser = new AdvancedJava();
	    result = "temp0 = 14\n" + 
	    		"res = temp0\n" + 
	    		"temp0 = 2\n" + 
	    		"temp1 = 3\n" + 
	    		"IF_LT: temp0, temp1, trueLabel0\n" + 
	    		"GOTO: falseLabel1\n" + 
	    		"falseLabel1\n" + 
	    		"temp2 = 9\n" + 
	    		"temp3 = 10\n" + 
	    		"IF_LT: temp2, temp3, trueLabel0\n" + 
	    		"GOTO: falseLabel0\n" + 
	    		"trueLabel0\n" + 
	    		"temp0 = 42\n" + 
	    		"res = temp0\n" + 
	    		"falseLabel0\n" + 
	    		"temp0 = 1\n" + 
	    		"temp1 = res + temp0\n" + 
	    		"res = temp1\n";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    System.out.println("test case 53 pass!");
	    
	    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
	                       "(this is only a subset of what we will test your code on)");
	    
	    // error case
	    eval = "public class test {void main() {@}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {1.2}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {<-=}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {\"This string isn't allowed!\"}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{3 +}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{(}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{()}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{8- 9- }}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{3*(((((((((((((((0)}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{17--3}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{(9*(4-()}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{12-9() + 3}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{)}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main() {{-8}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {3 + 4}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {public static int main}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){int xx;}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){int x2}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){if(if(){}{}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){int x2;x9}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){if()}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    eval = "public class test {void main(){int xx =12 + if(){}}";
	    parser = new AdvancedJava();
	    result = "";
	    assertTrue(parser.getThreeAddr(eval).equals(result));
	    
	    System.out.println("*******************************************");
	}

  public static void main(String[] args){
    TestThreeAddrGen();
  }

}
