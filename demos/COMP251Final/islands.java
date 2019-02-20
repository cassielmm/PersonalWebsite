import java.io.*;
import java.util.*;

/***
 * COMP 251 Assignment #5
 * @author Cassiel Moroney
 * 260662020
 * 
 * With reference to https://www.baeldung.com/java-write-to-file for file IO
 *
 */

public class islands {

	public static void main(String args[]) {
		
		long start = System.currentTimeMillis();;
		
		int num_of_problems;
		int[] solutions;
		int[][] map;
		islands Game = new islands();
		
		 try {
	            Scanner f = new Scanner(new File("testIslands.txt"));
	            num_of_problems = Integer.parseInt(f.nextLine());
	            solutions = new int[num_of_problems];
	            
	            for (int i = 0; i < num_of_problems; i++) {
	            		int m = f.nextInt();
	            		int n = f.nextInt();
	            		map = new int[m][n];
	            		
	            		for (int j = 0; j < m; j++) {
	            			String line = f.next();
	            			
	            			for (int k = 0; k < n; k++) {
	            				
	            				map[j][k] = ( line.charAt(k) == 35 ? -1 : 0);
		            			//water, # gets turned into -1
		            			//land, - gets turned into 0
	            			}
	            		}
	            		solutions[i] = Game.findIslands(map, m, n);
	            		//System.out.println("solution "+i+": "+solutions[i]);
	            }
	            
	            FileWriter fileWriter;
				try {
					fileWriter = new FileWriter("testIslands_solution.txt");
					PrintWriter printWriter = new PrintWriter(fileWriter);
		    	    		for (int i = 0; i < num_of_problems; i++) {
		    	    			printWriter.print(solutions[i]+"\n");
		    	    		}
		    	    		printWriter.close();
		    	    	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		 } catch (FileNotFoundException e){
	            System.out.println("File not found!");
	            System.exit(1);
	     }
		 
		 long end = System.currentTimeMillis();;
		 System.out.println((end - start) + " ms");
        
	}

	private int findIslands(int[][] map, int m, int n) {
		ArrayList<ArrayList<int[]>> sets = new ArrayList<ArrayList<int[]>>();
		//String line = "";
		int islands = 0;
		
		for (int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if (map[i][j] == 0) {			

					int[] neighbors = new int[]{-1, -1, -1, -1}; //{up, left, down, right}
					
					/*** Survey **/
					
					//up
					if (i-1 >= 0) neighbors[0] = (map[i-1][j] != 0) ? map[i-1][j] : -1;
					
					//left
					if (j-1 >= 0) neighbors[1] = (map[i][j-1] != 0) ? map[i][j-1] : -1;
					
					//down
					if (i+1 < m) neighbors[2] = (map[i+1][j] != 0) ? map[i+1][j] : -1;
					
					//right
					if (j+1 < n) neighbors[3] = (map[i][j+1] != 0) ? map[i][j+1] : -1;
					
					/*** Interpret **/
					int sharedGroup = sharedGroup(neighbors);
					
					//all water
					if (neighbors[0] == -1 && neighbors[1] == -1 && neighbors[2] == -1 
							&& neighbors[3] == -1) {
						sets.add(new ArrayList<int[]>());
						sets.get(sets.size()-1).add(new int[]{i, j});
						map[i][j] = sets.size(); //ID
						islands++;
					}
						
					//same group
					else if (sharedGroup != -1) {
						sets.get(sharedGroup-1).add(new int[] {i, j});
						map[i][j] = sharedGroup;
					}
						
					//different groups
					else {
						
						//always merge up
						map[i][j] = neighbors[0];
						sets.get(neighbors[0]-1).add(new int[] {i, j});
						
						merge(sets, map, neighbors[0], neighbors[1]);
						islands--;
					}
					
					//line += map[i][j];
				} else {
					//line += ".";
				}
				
			}
			//line = "";
		}
		return islands;
	}
	
	private int sharedGroup(int[] neighbors) {
		
		int shared = -1;
		for (int i = 0; i < 4; i++) {
			if (neighbors[i] != -1) {
				if (shared == -1) shared = neighbors[i];
				else if (shared != neighbors[i]) return -1;
			}
		}
		return shared;
		
	}
	
	//of all the listed ArrayLists, how many are active sets?
	private int countIslands(ArrayList<ArrayList<int[]>> sets) {
		
		int islands = 0;
		for(ArrayList<int[]> i : sets) {
			if (i.get(0).length > 1) islands++;
		}
		
		return islands;
	}
	
	//merge disjoint sets
	private void merge(ArrayList<ArrayList<int[]>> sets, int[][] map, int to, int from) {
		
		to = to-1;
		from = from-1;
		
		ArrayList<int[]> targets = sets.get(from);
		
		if (targets.get(0).length <= 1) {
			System.err.println(" You have been misdirected..."+(from+1)+" DNE anymore");
			
		}
		
		for (int i = 0; i < targets.size(); i++) {
			int[] coords = targets.get(i);
			map[coords[0]][coords[1]] = to+1;
			sets.get(to).add(coords);
		}

		sets.get(from).clear();
		sets.get(from).add(new int[] {to+1});

	}
	
	private void printMap(int[][] map) {
		for (int l = 0; l < map.length ; l++) {
			String str = "";
			for (int r = 0; r < map[0].length; r++) {
				if (map[l][r] == -1) str+= ". ";
				else str+=map[l][r]+" ";
			}
			System.out.println(str);
			
		}
	}
	
}
