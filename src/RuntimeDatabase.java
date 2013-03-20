import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SHPRC-POS
 * RuntimeDatabase.java
 * Stores back end information to be retrieved by the Purchase (Model in MVC
 * class) at runtime and enables writes to the back end data base. Because 
 * the amount of data in the back end is small and largely read-only, runtime 
 * structures allow greater efficiency than directly querying the back end.
 * 
 * @author Sophi Newman
 * @version 0.1 03/17/2013
 */
public class RuntimeDatabase implements SHPRCConstants{

	/* The JDBC Connection that backs the class */
	private Connection connection;

	/* All possible community affiliations */
	private ArrayList<Affiliation> affiliations;

	/* Maps from affiliation ID to affiliation */
	private HashMap<Integer, Affiliation> affiliationMap;

	/* All merchandise categories */
	private ArrayList<Category> categoryList;

	/* The product specified as the pregnancy test */
	private Product pregnancyTest;

	/* All products available for purchase */
	private ArrayList<Product> productList;



	/**
	 * Class constructor. Does not actually read in data and store it;
	 * instead, it ensures that the JDBC Class is successfully loaded.
	 * @throws ClassNotFoundException
	 */
	public RuntimeDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");

	}


	/**
	 * Initializes the JDBC connection to the database and loads up the
	 * runtime data structures. Returns false if any of these initializations
	 * fail.
	 * @return successfully initialized
	 */
	public boolean initRuntimeDatabase() {
		try {		
			connection = 
				DriverManager.getConnection("jdbc:sqlite:/Users/sophi/Documents/Workspace/SHPRC-POS/SHPRC-POS.db");
			if (connection == null) {
				return false;
			}
			if (!initializeProductMap()) {
				return false;
			}
			if (!initializeAffiliationMaps()) {
				return false;
			} if (!initializeCategoryList()) {
				return false;
			}
		}
		catch (SQLException e) {
			return false;
		}
		return true;
	}


	/**
	 * Closes the Connection object that underpins the RuntimeDatabase class.
	 * @throws SQLException
	 */
	public void closeDatabase() throws SQLException {
		if (connection != null)
			connection.close();
	}


	/**
	 * Returns all affiliations.
	 * @return all affiliations
	 */
	public ArrayList<Affiliation> getAffiliations() {
		return affiliations;
	}


	/**
	 * Returns all categories.
	 * @return all categories
	 */
	public ArrayList<Category> getCategoryList() {
		return categoryList;
	}


	/**
	 * Returns the product that the administrator has specified to be the pregnancy test.
	 * @return the pregnancy test Product object
	 */
	public Product getPregnancyTestProduct() {
		return pregnancyTest;
	}


	/**
	 * Returns all products.
	 * @return all products
	 */
	public ArrayList<Product> getProductList() {
		return productList;
	}


	/**
	 * Returns the credit a given affiliation has as a negative integer of cents.
	 * @param affiliationID the integer community/class affiliation ID to be looked up.
	 * @return the amount of credit a given affiliation receives
	 */
	public Affiliation getAffiliationCredit(int affiliationID) {
		if (affiliationMap.containsKey(affiliationID)) {
			return affiliationMap.get(affiliationID);
		}
		return null;
	}


	/**
	 * Returns the category associated with the given categoryID.
	 * @param categoryID
	 * @return the category associated with the given categoryID
	 */
	public Category getCategory(int categoryID) {
		Category category = null;
		for (Category c : categoryList ) {
			if (c.getCategoryID() == categoryID) {
				category = c;
			}
		} 
		return category;
	}


	//	/**
	//	 * Returns the product associated with the specified productID.
	//	 * @param productID the unique integer product ID to be looked up
	//	 * @return the product associated with the specified productID
	//	 */
	//	public Product getProduct (int productID) {
	//		return productMap.get(productID);
	//	}


	/**
	 * Queries the database for the Product table and stores the ResultSet
	 * in the productMap HashMap from ProductID to Product. Also stores the
	 * Pregnancy Test object in the pregnancyTest instance variable.
	 * @return successfully initialized
	 */
	private boolean initializeProductMap() {
		productList = new ArrayList<Product>();
		try {
			Statement stmt = connection.createStatement();
			// Return all rows in the Product relation
			ResultSet rs = stmt.executeQuery(("SELECT productID, productName, price, cost, isPregnancyTest, " +
					"Product.categoryID, categoryName FROM Product, Category " +
			"WHERE Product.categoryID = Category.categoryID ORDER BY productID"));
			// Read in the information about each row and store in Product object
			// Store the flagged pregnancy test product in an instance variable
			while (rs.next()) {
				// Fetching row information
				int productID = rs.getInt("productID");
				String productName = rs.getString("productName");
				int price = rs.getInt("price");
				int cost = rs.getInt("cost");
				int categoryID = rs.getInt("categoryID");
				String categoryName = rs.getString("categoryName");
				// Storing in Product object
				Product product = new Product(productID, price, cost, productName, categoryID, categoryName);
				// Determining whether this is a pregnancy test
				boolean isPregnancyTest = rs.getBoolean("isPregnancyTest");
				if (isPregnancyTest) {
					pregnancyTest = product;
				}
				productList.add(product);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		// Ensures that the pregnancyTest and productMap objects have been initialized
		return (pregnancyTest != null);
	}


	/**
	 * Initializes the list of categories.
	 * @return successfully initialized
	 */
	private boolean initializeCategoryList() {
		categoryList = new ArrayList<Category>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT categoryID, categoryName from Category");
			while (rs.next()) {
				int categoryID = rs.getInt("categoryID");
				String categoryName = rs.getString("categoryName");
				Category category = new Category(categoryID, categoryName);
				categoryList.add(category);
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return categoryList != null;
	}


	/**
	 * Reads in the Affiliation relation and populates two local data structures
	 * that store what amount of credit and what pregnancy test subsidy a given
	 * affiliation qualifies for.
	 * @return successfully initialized
	 */
	private boolean initializeAffiliationMaps() {
		affiliationMap = new HashMap<Integer, Affiliation>();
		affiliations = new ArrayList<Affiliation>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Affiliation");
			while (rs.next()) {
				int affiliationID = rs.getInt("affiliationID");
				int affiliationCredit = rs.getInt("affiliationCredit");
				boolean qualifiesForSubsidy = rs.getBoolean("qualifiesForSubsidy");
				String affiliationName = rs.getString("affiliationName");
				Affiliation affiliation = new Affiliation(affiliationID, affiliationName, affiliationCredit, qualifiesForSubsidy);
				affiliationMap.put(affiliationID, affiliation);
				affiliations.add(affiliation);

			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return (affiliationMap != null && affiliations != null);
	}


	/**
	 * Tests to see if the specified client exists in the client database. If so,
	 * the corresponding client object is created and returned to the user. If a
	 * client is not in the database, lookupClient returns null.
	 * @param SUID
	 * @return a client object retrieved from the database, if it exists
	 */
	public Client lookupClient(int SUID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Client WHERE SUID = ?");
			pstmt.setInt(1, SUID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int creditAvailable = rs.getInt("creditAvailable");
				boolean pregnancyTestUsed = rs.getBoolean("pregnancyTestUsed");
				int affiliationID = rs.getInt("affiliationID");
				Affiliation affiliation = affiliationMap.get(affiliationID);
				boolean qualifiesForPregnancyTestSubsidy = affiliation.qualifiesForPregnancyTest();
				Client client = 
					new Client(SUID, affiliationID, creditAvailable, 
							pregnancyTestUsed, qualifiesForPregnancyTestSubsidy);
				return client;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}


	/**
	 * Tests whether an affiliation name is already in use.
	 * @param name name to be tested for uniqueness
	 * @return whether name valid
	 */
	public boolean validProductName (String name) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Product WHERE productName = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Adds a new product to the database.
	 * @param name the name of the product to be added
	 * @param price the price in cents of the product to be added
	 * @param cost the cost in cents of the product to be added
	 * @param categoryID the category of the product to be added
	 * @return successfully added
	 */
	public boolean addProduct(String name, int price, int cost, int categoryID) {
		int productID = 0;
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(productID) AS maxID FROM Product where categoryID = ?");
			pstmt.setInt(1, categoryID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				productID = rs.getInt("maxID");
			}
			if (productID > 0) {
				productID++;
			} else {
				productID = categoryID * MINIMUM_PRODUCT_ID_FACTOR;
			}
			pstmt = connection.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?, 0, ?)") ;
			pstmt.setInt(1, productID);
			pstmt.setString(2, name);
			pstmt.setInt(3, price);
			pstmt.setInt(4, cost);
			pstmt.setInt(5, categoryID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Updates the given product in the database.
	 * @param newName the new name of the product
	 * @param oldName the old name of the product
	 * @param price the retail price in cents of the product
	 * @param cost the seller cost in cents of the product
	 * @param categoryID the categoryID of the product
	 * @param productID the productID of the product
	 * @return successfully updated
	 */
	public boolean updateProduct (String newName, String oldName, int price, int cost, int categoryID, int productID) {
		if (!newName.equals(oldName) && !validProductName(newName)) {
			return false;
		}
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE Product SET productName = ?, price = ?, cost = ? WHERE productID = ?") ;
			pstmt.setString(1, newName);
			pstmt.setInt(2, price);
			pstmt.setInt(3, cost);
			pstmt.setInt(4, productID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Deletes the given product from the database.
	 * @param productID the productID of the product to be deleted
	 * @return successfully deleted
	 */
	public boolean deleteProduct(int productID) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM PurchasedProduct WHERE productID = ?");
			pstmt.setInt(1, productID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			pstmt = connection.prepareStatement("DELETE FROM Product WHERE productID = ?");
			pstmt.setInt(1, productID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Tests whether an affiliation name is already in use.
	 * @param name name to be tested for uniqueness
	 * @return whether name valid
	 */
	public boolean validAffiliationName (String name) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Affiliation WHERE affiliationName = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	

	/**
	 * Adds a new affiliation to the database.
	 * @param name the name of the affiliation to be added
	 * @param credit the credit in cents associated with the affiliation
	 * @param subsidyOn whether the affiliation receives a pregnancy test subsidy
	 * @return successfully added
	 */
	public boolean addAffiliation (String name, int credit, boolean subsidyOn) {
		int affiliationID = 0;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(affiliationID) as maxID from Affiliation");
			if (rs.next()) {
				affiliationID = rs.getInt("maxID");
			}
			if (affiliationID > 0) {
				affiliationID++;
			} else {
				affiliationID = MINIMUM_AFFILIATION_ID;
			}
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Affiliation VALUES (?, ?, ?, ?)") ;
			pstmt.setInt(1, affiliationID);
			pstmt.setString(2, name);
			pstmt.setInt(3, credit * -1);
			pstmt.setBoolean(4, subsidyOn);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Updates the given affiliation in the database.
	 * @param newName the new name of the affiliation
	 * @param oldName the old name of the affiliation
	 * @param credit the credit in cents associated with the affiliation
	 * @param subsidyOn whether the affiliation receives a pregnancy test subsidy
	 * @param affiliationID the affiliationID of the affiliation to be updated
	 * @return successfully updated
	 */
	public boolean updateAffiliation (String newName, String oldName, int credit, boolean subsidyOn, int affiliationID) {
		if (!newName.equals(oldName) && !validAffiliationName(newName)) {
			return false;
		}
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE Affiliation set affiliationName = ?, " +
			"affiliationCredit = ?, qualifiesForSubsidy = ? where affiliationID = ?") ;
			pstmt.setString(1, newName);
			pstmt.setInt(2, credit * -1);
			pstmt.setBoolean(3, subsidyOn);
			pstmt.setInt(4, affiliationID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Deletes the affiliation from the database.
	 * @param affiliationID the affiliation ID of the affiliation to be deleted.
	 * @return successfully deleted
	 */
	public boolean deleteAffiliation(int affiliationID) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * from Client WHERE affiliationID = ?");
			pstmt.setInt(1, affiliationID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			pstmt = connection.prepareStatement("DELETE FROM Affiliation WHERE affiliationID = ?");
			pstmt.setInt(1, affiliationID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Tests whether a category name is already in use.
	 * @param name name to be tested for uniqueness
	 * @return whether name valid
	 */
	public boolean validCategoryName (String name) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Category WHERE categoryName = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Adds a new category to the database.
	 * @param name the name of the category to be added
	 * @return successfully added
	 */
	public boolean addCategory (String name) {
		int categoryID = 0;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(categoryID) as maxID from Category");
			if (rs.next()) {
				categoryID = rs.getInt("maxID");
			}
			if (categoryID > 0) {
				categoryID++;
			} else {
				categoryID = MINIMUM_CATEGORY_ID;
			}
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Category VALUES (?, ?)") ;
			pstmt.setInt(1, categoryID);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Updates the given category in the database.
	 * @param newName the new name of the category
	 * @param oldName the old name of the category
	 * @param categoryID the categoryID of the category to be updated
	 * @return successfully updated
	 */
	public boolean updateCategory (String newName, String oldName, int categoryID) {
		if (!newName.equals(oldName) && !validCategoryName(newName)) {
			// tests to ensure a new name is not already in use for another category
			return false;
		}
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE Category SET categoryName = ? WHERE categoryID = ?") ;
			pstmt.setString(1, newName);
			pstmt.setInt(2, categoryID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Deletes the given category from the database.
	 * @param categoryID the categoryID of the category to be deleted
	 * @return successfully deleted
	 */
	public boolean deleteCategory(int categoryID) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("SELECT * from Product WHERE categoryID = ?");
			pstmt.setInt(1, categoryID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			pstmt = connection.prepareStatement("DELETE FROM Category WHERE categoryID = ?");
			pstmt.setInt(1, categoryID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Writes all information about a purchase to the database.
	 * @param purchase the purchase to be written
	 * @return successfully written
	 */
	public boolean writePurchase (PurchaseModel purchase) {

		int purchaseID = 0;

		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = null;
			pstmt = connection.prepareStatement("SELECT Max(purchaseID) AS maxID from Purchase");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				purchaseID = rs.getInt("maxID");
			}
			if (purchaseID > 0) {
				purchaseID++;
			} else {
				purchaseID = MINIMUM_PURCHASE_ID;
			}
			
			purchase.setPurchaseID(purchaseID);

			int[] totals = purchase.getTotals();
			Client client = purchase.getCurrentClient();

			int creditUsed = -1 * totals[CREDIT] + -1 * totals[PT_SUBSIDY];
			int total = totals[TOTAL];
			Date date= new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			int affiliationID = client.getAffiliation();

			pstmt =  connection.prepareStatement("INSERT INTO Purchase VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, purchaseID);
			pstmt.setString(2, timestamp.toString());
			pstmt.setInt(3, total);
			pstmt.setInt(4, creditUsed);
			pstmt.setInt(5, affiliationID);

			pstmt.executeUpdate();

			if (!writePurchasedProducts(purchase, purchaseID)) { 
			// Calls for all products in purchase to be written to database. Returns false on failure.
				return false;
			}

			// Either writes new client to database or creates new client entry.
			if (purchase.isNewClient()) {
				if (!addClient(client, totals)) {
					return false;
				}
			} else if (!updateClient(client, totals)) {
				return false;
			}

			connection.commit();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println("In writePurchase" + e.getMessage());
			return false;

		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}


	/**
	 * Write the products in a purchase to the database.
	 * @param purchase the purchase to be written to the database
	 * @param purchaseID the purchaseID of the purchase
	 * @return successfully written
	 */
	public boolean writePurchasedProducts (PurchaseModel purchase, int purchaseID) {
		try {

			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO PurchasedProduct VALUES (?, ?, ?)");

			HashMap<Product, Integer> productsPurchased = purchase.getPurchaseProducts();

			Iterator it = productsPurchased.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Product, Integer> pair = (Entry<Product, Integer>) it.next();
				Product product = pair.getKey();
				int productID = product.getProductID();
				int quantity = pair.getValue();
				pstmt.setInt(1, purchaseID);
				pstmt.setInt(2, productID);
				pstmt.setInt(3, quantity);

				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	
	/**
	 * Removes a purchase and all client updates based on it from the database
	 * @param purchase the purchase to be voided
	 * @return successfully voided
	 */
	public boolean voidPurchase (PurchaseModel purchase) {

		int purchaseID = purchase.getPurchaseID();

		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = null;
			pstmt = connection.prepareStatement("SELECT Max(purchaseID) AS maxID from Purchase");


			int[] totals = purchase.getTotals();
			Client client = purchase.getCurrentClient();

			pstmt =  connection.prepareStatement("DELETE FROM Purchase WHERE purchaseID = ?");
			pstmt.setInt(1, purchaseID);
			pstmt.executeUpdate();

			pstmt = connection.prepareStatement("DELETE FROM PurchasedProduct WHERE purchaseID = ?");
			pstmt.setInt(1, purchaseID);
			pstmt.executeUpdate();

			int SUID = client.getSUID();
			int credit = totals[CREDIT];
			boolean testUsed = !client.isPregnancyTestRedeemed(); //sets the test to be unused except if it was used prior to this purchase

			pstmt = connection.prepareStatement("UPDATE Client SET creditAvailable = ?, pregnancyTestUsed = ? WHERE SUID = ?");
			pstmt.setInt(1, credit);
			pstmt.setBoolean(2, testUsed);
			pstmt.setInt(3, SUID);

			pstmt.executeUpdate();	

			connection.commit();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println("In writePurchase" + e.getMessage());
			return false;

		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}


	/**
	 * Adds a client to the database.
	 * @param client the client to be added
	 * @param totals the totals of a purchase
	 * @return successfully added
	 */
	private boolean addClient (Client client, int[] totals) {
		try {
			int SUID = client.getSUID();
			int credit = client.getCredit() - totals[CREDIT];
			int affiliationID = client.getAffiliation();
			boolean testUsed = totals[PT_SUBSIDY] != 0;

			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Client VALUES(?, ?, ?, ?)");
			pstmt.setInt(1, SUID);
			pstmt.setInt(2, credit);
			pstmt.setBoolean(3, testUsed);
			pstmt.setInt(4, affiliationID);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}


	/**
	 * Update the given client in the database.
	 * @param client the client to be updated
	 * @param totals the totals of a purchase
	 * @return successfully updated
	 */
	private boolean updateClient (Client client, int[] totals) {
		try {
			int SUID = client.getSUID();
			int credit = client.getCredit() - totals[CREDIT];
			boolean testUsed = totals[PT_SUBSIDY] != 0 || client.isPregnancyTestRedeemed(); // true if unused in this purchase or if used previously

			PreparedStatement pstmt = connection.prepareStatement("UPDATE Client SET creditAvailable = ?, pregnancyTestUsed = ? WHERE SUID = ?");
			pstmt.setInt(1, credit);
			pstmt.setBoolean(2, testUsed);
			pstmt.setInt(3, SUID);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}
}
