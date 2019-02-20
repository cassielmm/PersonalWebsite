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

public class mancala {
	
	ArrayList<Integer[]> moves;

	public static void main(String args[]) {
		
		long start = System.currentTimeMillis();;
		
		mancala Game = new mancala();
		
		try {
            Scanner f = new Scanner(new File("testMancala.txt"));
            int num_of_problems = Integer.parseInt(f.nextLine());
            int[][] setups = new int[num_of_problems][12];
            int[] solutions = new int[num_of_problems];
            
            for (int i = 0; i < num_of_problems; i++) {
	            	String line = f.nextLine();
	    			
	    			for (int k = 0; k < 24; k+=2) {	
	    				setups[i][k/2] = line.charAt(k) - 48;
	    				
	    			}
	    			
	    			String l = "";
	    			for(int m = 0; m <12; m++) {
	    				l+=setups[i][m]+" ";
	    			}
	    			
            		solutions[i] = Game.bestScore(setups[i]);
            		//System.out.println("solution "+i+": "+solutions[i]);
            }
            f.close();
            
            FileWriter fileWriter = new FileWriter("testMancala_solution.txt");
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
		
		long end = System.currentTimeMillis();;
		 System.out.println((end - start) + " ms");
		
	}
	
	private int bestScore(int[] starting_board) {
		int[] board = new int[starting_board.length];
		int best = 12;
		int moves;
		
		for (int i = 0; i < 36; i++) {
			
			System.arraycopy(starting_board, 0, board, 0, starting_board.length);
			moves = 0;
			
			if (pebblesLeft(board) < 2) return (pebblesLeft(board));
			
			int lim = 0;
			while (movesLeft(board)) {
				move(board);
				moves++;
				lim++;
			}
			int remaining = pebblesLeft(board);
			if (best > remaining) best = remaining;
			
		}

		return best;
	}
	
	private void print(int[] board) {
		
		String l = "";
		for(int i = 0; i < board.length; i++) {
			l+= board[i]+" ";
		}
		System.out.println("p: "+l);
		
	}
	
	private int pebblesLeft(int[] board) {
		
		int pebbles = 0;
		for(int i = 0; i < board.length; i++) {
			if (board[i] == 1) pebbles++;
		}
		return pebbles;
		
	}
	
	private void move(int[] board) {

		jump(board, moves.get((int) (Math.random() * moves.size())));
		
	}
	
	private ArrayList<Integer[]> getMoves(int[] board) {
		
		ArrayList<Integer[]> m = new ArrayList<Integer[]>();
		
		//forward
		for (int i = 0; i < 10; i++) {
			if (board[i] == 1 && board[i+1] == 1 && board[i+2] == 0) {
				m.add(new Integer[]{i, i+1, i+2});
			}
		}
		//backward
		for (int i = 11; i > 2; i--) {
			if (board[i] == 1 & board[i-1] == 1 && board[i-2] == 0) {
				m.add(new Integer[]{i, i-1, i-2});
			}
		}
				
		return m;
		
	}
	
	private static void jump(int[] board, Integer[] move) {
		
		if (move.length != 3 || board[move[0]] == board[move[2]] || board[move[1]] != 1) 
			System.err.println("Jump used improperly.");
				
		board[move[0]] = 0;
		board[move[1]] = 0;
		board[move[2]] = 1;
		
	}
	
	private boolean movesLeft(int[] board) {
		
		moves = getMoves(board);
		if (moves.size() == 0) return false;
		return true;
		
	}
	
}
