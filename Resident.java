
public class Resident {
	
	private int id;
	private String firstname;
	private String lastname;
	private Program[] rol;
	private Program matchedProgram;
	private int matchedRank;
	
	
	
	
	public Resident(int id, String firstname, String lastname, Program[] rol, Program matchedProgram, int matchedRank) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rol = rol;
		this.matchedProgram = matchedProgram;
		this.matchedRank = matchedRank;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Program[] getRol() {
		return rol;
	}
	public void setRol(Program[] rol) {
		this.rol = rol;
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
	
	
	
}
