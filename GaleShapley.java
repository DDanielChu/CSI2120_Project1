
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
	
	
	
	
	public void unMatchPeople(Integer residentID) {
		Resident resident = residents.get(residentID);
		resident.setMatchedRank(-1);
		resident.setMatchedProgram(null);
		
		unmatched.add(residentID);
		
		
	}
	
	
	public void galeShapleyAlgorithm() {
		
		for (Integer key : residents.keySet()) {
			unmatched.add(key);
		}
		
		
		boolean change = true;
		
		int index = unmatched.size()-1;
		
		while (!unmatched.isEmpty()) {
			
			if (index == unmatched.size()-1) {
				if (!change){
					break;
				}
				
				change = false;
			
			}
			
			Resident resident = residents.get(unmatched.get(index));
			
			
			String[] residentROL = resident.getRol();
			
			
			
			for (int counter = 0; counter < residentROL.length; counter++) {
				
				Integer seeIfAnotherPersonWasRemoved = programs.get(residentROL[counter]).addResident(resident);
				
				if (seeIfAnotherPersonWasRemoved != null) {
					unMatchPeople(seeIfAnotherPersonWasRemoved);
					unmatched.remove(Integer.valueOf(resident.getResidentID()));
					change = true;
					break;
				}
				
				if(resident.getMatchedProgram() != null) {
					unmatched.remove(Integer.valueOf(resident.getResidentID()));
					change = true;
					break;
					
				}
				
				
				
			}
			
			index --;
			
			
			
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
			
			System.out.println(gs.residents);
			System.out.println(gs.programs);
			
			System.out.println("\n\n\n\nlastname,firstname,residentID,programID,name");


			List<Resident> residentList = new ArrayList<>(gs.residents.values());

			residentList.sort(Comparator.comparing(Resident::getLastname).thenComparing(Resident::getFirstname));

			
			
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
			
			
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
	}
}
