import java.io.*;
import java.util.*;

/***
 * 
 * COMP 251 Assignment #5
 * @author Cassiel Moroney
 * 260662020
 *
 * With reference to https://www.baeldung.com/java-write-to-file for file IO
 *
 */

public class balloon {
	
	public static void main(String args[]) {
		
		//long start = System.currentTimeMillis();;
		
		int num_of_problems;
		int[] solutions;
		balloon Game = new balloon();
		
		 try {
	            Scanner f = new Scanner(new File("testBalloons.txt"));
	            num_of_problems = Integer.parseInt(f.nextLine());
	            solutions = new int[num_of_problems];
	            
	            f.nextLine(); //
	            for (int i = 0; i < num_of_problems; i++) {
	            		String balloons = f.nextLine();
	            		solutions[i] = Game.balloonBest(balloons);
	            }
	            
	            FileWriter fileWriter = new FileWriter("testBalloons_solution.txt");
	            PrintWriter printWriter = new PrintWriter(fileWriter);
	    	    		for (int i = 0; i < num_of_problems-1; i++) {
	    	    			printWriter.println(solutions[i]);
	    	    		}
	    	    		printWriter.print(solutions[num_of_problems-1]);
	    	    		
	    	    		printWriter.close();
		
		 } catch (FileNotFoundException e){
	            System.out.println("File not found!");
	            System.exit(1);
	     } catch (IOException e) {
			e.printStackTrace();
		}
		 
		//long end = System.currentTimeMillis();;
		// System.out.println((end - start) + " ms");
		 
        
	}
	
	private int balloonBest(String input) {
		
		String[] strArray = input.split("\\ ") ;
		
		
		int[] array = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			array[i] = Integer.parseInt(strArray[i]);
		}
			
//		String l = "";
//		for(int i = 0; i < array.length; i++) {
//			l+= array[i]+" ";
//		}
//		System.out.println("p: "+l);
		
		int balloons = array.length;
		int arrows = 0;
		
		while (balloons != 0) {
			int height = highest(array);

			for(int i = 0; i < array.length; i++) {
				if (array[i] == height && height != 0) {
					array[i] = 0; //popped
					height--;
					balloons--;
				}
			}
			arrows ++;
		} 
		return arrows;
	}
	
	private int highest(int[] array) {
		int highest = 0;
		
		for(int i = 0; i < array.length; i++) {
			if (array[i] > highest) highest = array[i];
		}
		
		return highest;
	}
	
}
