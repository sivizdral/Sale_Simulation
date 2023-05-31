package student;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import rs.etf.sab.operations.GeneralOperations;

public class MyGeneralOperations implements GeneralOperations {

	@Override
	public void eraseAll() {
		Connection conn = DB.getInstance().getConnection();
		
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("DELETE FROM [Transaction]");
			stmt.executeUpdate("DELETE FROM [Selected]");
			stmt.executeUpdate("DELETE FROM [Article]");
			stmt.executeUpdate("DELETE FROM [Store]");
			stmt.executeUpdate("DELETE FROM [Order]");
			stmt.executeUpdate("DELETE FROM [Buyer]");
			stmt.executeUpdate("DELETE FROM [Sale]");
			stmt.executeUpdate("DELETE FROM [Path]");
			stmt.executeUpdate("DELETE FROM [Town]");
        } catch (SQLException e) {
        }
		
	}
	
	private Calendar myTime = null;

	@Override
	public Calendar getCurrentTime() {
		return myTime;
	}

	@Override
	public void setInitialTime(Calendar arg0) {
		myTime = arg0;
	}

	@Override
	public Calendar time(int arg0) {
		myTime.add(Calendar.DAY_OF_MONTH, arg0);
		return myTime;
	}

}
