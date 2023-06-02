package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs.etf.sab.operations.TransactionOperations;

public class MyTransactionOperations implements TransactionOperations {

	@Override
	public BigDecimal getAmmountThatBuyerPayedForOrder(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getAmmountThatShopRecievedForOrder(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBuyerTransactionsAmmount(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT SUM(Amount) AS 'Total' FROM Transaction WHERE IdB = ? GROUP BY IdB";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("Total");
            } else
            	return new BigDecimal(-1);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getShopTransactionsAmmount(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT SUM(Amount) AS 'Total' FROM Transaction WHERE IdS = ? GROUP BY IdS";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("Total");
            } else
            	return new BigDecimal(-1);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getSystemProfit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getTimeOfExecution(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT ExecutionTime FROM Transaction WHERE IdT = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                if (set.getTimestamp("ExecutionTime") != null) {
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis(set.getTimestamp("ExecutionTime").getTime());
                    return time;
                }
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public BigDecimal getTransactionAmount(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT Amount FROM Transaction WHERE IdT = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("Amount");
            } else
            	return new BigDecimal(-1);
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public int getTransactionForBuyersOrder(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Transaction WHERE IdO = ? AND IdB IS NOT NULL";
		
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
	public int getTransactionForShopAndOrder(int arg0, int arg1) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Transaction WHERE IdS = ? AND IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg1);
        	pstmt.setInt(2, arg0);
			
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
	public List<Integer> getTransationsForBuyer(int arg0) {
		Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdT FROM [Transaction] WHERE IdB = ?";
        
		List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setInt(1, arg0);

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
	public List<Integer> getTransationsForShop(int arg0) {
		Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdT FROM [Transaction] WHERE IdS = ?";
        
		List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setInt(1, arg0);

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

}
