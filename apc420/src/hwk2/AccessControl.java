package hwk2;

import java.io.FileReader; 
import java.util.Arrays;
import java.util.Scanner;

/* Name: Maggie Herms Contact: mkherms@uwm.edu
 * Load an access control matrix from a file
 * First line: subjects 
 * Second line: objects
 * Subsequent lines: access control matrix (comma separated)
 * e.g 
 * 
 *    alice, bob, carol, eve
 *    top-secret, secret, public
 *    rw,                
 *    r,          rw,    rw
 *    ,           r,     rw
 *    ,           ,      r   
 */
public class AccessControl {
	
	String [] subjects;
	String [] objects;
	String [][] matrix;
	
	// load access control matrix from a file by the name of "fileName"
	void load(String fileName) {
		try {
			Scanner in = new Scanner(new FileReader(fileName));
			
			subjects = in.nextLine().split(",\\s*");
			objects = in.nextLine().split(",\\s*");
			matrix = new String[subjects.length][objects.length];
			
			int i = 0;
			while (i < subjects.length && in.hasNextLine()) {
				matrix[i] = in.nextLine().split(",\\s*");
				i++;
			}
			in.close();
			
		} catch (Exception e) {
			System.out.println("Cannot open file: '" + fileName + "'");
		}
	}
	
	public String toString() {
		
		String objectString = "\t\t";
		int numObjects = 0;
		
		for (int i = 0; i < objects.length && objects[i] != null; i++) {
			
			objectString += (objects[i] + "\t");
			numObjects++;
			 	
		}
		System.out.println(numObjects);
		
	
		
		for (int i = 0; i < subjects.length; i++) {
			objectString += ("\n" + subjects[i] + "\t\t");
			
				
			for (int j = 0; j < matrix[i].length; j++) {
				
				objectString += (matrix[i][j] + "\t\t");
				
					
			}
					
		}
		
		return objectString;
		
		 /*return String.format("\t\t%s\t%s\t\t%s\n%s\t\t%s\n%s\t\t%s\t\t%s\t\t%s\n%s\t\t%s\t\t%s\t\t%s\n%s\t\t%s\t\t%s\t\t%s\n", 
				 objects[0], objects[1], objects[2], 
				 subjects[0], matrix[0][0], 
				 subjects[1], matrix[1][0], matrix[1][1], matrix[1][2],
				 subjects[2], matrix[2][0], matrix[2][1], matrix[2][2],
				 subjects[3], matrix[3][0], matrix[3][1], matrix[3][2]);*/
		
	}
	
	boolean check(String subj, String op, String obj) {
		int subIndex = -1;
		int objIndex = -1;
		
		for (int i =0; i < subjects.length; i++) {
			if(subjects[i].equals(subj)) {
				subIndex = i;
			}
		}
		
		if (subIndex == -1) {
			throw new RuntimeException(subj + " is not found\nAllowed subjects are [alice, bob, carol, eve]"); 
		}
				
		for (int j = 0; j < objects.length; j++) {
			if (objects[j].equals(obj)) {
				objIndex = j;	
			}
		}
		
		if (objIndex == -1) {
			throw new RuntimeException(obj + " is not found\nAllowed objects are [top-secret, secret, public]");
		}
		
		if (matrix[subIndex].length <= objIndex) {
			return false;
		} else if (matrix[subIndex][objIndex].toLowerCase().contains(op.toLowerCase())) {
			return true;
		}
		return false;
	}

	// Driver method
	public static void main(String[] args) {
		AccessControl ac = new AccessControl();
		ac.load("ac_matrix");
		System.out.println(ac);

		Scanner in = new Scanner(System.in);

		System.out.println("Enter your command (e.g. alice r secret):");
		String cmd = in.nextLine().trim(); 

		while(!cmd.equals("")) {
			String[] triple = cmd.split(" ");
			if(triple.length != 3) {
				System.out.println("Illegal command. Try again");
			}
			else {
				String subj = triple[0], op = triple[1], obj = triple[2];
				try {
					if(ac.check(subj, op, obj)) {
						System.out.printf("%s is allowed to perform %s operation on %s\n", subj, op, obj);
					}
					else {
						System.out.printf("%s is NOT allowed to perform %s operation on %s\n", subj, op, obj);
					}
				} catch(Exception e) { System.out.println(e); }
			}
			System.out.println("\nEnter your command");
			cmd = in.nextLine().trim();
		}

		in.close();
	}
}
