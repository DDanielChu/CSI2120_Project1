//Project CSI2120/CSI2520
//Winter 2026
//Robert Laganiere, uottawa.ca

//this is the (incomplete) Resident class
public class Resident {
	
	private int residentID;
	private String firstname;
	private String lastname;
	private String[] rol;
	
	private Program matchedProgram;
	private int matchedRank;
	
	// constructs a Resident
	public Resident(int id, String fname, String lname) {
	
		residentID= id;
		firstname= fname;
		lastname= lname;
		
		matchedProgram = null;
	}


	public int getResidentID() {
		return residentID;
	}


	 public void setResidentID(int residentID) {
		this.residentID = residentID;
	 }

	
	
	 public String getFirstname() {
		return firstname;
	 }
	
	 public void setFirstname(String firstname) {
		this.firstname = firstname;
	 }
	
	
	 public String getLastname() {
		return lastname;
	 }
	
	
	
	 public void setLastname(String lastname) {
		this.lastname = lastname;
	 }
	
	
	
	
	
	
	 public Program getMatchedProgram() {
		return matchedProgram;
	 }
	
	 public void setMatchedProgram(Program matchedProgram) {
		this.matchedProgram = matchedProgram;
	 }
	
	
	 public int getMatchedRank() {
		return matchedRank;
	 }
	
	
	 public void setMatchedRank(int matchedRank) {
		this.matchedRank = matchedRank;
	 }

	// the rol in order of preference
	public void setROL(String[] rol) {
		
		this.rol= rol;
	}
	
	
	public String[] getRol() {
		return rol;
	}
	
	
	// string representation
	public String toString() {
   
    return "["+residentID+"]: "+firstname+" "+ lastname+" ("+rol.length+")";	  
	}
}