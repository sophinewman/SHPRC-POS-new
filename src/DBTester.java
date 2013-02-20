
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ClassNotFoundException{
		// load up the JDBC Driver with the class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/sophi/Documents/Workspace/SHPRC-POS/toy.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); //timeout is set to 30 seconds

			statement.executeUpdate("insert into Alphabet values('F')");
			ResultSet rs = statement.executeQuery("select distinct * from ALphabet");
			while(rs.next()) {
				System.out.println("Letter: " + rs.getString("letter"));
			}
		}

		catch(SQLException e)
		{
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if (connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				System.err.println(e);
			}
		}
	}
}

