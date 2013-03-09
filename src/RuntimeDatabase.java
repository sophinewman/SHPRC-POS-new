import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

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
public class RuntimeDatabase {

	/* The JDBC Connection that backs the class */
	private Connection connection;

	private Vector<Affiliation> affiliations;
	private HashMap<Integer, Affiliation> affiliationMap;
	private HashMap<Integer, Product> productMap;
	private ArrayList<Product> productList;
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
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
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
			ResultSet rs = stmt.executeQuery(("SELECT * FROM Product ORDER BY productID"));
			// Read in the information about each row and store in Product object
			// Store the flagged pregnancy test product in an instance variable
			while (rs.next()) {
				// Fetching row information
				int productID = rs.getInt("productID");
				String productName = rs.getString("productName");
				int price = rs.getInt("price");
				int categoryID = rs.getInt("CategoryID");
				// Storing in Product object
				Product product = new Product(productID, price, productName, categoryID);
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
		}
		// Ensures that the pregnancyTest and productMap objects have been initialized
		return (pregnancyTest != null && productMap != null);
	}


	/**
	 * Reads in the Affiliation relation and populates two local data structures
	 * that store what amount of credit and what pregnancy test subsidy a given
	 * affiliation qualifies for.
	 * @return successfully initialized
	 */
	private boolean initializeAffiliationMaps() {
		affiliationMap = new HashMap<Integer, Affiliation>();
		affiliations = new Vector<Affiliation>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Affiliation");
			for (int i = 0; rs.next(); i++) {
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
		}
		return (affiliationMap != null && affiliations != null);

	}
	
	public Vector<Affiliation> getAffiliations() {
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
}
