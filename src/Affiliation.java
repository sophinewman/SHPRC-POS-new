
public class Affiliation {
	private int affiliationID;
	private String affiliationName;
	private int credit;
	private boolean qualifiesForPregnancyTest;
	
	public Affiliation (int affiliationID, String affiliationName, int credit, boolean qualifiesForPregnancyTest) {
		this.affiliationID = affiliationID;
		this.affiliationName = affiliationName;
		this.credit = credit;
		this.qualifiesForPregnancyTest = qualifiesForPregnancyTest;
	}
	
	public String getName() {
		return affiliationName;
	}
	
	public boolean qualifiesForPregnancyTest() {
		return qualifiesForPregnancyTest;
	}
	
	public int getCredit () {
		return credit;
	}
	
	public int getAffiliationID() {
		return affiliationID;
	}
	
	public String toString() {
		return affiliationName;
	}
}
