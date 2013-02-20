
/**
 * SHPRC-POS
 * Client.java
 * Stores information about a client.
 * 
 * @author Sophi Newman
 * @version 0.1 2/8/13
 */

public class Client {

	/*	The SUID of the client */
	private int suid;  

	/* The class or community affiliation of the client. This number is internal
	 * to SHPRC-POS and is not derived from any external or university-specified
	 * affiliation ID system. */
	private int affiliationID; 

	/* The amount of credit available to the client. Credit is always stored as
	 * an integer representing the US Dollar cents to be added to a purchase total
	 * and is therefore always negative, e.g., a student who has $2 to spend will
	 * have -200 creditAvailable. */
	private int creditAvailable;

	/* The following two booleans store information that determines whether or not
	 * a client is eligible to receive a free pregnancy test. Whether the test has 
	 * been used and whether the client qualifies must be stored separately to allow
	 * for administrative changes mid-quarter. */
	 
	/* Whether this client has already received one free pregnancy test this quarter. */
	private boolean pregnancyTestRedeemed;

	/* Whether the client qualifies for a free pregnancy test. */
	private boolean qualifiesForPregnancyTest;


	/**
	 * Class constructor.
	 * @param suid the SUID number of the client
	 * @param affiliationID the class or community affiliation of the client
	 * @param creditAvailable how much credit the client has available
	 * @param pregnancyTestRedeemed whether this client has already redeemed
	 * the free pregnancy test subsidy this quarter
	 * @param qualifiesForPregnancyTest whether, given the affiliation, the client
	 * qualifies for a free pregnancy test
	 */
	public Client(int suid, int affiliationID, int creditAvailable, boolean pregnancyTestRedeemed,
			boolean qualifiesForPregnancyTest) {
		this.suid = suid;
		this.affiliationID = affiliationID;
		this.creditAvailable = creditAvailable;
		this.pregnancyTestRedeemed = pregnancyTestRedeemed;
		this.qualifiesForPregnancyTest = qualifiesForPregnancyTest;
	}


	/**
	 * Intended only for debugging.
	 * @return the string representation of a Client.
	 */
	@Override 
	public String toString() {
		return "SUID: " + suid + " Affiliation: " + affiliationID + " Credit Available: " + 
		creditAvailable + " PT Redeemed: " + pregnancyTestRedeemed + " qualifies for PT: " + qualifiesForPregnancyTest;
	}


	/**
	 * Returns the client's SUID number.
	 * @return the client's SUID number
	 */
	public int getSUID() {
		return suid;
	}


	/**
	 * Returns the amount of credit the client has available.
	 * @return the amount of credit the client has available
	 */
	public int getCredit() {
		return creditAvailable;
	}


	/**
	 * Sets the amount of credit the client has available to the specified amount.
	 * Credit is always stored as a negative number.
	 * @param credit the integer amount in cents of credit available to the client
	 */
	public void setCredit(int credit) {
		creditAvailable = credit;
	}


	/**
	 * Returns the class or community affiliation ID of the client. This ID is
	 * part of the SHPRC-POS system and is not based on external information.
	 * @return the affiliation ID of the client
	 */
	public int getAffiliation() {
		return affiliationID;
	}


	/**
	 * Returns whether or not the client can receive a free pregnancy test based on
	 * if a) he/she/ze qualifies based on affiliation and b) if a test has already
	 * been redeemed this quarter.
	 * @return whether a client qualifies for a free pregnancy test
	 */
	public boolean pregnancyTestAvailable() {
		return qualifiesForPregnancyTest && !pregnancyTestRedeemed;
	}


}
