// Project CSI2120 / CSI2520
// Winter 2026
// Robert Laganiere, uottawa.ca
//
// Completed Program class for Stable Matching

import java.util.ArrayList;

public class Program {

    // Attributes
    private String programID;
    private String name;
    private int quota;
    private int[] rol;
    private ArrayList<Integer> matchedResidents; // list of residentIDs

    // constructs a Program
    public Program(String id, String n, int q) {
        programID = id;
        name = n;
        quota = q;
        matchedResidents = new ArrayList<>();
    }


    // Getters
    public String getProgramID() {
        return programID;
    }

    public String getName() {
        return name;
    }

    public int getQuota() {
        return quota;
    }

    public int[] getROL() {
        return rol;
    }

    public ArrayList<Integer> getMatchedResidents() {
        return matchedResidents;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    // the rol in order of preference
    public void setROL(int[] rol) {
        this.rol = rol;
    }


	// Other methods

    // return true if residentID is in program's ROL
    public boolean member(int residentID) {
		
        for (int i = 0; i < rol.length; i++) {
            if (rol[i] == residentID) {
                return true;
            }
        }
        return false;
    }

    // return rank of a resident in the ROL, else -1
    public int rank(int residentID) {
		
        for (int i = 0; i < rol.length; i++) {
            if (rol[i] == residentID) {
                return i;
            }
        }
        return -1;
    }

    // return least preferred currently matched resident ID
    public int leastPreferred() {

        int worstID = -1;
        int worstRank = -1;

        for (int residentID : matchedResidents) {
            int rRank = rank(residentID);
            if (rRank > worstRank) {
                worstRank = rRank;
                worstID = residentID;
            }
        }

        return worstID;
    }

    // adds a resident to this program
    public void addResident(Resident r) {

        int residentID = r.getResidentID();
        int rRank = rank(residentID);
        if (rRank == -1) {
            return;
        }


        // program not full
        if (matchedResidents.size() < quota) {
			
            matchedResidents.add(residentID);
            r.setMatchedProgram(this);
            return;
        }


        // program full
        int worstID = leastPreferred();
        int worstRank = rank(worstID);
        if (rRank < worstRank) {
			
            matchedResidents.remove(Integer.valueOf(worstID));
            r.setMatchedProgram(this);
            matchedResidents.add(residentID);
        }
    }


    // string representation
    public String toString() {
        return "[" + programID + "]: " + name + " {" + quota + "} (" + rol.length + ")";
    }
}
