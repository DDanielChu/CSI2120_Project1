
// Project CSI2120/CSI2520
// Winter 2026
// Robert Laganiere, uottawa.ca
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


// this is the (incomplete) class that will generate the resident and program maps
public class GaleShapley {
	
	public HashMap<Integer,Resident> residents;
	public HashMap<String,Program> programs;
	
	private ArrayList<Integer> unmatched = new ArrayList<>();
	
	

	public GaleShapley(String residentsFilename, String programsFilename) throws IOException, 
													NumberFormatException {
		readResidents(residentsFilename);
		readPrograms(programsFilename);

		
		galeShapleyAlgorithm();
		
		
	}
	
	
	
	//Makes sure that it puts the resident into the unmatched array
	//Also ensures that the resident does not have a matched rank or matched program
	public void unMatchPeople(Integer residentID) {
		Resident resident = residents.get(residentID);
		resident.setMatchedRank(-1);
		resident.setMatchedProgram(null);
		
		unmatched.add(residentID);
		
		
	}
	
	//Goes through the entirety of the resident HashMap and tries to match them with their preferred programs
	//If they don't get any of their preferred programs, then they will be unmatched
	public void galeShapleyAlgorithm() {
		
		
		//Initially, all of the residents are unmatched
		//This will put their student number into the unmatched array
		for (Integer key : residents.keySet()) {
			unmatched.add(key);
		}
		
		//This boolean holds a boolean value to see if anyone was matched or unmatched
		boolean change = true;
		
		//Will start from the last index so that if I remove a person from the unmatched array
		//it won't affect the process of going through each resident 
		int index = unmatched.size()-1;
		
		//Keeps going until everyone is matched, or no one can be matched to their preferred program
		while (!unmatched.isEmpty()) {
			
			//Every time the while loop goes through the entire array
			//it checks if anyone has been unmatched or matched. 
			//If no one has been matched or unmatched, then that means that no one can be matched to their preferred program
			//Therefore, it will break the while loop
			//Otherwise, if there has been a change, then it will continue the loop and set change to false
			if (index == unmatched.size()-1) {
				if (!change){
					break;
				}	
				change = false;
			}
			
			//Gets a random resident from the hashmap 
			Resident resident = residents.get(unmatched.get(index));
			
			
			//Gets the residents Ranked Order List
			String[] residentROL = resident.getRol();
			
			//Goes through the Ranked Order List and tries to pair them up with their preferred program
			for (int counter = 0; counter < residentROL.length; counter++) {
				
				//the addResident() method in the Program class checks three cases
			
				//1. If the person is in the programs ROL
					//1.1 if not then return null
					
				//2. If the quota size is reached
					//2.1 If not then change the person's program and then return null
						//2.1.1 This will trigger the second if statement

				//3 If there is another person that is ranked lower than them on the program's ROL that is currently in the program list
					//3.1 Remove the lower ranked person, and put the new resident into the list and return the ID of the removed person
						//3.1.1 This will trigger the first If statement
				Integer seeIfAnotherPersonWasRemoved = programs.get(residentROL[counter]).addResident(resident);
				
				//Removes the lower ranked person on the program's list and makes them unmatched
				//Removes the new resident from the unmatched array
				if (seeIfAnotherPersonWasRemoved != null) {
					unMatchPeople(seeIfAnotherPersonWasRemoved);
					unmatched.remove(Integer.valueOf(resident.getResidentID()));
					change = true;
					break;
				}
				
				//Removes the new residednt from the unmatched array
				if(resident.getMatchedProgram() != null) {
					unmatched.remove(Integer.valueOf(resident.getResidentID()));
					change = true;
					break;
					
				}
				
			}
			
			//Goes down up the array
			index --;
			
			//Once the array goes through the entire array, it loops back to the end
			if (index == -1){
				index = unmatched.size()-1;
			}
				
		}
				
			
	}
		
	
	
	
	// Reads the residents csv file
	// It populates the residents HashMap
    public void readResidents(String residentsFilename) throws IOException, 
													NumberFormatException {

        String line;
		residents= new HashMap<Integer,Resident>();
		BufferedReader br = new BufferedReader(new FileReader(residentsFilename)); 

		int residentID;
		String firstname;
		String lastname;
		String plist;
		String[] rol;

		// Read each line from the CSV file
		line = br.readLine(); // skipping first line
		while ((line = br.readLine()) != null && line.length() > 0) {

			int split;
			int i;

			// extracts the resident ID
			for (split=0; split < line.length(); split++) {
				if (line.charAt(split) == ',') {
					break;
				} 
			}
			if (split > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);

			residentID= Integer.parseInt(line.substring(0,split));
			split++;

			// extracts the resident firstname
			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					break;
				} 
			}
			if (i > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);

