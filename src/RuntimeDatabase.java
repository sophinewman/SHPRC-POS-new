import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SHPRC-POS
 * RuntimeDatabase.java
 * Stores back end information to be retrieved by the Purchase (Model in MVC
 * class) at runtime. Because the amount of data in the back end is small and
 * largely read-only, runtime structures allow greater efficiency than directly
 * querying the back end.
 * 
 * @author Sophi Newman
 * @version 0.1 2/8/13
 */
public class RuntimeDatabase implements SHPRCConstants{

	/* The JDBC Connection that backs the class */
	private Connection connection;

	private ArrayList<Affiliation> affiliations;
	private HashMap<Integer, Affiliation> affiliationMap;
	private HashMap<Integer, Product> productMap;
	private ArrayList<Product> productList;
	private ArrayList<Category> categoryList;
	private Product pregnancyTest;

	/**
	 * Class constructor. Does not actually read in data and store it;
	 * instead, it ensures that the JDBC Class is successfully loaded.
	 * @throws ClassNotFoundException
	 */
	public RuntimeDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");

	}

	public HashMap<Integer, Product> getProductMap() {
		return productMap;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public ArrayList<Category> getCategoryList() {
		return categoryList;
	}

	public Category getCategory(int categoryID) {
		Category category = null;
		for (Category c : categoryList ) {
			if (c.getCategoryID() == categoryID) {
				category = c;
			}
		} 
		return category;
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
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Closes the Connection object that underpins the RuntimeDatabase class.
	 * @throws SQLException
	 */
	public void closeDatabase() throws SQLException {
		try {
			if (connection != null)
				connection.close();
		}
		catch(SQLException e) {
			System.err.println(e);
		}
	}


	/**
	 * Queries the database for the Product table and stores the ResultSet
	 * in the productMap HashMap from ProductID to Product. Also stores the
	 * Pregnancy Test object in the pregnancyTest instance variable.
	 * @return successfully initialized
	 */
	private boolean initializeProductMap() {
		productMap = new HashMap<Integer, Product>();
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
				// Add product to map of all products
				productMap.put(productID, product);
				productList.add(product);
			}
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		// Ensures that the pregnancyTest and productMap objects have been initialized
		return (pregnancyTest != null && productMap != null);
	}

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

	public ArrayList<Affiliation> getAffiliations() {
		return affiliations;
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
	 * Returns the credit a given affiliation has as a negative integer of cents.
	 * @param affiliationID the integer community/class affiliation ID to be looked up.
	 * @return the amount of credit a given affiliation receives
	 */
	public Affiliation getAffiliation(int affiliationID) {
		if (affiliationMap.containsKey(affiliationID)) {
			return affiliationMap.get(affiliationID);
		}
		return null;
	}


	/**
	 * Returns the product that the administrator has specified to be the pregnancy test.
	 * @return the pregnancy test Product object
	 */
	public Product getPregnancyTestProduct() {
		return pregnancyTest;
	}


	/**
	 * Returns the product associated with the specified productID.
	 * @param productID the unique integer product ID to be looked up
	 * @return the product associated with the specified productID
	 */
	public Product getProduct (int productID) {
		return productMap.get(productID);
	}

	public boolean validProductName (String name) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean addProduct(String name, int price, int cost, int categoryID) {
		int productID = 0;
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(productID) AS maxID FROM Product where categoryID = ?");
			pstmt.setInt(1, categoryID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				productID = rs.getInt("maxID");
			}
			if (productID > 0) {
				productID++;
			} else {

				productID = categoryID * 100;
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

	public boolean deleteProduct(int productID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean deleteAffiliation(int affiliationID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean deleteCategory(int categoryID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean validAffiliationName (String name) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean validCategoryName (String name) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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

	public boolean addAffiliation (String name, int credit, boolean subsidyOn) {
		int affiliationID = 0;
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(affiliationID) as maxID from Affiliation");
			if (rs.next()) {
				affiliationID = rs.getInt("maxID");
			}
			if (affiliationID > 0) {
				affiliationID++;
			} else {
				affiliationID = 1000;
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

	public boolean updateAffiliation (String name, int credit, boolean subsidyOn, int affiliationID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */

			PreparedStatement pstmt = connection.prepareStatement("UPDATE Affiliation set affiliationName = ?, affiliationCredit = ?, qualifiesForSubsidy = ? where affiliationID = ?") ;
			pstmt.setString(1, name);
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

	public boolean addCategory (String name) {
		int categoryID = 0;
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(categoryID) as maxID from Category");
			if (rs.next()) {
				categoryID = rs.getInt("maxID");
			}
			if (categoryID > 0) {
				categoryID++;
			} else {
				categoryID = 1;
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

	public boolean updateProduct (String name, int price, int cost, int categoryID, int productID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */

			PreparedStatement pstmt = connection.prepareStatement("UPDATE Product SET productName = ?, price = ?, cost = ? WHERE productID = ?") ;
			pstmt.setString(1, name);
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

	public boolean updateCategory (String name, int categoryID) {
		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */

			PreparedStatement pstmt = connection.prepareStatement("UPDATE Category SET categoryName = ? WHERE categoryID = ?") ;
			pstmt.setString(1, name);
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

	private boolean addClient (Client client, int[] totals) {
		try {
			int SUID = client.getSUID();
			System.out.println("client.getCredit() returns "+client.getCredit());
			System.out.println("totals[credit] returns" + totals[CREDIT]);
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
	
	private boolean updateClient (Client client, int[] totals) {
		try {
			int SUID = client.getSUID();
			int credit = client.getCredit() - totals[CREDIT];
			boolean testUsed = totals[PT_SUBSIDY] != 0; //stays at zero if unused
			
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


	public boolean writePurchase (PurchaseModel purchase) {

		int purchaseID = 0;

		try {
			/* A PreparedStatement is used here to ensure that the SQL query is correctly formatted
			 and to allow for more easily human-readable variable insertion. */
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
			
			System.out.println("purchaseID is : "+purchaseID);

			int[] totals = purchase.getTotals();
			Client client = purchase.getCurrentClient();
			
			if (purchase.isNewClient()) {
				if (!addClient(client, totals)) {
					return false;
				}
			} else if (!updateClient(client, totals)) {
				return false;
			}

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
				return false;
			}

			connection.commit();
			connection.setAutoCommit(true);
		}
		catch (SQLException e) {
			System.err.println("In writePurchase" + e.getMessage());
			return false;
		}
		catch (NullPointerException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
}

