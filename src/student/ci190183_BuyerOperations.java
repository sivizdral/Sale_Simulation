package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.BuyerOperations;

public class ci190183_BuyerOperations implements BuyerOperations {

	@Override
	public int createBuyer(String arg0, int arg1) {
		Connection conn = DB.getInstance().getConnection();
        
        String sql2 = "INSERT INTO Buyer (AccountBalance, Name, IdT) VALUES (0, ?, ?)";
		
		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
        	pstmt2.setString(1, arg0);
        	pstmt2.setInt(2, arg1);
			int rowsInserted = pstmt2.executeUpdate();
			
			ResultSet generatedKeys = pstmt2.getGeneratedKeys();
			
			if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return -1;
	}

	@Override
	public int createOrder(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "INSERT INTO [Order] (IdB, State) VALUES (?, 'created')";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
        	pstmt.setInt(1, arg0);
			
        	pstmt.executeUpdate();
        	
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
	public int getCity(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Buyer WHERE IdB = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getInt("IdT");
            } else
            	return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public BigDecimal getCredit(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT AccountBalance FROM Buyer WHERE IdB = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("AccountBalance");
            } else
            	return new BigDecimal(-1);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public List<Integer> getOrders(int arg0) {
		Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdO FROM [Order] WHERE IdB = ?";
        
		List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setInt(1, arg0);

            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                ids.add(set.getInt("IdO"));
            }

            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public BigDecimal increaseCredit(int arg0, BigDecimal arg1) {
		Connection conn = DB.getInstance().getConnection();
        
        String sql2 = "UPDATE Buyer SET AccountBalance = AccountBalance + ? WHERE IdB = ?";
		
		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);)
		{
        	pstmt2.setBigDecimal(1, arg1);
        	pstmt2.setInt(2, arg0);
			
        	int ret = pstmt2.executeUpdate();

            if (ret < 1) return new BigDecimal(-1);
            
            String sql = "SELECT AccountBalance FROM Buyer WHERE IdB = ?";
    		
    		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
    		{
            	pstmt.setInt(1, arg0);
    			
            	ResultSet set = pstmt.executeQuery();

                if (set.next()) {
                	return set.getBigDecimal("AccountBalance");
                }
                
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
            
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new BigDecimal(-1);
	}

	@Override
	public int setCity(int arg0, int arg1) {
		Connection conn = DB.getInstance().getConnection();
            
        String sql2 = "UPDATE Buyer SET IdT = ? WHERE IdB = ?";
		
		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);)
		{
        	pstmt2.setInt(1, arg1);
        	pstmt2.setInt(2, arg0);
			
        	int ret = pstmt2.executeUpdate();

            if (ret > 0) return 1;
            else return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

}