			firstname= line.substring(split,i);
			split= i+1;
			
			// extracts the resident lastname
			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					break;
				} 
			}
			if (i > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);

			lastname= line.substring(split,i);
			split= i+1;		
				
			Resident resident= new Resident(residentID,firstname,lastname);

			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == '"') {
					break;
				} 
			}
			
			// extracts the program list
			plist= line.substring(i+2,line.length()-2);
			String delimiter = ","; // Assuming values are separated by commas
			rol = plist.split(delimiter);
			
			resident.setROL(rol);
			
			residents.put(residentID,resident);
		}	
    }

	
	// Reads the programs csv file
	// It populates the programs HashMap
    public void readPrograms(String programsFilename) throws IOException, 
													NumberFormatException {

        String line;
		programs= new HashMap<String,Program>();
		BufferedReader br = new BufferedReader(new FileReader(programsFilename)); 

		String programID;
		String name;
		int quota;
		String rlist;
		int[] rol;

		// Read each line from the CSV file
		line = br.readLine(); // skipping first line
		while ((line = br.readLine()) != null && line.length() > 0) {

			int split;
			int i;

			// extracts the program ID
			for (split=0; split < line.length(); split++) {
				if (line.charAt(split) == ',') {
					break;
				} 
			}			
			if (split > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);


			programID= line.substring(0,split);
			split++;

			// extracts the program name
			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					break;
				} 
			}
			if (i > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);
			
			name= line.substring(split,i);
			split= i+1;
			
			// extracts the program quota
			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					break;
				} 
			}
			if (i > line.length()-2)
				throw new IOException("Error: Invalid line format: " + line);

			quota= Integer.parseInt(line.substring(split,i));
			split= i+1;		
				
			Program program= new Program(programID,name,quota);

			for (i= split ; i < line.length(); i++) {
				if (line.charAt(i) == '"') {
					break;
				} 
			}
			
			// extracts the resident list
			rlist= line.substring(i+2,line.length()-2);
			String delimiter = ","; // Assuming values are separated by commas
			String[] rol_string = rlist.split(delimiter);
			rol= new int[rol_string.length];
			for (int j=0; j<rol_string.length; j++) {
				
				rol[j]= Integer.parseInt(rol_string[j]);
			}
			
			program.setROL(rol);
			
			programs.put(programID,program);
		}	
    }
	
	
	
	
	

	public static void main(String[] args) {
		
		
		try {
			
			GaleShapley gs= new GaleShapley(args[0],args[1]);
			
			
			//Places the output into a file called TestOutput
			PrintStream fileStream = new PrintStream(new File("TestOutput.txt"));
			System.setOut(fileStream);
			
			
			
			System.out.println("lastname,firstname,residentID,programID,name");

			//Sorts out the results alphabetically based on the residents' first names
			List<Resident> residentList = new ArrayList<>(gs.residents.values());
			residentList.sort(Comparator.comparing(Resident::getLastname).thenComparing(Resident::getFirstname));
				
			
			//Goes through the residents and outputs the results of their program acceptances
			for (Resident resident: residentList) {
				System.out.print(resident.getLastname() + ", ");
				System.out.print(resident.getFirstname()+ ", ");
				System.out.print(resident.getResidentID()+ ", ");
				
				if (resident.getMatchedProgram() == null){
					System.out.print("XXX"+ ", ");
				}
				
				else{
					System.out.print(resident.getMatchedProgram().getProgramID()+ ", ");
				}
				
				
				if (resident.getMatchedProgram() == null){
					System.out.print("NOT MATCHED"+ ", ");
				}
				
				else{
					System.out.print(resident.getMatchedProgram().getName());
				}
				
				System.out.println();
			}
			
			System.out.println("\nNumber of unmatched residents: " + gs.unmatched.size());
			
			
			
			//Goes through all of the programs and sees if there are any leftoever quotas
			int totalNumberOfQuotasLeft = 0;
			for (String key: gs.programs.keySet()) {
				Program program = gs.programs.get(key);
				totalNumberOfQuotasLeft += program.getQuota() - program.getMatchedResidents().size();
			}
			System.out.println("Number of positions available: " + totalNumberOfQuotasLeft);
			
			
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
	}
}
