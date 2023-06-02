package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.CityOperations;


public class ci190183_CityOperations implements CityOperations {

	@Override
	public int connectCities(int arg0, int arg1, int arg2) {
		Connection conn = DB.getInstance().getConnection();
		
        String sql = "INSERT INTO Path(IdT1, IdT2, Distance) VALUES (?, ?, ?), (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, arg0);
        	stmt.setInt(2, arg1);
        	stmt.setInt(3, arg2);
        	stmt.setInt(4, arg1);
        	stmt.setInt(5, arg0);
        	stmt.setInt(6, arg2);
        	
        	int ret = stmt.executeUpdate();
            return ret;
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return -1;
	}

	@Override
	public int createCity(String arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "INSERT INTO Town (Name) VALUES (?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
        	pstmt.setString(1, arg0);
			int rowsInserted = pstmt.executeUpdate();
			
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			
			if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return -1;
	}

	@Override
	public List<Integer> getCities() {
		Connection conn = DB.getInstance().getConnection();
		
        String sql = "SELECT IdT FROM Town";
        List<Integer> ids = new ArrayList<>();

        try (Statement stmt = conn.createStatement(); ResultSet set = stmt.executeQuery(sql)) {

            while (set.next()) {
                ids.add(set.getInt("IdT"));
            }
            return ids;
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return null;
	}

	@Override
	public List<Integer> getConnectedCities(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
        String sql = "SELECT IdT2 AS 'IdT' FROM Path WHERE IdT1 = ? union SELECT IdT1 AS 'IdT' FROM Path WHERE IdT2 = ?";
        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, arg0);
        	stmt.setInt(2, arg0);
        	
        	ResultSet set = stmt.executeQuery();

            while (set.next()) {
                ids.add(set.getInt("IdT"));
            }
            return ids;
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return null;
	}

	@Override
	public List<Integer> getShops(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
        String sql = "SELECT IdS from Store WHERE IdT = ?";
        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, arg0);
        	
        	ResultSet set = stmt.executeQuery();

            while (set.next()) {
                ids.add(set.getInt("IdS"));
            }
            return ids;
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return null;
	}

}
