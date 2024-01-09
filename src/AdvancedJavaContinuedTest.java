import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class AdvancedJavaContinuedTest {
	public static void consume(Process cmdProc) throws IOException{
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
		String line;
		while ((line = stdoutReader.readLine()) != null) {
			// process procs standard output here
		}
		
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
		while ((line = stderrReader.readLine()) != null) {
			// process procs standard error here
		}
	}
	@Test
	public static void TestThreeAddrGen() throws IOException, InterruptedException {
	    System.out.println("*******************************************");
	    System.out.println("Testing Three Address Generation");
	
	    String eval = "public class test {int reserved; int x; void main(int x) {x = 3; return x + x;} void one() {return 1;} void mainEntry() {while(reserved < 10) {reserved = reserved + main(one());}}}\n";
	    AdvancedJava parser = new AdvancedJava();
//	    String result = "temp0 = 12\n" + 
//	    		"x = temp0\n" + 
//	    		"temp0 = 8\n" + 
//	    		"temp1 = 9\n" + 
//	    		"temp2 = 3\n" + 
//	    		"temp3 = temp1 - temp2\n" + 
//	    		"temp4 = temp0 * temp3\n" + 
//	    		"y = temp4\n" + 
//	    		"IF_LT: y, x, trueLabel1\n" + 
//	    		"GOTO: falseLabel0\n" + 
//	    		"trueLabel1\n" + 
//	    		"IF_NE: y, x, trueLabel0\n" + 
//	    		"GOTO: falseLabel0\n" + 
//	    		"trueLabel0\n" + 
//	    		"falseLabel0\n";
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 12);
//	    
//	    eval = "public class test {int reserved; int x; void main() {x = 1; int x = 3; return x + x + x;} void one() {return 11;} void mainEntry() {while(reserved < 10) {reserved = reserved + main() + one();}}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 20);
//	    
//	    eval = "public class test {int reserved; int x; int main() {x = 1; int x = 3; return x + x + x;} void mainEntry() {while(reserved < 10) {reserved = reserved + main();}}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 18);
//	    
//	    eval = "public class test {int reserved; int x; int main(int x) {x = 3; return x + x;} int one() {return 1;} void mainEntry() {while(reserved < 10) {reserved = reserved + main(one());}}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 12);
//	    
//	    eval = "public class test {int reserved; int x; int two(int x) {return x + 2;} int one(int x) {return x + 1;} void mainEntry() {while(reserved < 10) {reserved = reserved + two(one(two(one(two(3)))));}}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 11);
//	    
//	    eval = "public class test {int reserved; int x; int two(int x, int y) {return x + y;} int one() {return 1;} void mainEntry() {while(reserved < 10) {reserved = reserved + two(one(), 4);}}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 10);
//	    
//	    eval = "public class test {int reserved; int one(int x) {return x + 1;} "
//	    		+ "void mainEntry() {reserved = one(2);}}\n";
//	    parser = new AdvancedJava();
//	    System.out.println(parser.getThreeAddr(eval));
//	    sampleRun(eval, 3);
    
	    eval = "public class test {int reserved; int five(int x, int y, int z, int u, int v) {return x + y + z + u + v;} "
	    		+ "int main(int x, int y, int z, int u, int v) {return five(x, y, z, u, v) + 5;}"
	    		+ "void mainEntry() {reserved = main(1, 2, 3, 4, 5);}}\n";
	    parser = new AdvancedJava();
	    System.out.println(parser.getThreeAddr(eval));
	    sampleRun(eval, 20);

	    eval = "public class test {int reserved; int two(int x, int y) {return x + y;} "
	    		+ "int one(int x) {return x + 1;} "
	    		+ "void mainEntry() {reserved = two(one(1), 2);}}\n";
	    parser = new AdvancedJava();
	    System.out.println(parser.getThreeAddr(eval));
	    sampleRun(eval, 4);
	    
	    eval = "public class test {int reserved; int three(int x, int y, int z) {return x + y + z;} "
	    		+ "int two(int x, int y) {return x + y;} "
	    		+ "int one(int x) {return x + 1;} "
	    		+ "void mainEntry() {reserved = three(two(one(1), 2), one(2), 3);}}\n";
	    parser = new AdvancedJava();
	    System.out.println(parser.getThreeAddr(eval));
	    sampleRun(eval, 10);
	    
	    eval = "public class test {int reserved; int five(int x, int y, int z, int u, int v) {return x + y + z + u + v;} "
	    		+ "int four(int x, int y, int z, int u) {return x + y + z + u;} "
	    		+ "int three(int x, int y, int z) {return x + y + z;} "
	    		+ "int two(int x, int y) {return x + y;} "
	    		+ "int one(int x) {return x + 1;} "
	    		+ "void mainEntry() {reserved = five(four(three(two(one(0), 1), one(1), 2), two(one(1), 2), one(2), 3), three(two(one(1), 2), one(2), 3), two(one(2), 3), one(3), 4);}}\n";
	    parser = new AdvancedJava();
	    System.out.println(parser.getThreeAddr(eval));
	    sampleRun(eval, 40);
	    
	    eval = "public class test {int reserved; int two(int x, int y) {return x + y;} "
	    		+ "int one(int x) {return x + 1;} "
	    		+ "void mainEntry() {int a = 3; reserved = two(a + one(a), a);}}\n";
	    parser = new AdvancedJava();
	    System.out.println(parser.getThreeAddr(eval));
	    sampleRun(eval, 10);
	}
	
	 private static void sampleRun(String input, int result) throws IOException, InterruptedException {
		 AdvancedJava parser = new AdvancedJava();
		 String fileName = "test.c";
		 parser.codeGen(input, fileName);
		 /* Run Shell command */
		 Process cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
		 cmdProc.waitFor();
		 consume(cmdProc);
		 cmdProc = Runtime.getRuntime().exec("test");
		 cmdProc.waitFor();
		 consume(cmdProc);
		 int retValue = cmdProc.exitValue();
		 System.out.println(retValue);
		 assertTrue(retValue == result);
	 }
	 
	 public static void main(String[] args){
		 try {
			 TestThreeAddrGen();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
}