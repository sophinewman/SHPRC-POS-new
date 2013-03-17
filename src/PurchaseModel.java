import java.util.HashMap;


/**
 * SHPRC-POS
 * Purchase.java
 * Stores information about a purchase, calculating totals based on the specified products
 * and the current client. Purchase is the Model in the MVC framework.
 * 
 * @author Sophi Newman
 * @version 0.1 2/4/13
 */


public class PurchaseModel implements SHPRCConstants {



	/* total stores the subtotal, credit used, preg. test subsidy, and total of an order */
	private int[] totals = {0,0,0,0};

	/* currentClient stores information about the purchaser*/
	private Client currentClient;

	/* products stores the products and their quantities in a given purchase */
	private HashMap<Product, Integer> products; 

	/* allows for lookup of global/backend data */
	private RuntimeDatabase rDB;


	/**
	 * Class constructor.
	 * @param rDB the RuntimeDatabase the Purchase will access.
	 */
	public PurchaseModel (RuntimeDatabase rDB) {
		this.rDB = rDB;
		products = new HashMap<Product, Integer>();

	}

	public Client getCurrentClient() {
		return currentClient;
	}


	public HashMap<Product, Integer> getPurchaseProducts() {
		return products;
	}

	/**
	 * Sets the current client based on the specified SUID and affiliation. Takes a 
	 * Stanford University IDentification (7-digit number, e.g., 5555555) and
	 * an affiliation identification number (e.g., "frosh," "grad," "other") and uses
	 * these parameters to look up the client in the client database. If the client is
	 * found, a Client object is created. If not, a new Client object is made.
	 * @param currentSUID the SUID to set
	 * @param affiliationID the class or community affiliation the client belongs to
	 * TODO allow for those with certain combinations to opt out of SUID
	 */
	public void setCurrentClient(int suid, int affiliationID) {
		currentClient = rDB.lookupClient(suid);
		Affiliation affiliation = rDB.getAffiliation(affiliationID);
		if (currentClient == null) {
			int creditAvailable = affiliation.getCredit();
			boolean qualifiesForPregnancyTest = affiliation.qualifiesForPregnancyTest();
			currentClient = 
				new Client(suid, affiliationID, creditAvailable, false, qualifiesForPregnancyTest);
		}
	}

	public void setCurrentClient(Object obj) {
		currentClient = null;
	}

	/**
	 * Adds a product in the specified quantity to the purchase and updates the total.
	 *  @param product the product to be added to a purchase
	 *  @param qty the quantity of the product to be added to a purchase
	 */
	public void addProduct(Product product, int qty) {
		if (qty != 0) {
			if (products.get(product) != null) {
				int currentQty = products.get(product);
				products.put(product, currentQty + qty);
				totals[SUBTOTAL] += product.getPrice() * qty;
			} else {
				products.put(product, qty);
				totals[SUBTOTAL] += product.getPrice() * qty;
			}
		}
	}


	/**
	 * Removes an product from the purchase's products, calculating and subtracting its
	 * cost contribution from the subtotal.
	 * @param product the product to be removed from a purchase
	 */
	public void removeProduct(Product product) {
		totals[SUBTOTAL] -= getCurrentProductCost(product);
		products.remove(product);
	}


	/**
	 * Returns the total cost of a purchase after credit and subsidy.
	 * Calculates how much, if any, credit can be applied to this purchase,
	 * and sets the credit index in totals. Determines whether a pregnancy
	 * test is in this purchase and if the client qualifies for a subsidy;
	 * if both are true, the subsidy is set in the subsidy index in totals.
	 * The total index is totals is set to the sum of the subtotal, the credit,
	 * and the pregnancy test subsidy. This total is also returned to the caller.
	 * @return the total cost of a purchase after credit and subsidy
	 */
	public int tallyPurchaseTotal() {
		totals[CREDIT] = calculateCredit();
		totals[PT_SUBSIDY] = applyPregnancyTestSubsidy();
		int total = totals[SUBTOTAL] + totals[CREDIT] + totals[PT_SUBSIDY];
		totals[TOTAL] = total;
		return total;
	}
	
	public int[] getTotals() {
		tallyPurchaseTotal();
		return totals;
	}


	/**
	 * Tests whether the order contains a pregnancy test and whether the client
	 * qualifies to receive a free pregnancy test; if both are true, the amount
	 * to be subsidized is returned. Otherwise, the method returns 0.
	 * @return the amount of subsidy for a pregnancy test in this purchase
	 */
	private int applyPregnancyTestSubsidy() {
		Product pregnancyTest = rDB.getPregnancyTestProduct();
		if (products.containsKey(pregnancyTest) && currentClient != null) {
			if (currentClient.pregnancyTestAvailable()) {
				return -1 * pregnancyTest.getPrice();
			}
		}
		return 0;
	}


	/**
	 * Tests to see whether the subtotal is less than the amount of available credit.
	 * If so, the applied credit is set to the merchandise total. Otherwise, the credit 
	 * is set to the available credit. If no client has been set, the method returns 0.
	 * @return the credit to be applied to the purchase.
	 */
	public int calculateCredit() {
		if (currentClient == null) {
			return 0;
		}
		int availableCredit = currentClient.getCredit();
		if (totals[SUBTOTAL] < -1 * availableCredit) {
			return -1 * totals[SUBTOTAL];
		}
		return availableCredit;
	}


	/**
	 * Calculates the total value in cents that a product is contributing to
	 * an order.
	 * @param product the product to be examined
	 * @return the cost in cents that this product at its quantity contributes to the total.
	 */
	private int getCurrentProductCost(Product product) {
		if (products.get(product) != null) {
			return product.getPrice() * products.get(product); // price * qty
		}
		return 0;
	}

}
