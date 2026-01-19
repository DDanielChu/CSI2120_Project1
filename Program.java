// Project CSI2120 / CSI2520
// Winter 2026
// Robert Laganiere, uottawa.ca
//
// Completed Program class for Stable Matching

import java.util.ArrayList;

public class Program {

    private String programID;      // 3-letter program ID
    private String name;           // program name
    private int quota;             // number of available positions
    private int[] rol;             // rank order list of resident IDs
    private ArrayList<Resident> matchedResidents;

    // constructs a Program
    public Program(String id, String n, int q) {
        programID = id;
        name = n;
        quota = q;
        matchedResidents = new ArrayList<>();
    }

    // sets the rank order list
    public void setROL(int[] rol) {
        this.rol = rol;
    }

    // returns true if residentID is in this program's ROL
    public boolean member(int residentID) {
        for (int i = 0; i < rol.length; i++) {
            if (rol[i] == residentID) {
                return true;
            }
        }
        return false;
    }

    // returns the rank of a resident in the ROL, or -1 if not found
    public int rank(int residentID) {
        for (int i = 0; i < rol.length; i++) {
            if (rol[i] == residentID) {
                return i;
            }
        }
        return -1;
    }

    // returns the least preferred currently matched resident
    public Resident leastPreferred() {
        Resident worst = null;
        int worstRank = -1;

        for (Resident r : matchedResidents) {
            int rRank = rank(r.getID());
            if (rRank > worstRank) {
                worstRank = rRank;
                worst = r;
            }
        }
        return worst;
    }

    // tries to add a resident to this program
    public void addResident(Resident r) {

        int rRank = rank(r.getID());

        // resident not acceptable
        if (rRank == -1) {
            return;
        }

        // program not full
        if (matchedResidents.size() < quota) {
            matchedResidents.add(r);
            r.setMatchedProgram(this);
            return;
        }

        // program full → compare with least preferred
        Resident worst = leastPreferred();
        int worstRank = rank(worst.getID());

        if (rRank < worstRank) {
            matchedResidents.remove(worst);
            worst.unmatch();

            matchedResidents.add(r);
            r.setMatchedProgram(this);
        }
    }

    // returns number of remaining positions
    public int availablePositions() {
        return quota - matchedResidents.size();
    }

    // string representation
    public String toString() {
        return "[" + programID + "]: " + name + " {" + quota + "} (" + rol.length + ")";
    }
}
