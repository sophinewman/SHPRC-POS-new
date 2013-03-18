/**
 * SHPRC-POS
 * Affiliation.java
 * Stores credit and subsidy information about a student or community affiliation.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class Affiliation {
	
	/* A unique numeric identifier for a given affiliation. This number is internal
	 * to SHPRC-POS and is not derived from any external or university-specified
	 * affiliation ID system. */
	private int affiliationID;
	
	/* The name of the affiliation, e.g., "Frosh" or "Professor". */
	private String affiliationName;
	
	/* The amount of credit available for this affiliation. Credit is always stored as
	 * an integer representing the US Dollar cents to be added to a purchase total
	 * and is therefore always negative, e.g., a student who has $2 to spend will
	 * have -200 creditAvailable. */
	private int credit;
	
	/* Whether the affiliation qualifies for a free pregnancy test. */
	private boolean qualifiesForPregnancyTest;
	
	/**
	 * Class constructor.
	 * @param affiliationID the unique numeric identifier for this affiliation
	 * @param affiliationName the name of the affiliation
	 * @param credit the credit available to the affiliation as a negative integer of cents
	 * @param qualifiesForPregnancyTest whether the affiliation qualifies for a free pregnancy test
	 */
	public Affiliation (int affiliationID, String affiliationName, int credit, boolean qualifiesForPregnancyTest) {
		this.affiliationID = affiliationID;
		this.affiliationName = affiliationName;
		this.credit = credit;
		this.qualifiesForPregnancyTest = qualifiesForPregnancyTest;
	}
	
	/**
	 * Returns the affiliation's name.
	 * @return the affiliation's name
	 */
	public String getName() {
		return affiliationName;
	}
	
	/**
	 * Returns whether the affiliation qualifies for a free pregnancy test.
	 * @return whether the affiliation qualifies for a free pregnancy test
	 */
	public boolean qualifiesForPregnancyTest() {
		return qualifiesForPregnancyTest;
	}
	
	/**
	 * Returns the amount of credit the affiliation receives as a negative integer of cents
	 * @return the amount of credit the affiliation receives
	 */
	public int getCredit () {
		return credit;
	}
	
	/**
	 * Returns the affiliation's affiliationID.
	 * @return the affiliation's affiliationID
	 */
	public int getAffiliationID() {
		return affiliationID;
	}
	

	/**
	 * Returns the affiliation's name. Used in JComboBox and JList display.
	 * @return the affiliation's name
	 */
	@Override
	public String toString() {
		return affiliationName;
	}
}
