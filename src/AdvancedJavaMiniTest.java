import static org.junit.Assert.assertTrue;

import java.io.*;
import java.util.*;

import org.junit.Test;

public class AdvancedJavaMiniTest{

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
  public static void TestThreeAddrGen() throws IOException, InterruptedException{
    System.out.println("*******************************************");
    System.out.println("Testing Three Address Generation");

    String eval;
    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; while(reserved < 3) {reserved = reserved + 1;}} }";
    sampleRun(eval, 3);
    
    eval = "public class test {int reserved; int y; void mainEntry(){ int z; reserved = 3; if( 2< 3 && (3 > 4 || ((4 > 5 || ((8 < 9 || 7 > 9) && 2 < 5)) && 1 < 2) && 6<7)){ reserved = 41; }}}";
    sampleRun(eval, 41);

    eval = "public class test {int reserved; int y; void mainEntry(){ int z; reserved = 40; if( 2< 3 && (3 > 4 || ((4 > 5 || ((8 > 9 || 7 > 9) && 2 < 5)) && 1 < 2) && 6<7)){ reserved = 41; }}}";
    sampleRun(eval, 40);
    // feedback
    eval = "public class test { int reserved; void mainEntry() { reserved = 0; } }";
    sampleRun(eval, 0);
    
    eval = "public class test { int reserved; void mainEntry() { reserved = 2 + 2; } }";
    sampleRun(eval, 4);
    
    eval = "public class test { int reserved; void mainEntry() { int x = 14; int y = x * 8; reserved = y + x; } }";
    sampleRun(eval, 126);
    
    eval = "public class test { int reserved; int glob; void mainEntry() { glob = 24; reserved = glob / 4; } }";
    sampleRun(eval, 6);
    
    eval = "public class test { int reserved; void blarg(){ reserved = 12; } void mainEntry() { reserved = 9; reserved = reserved - 4; } }";
    sampleRun(eval, 5);
    
    eval = "private class test { int reserved; void hey(){} int glob; void hey2(){} void mainEntry() { glob = 2; reserved = 42; reserved = reserved * (glob - 0); } }";
    sampleRun(eval, 84);
    
    eval = "private class test { int reserved; int glob; void mainEntry() { int glob = 31; reserved = glob; } }";
    sampleRun(eval, 31);
    
    eval = "private class test { int reserved; int glob; void mainEntry() { glob = 15; int glob = 2; reserved = glob; } }";
    sampleRun(eval, 2);
    
    eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc; loc = glob; reserved = loc; } }";
    sampleRun(eval, 15);
    
    eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc+1; loc = glob+1; reserved = loc; } }";
    sampleRun(eval, 17);
    
    eval = "private class test { int reserved; void mainEntry() { if(1 < 2){reserved = 47;} } }";
    sampleRun(eval, 47);
    
    eval = "private class test { int reserved; void mainEntry() { if(1 < 2){ reserved = 17; if(2>1){ reserved = 42; } reserved = reserved + 2; } } }";
    sampleRun(eval, 44);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22){reserved = 42;} } }";
    sampleRun(eval, 42);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 12; int loc = 14; if(reserved <= loc-2){reserved = 1;} } }";
    sampleRun(eval, 1);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22 && reserved > 9){reserved = 18;} } }";
    sampleRun(eval, 18);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22){reserved = reserved+1;} } }";
    sampleRun(eval, 22);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22 && reserved != 14){reserved = reserved+1;} } }";
    sampleRun(eval, 14);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 9; } }";
    sampleRun(eval, 20);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} int loc = 1; reserved = reserved + loc;} } }";
    sampleRun(eval, 11);
    
    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10 && reserved > 4){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 2;} }";
    sampleRun(eval, 13);
    
    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
  }
  
  private static void sampleRun(String input, int result) throws IOException, InterruptedException{
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
    } catch (Exception e){
      e.printStackTrace();
    }
  }

}
