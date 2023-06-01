package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs.etf.sab.operations.OrderOperations;

public class MyOrderOperations implements OrderOperations {

	@Override
	public int addArticle(int arg0, int arg1, int arg2) {
		Connection conn = DB.getInstance().getConnection();
		
        int id = 0;

        String sql = "SELECT QuantityOnStock FROM Article WHERE IdA = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, arg1);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (arg2 > rs.getInt("QuantityOnStock")) {
                    return -1;
                }
                else {
                	
                	String sql2 = "SELECT IdI FROM Item WHERE IdA = ? AND IdO = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                        pstmt2.setInt(1, arg1);
                        pstmt2.setInt(2, arg0);
                        


                        ResultSet rs2 = pstmt2.executeQuery();
                        if (rs2.next()) {
                        	id = rs2.getInt("IdI");
                        }
                        else {
                        	String sql3 = "INSERT INTO Item (IdA, IdO, Quantity) VALUES (?, ?, 0)";
                            try (PreparedStatement pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS)) {
                                pstmt3.setInt(1, arg1);
                                pstmt3.setInt(2, arg0);

                                int rows = pstmt3.executeUpdate();
                                
                                if (rows < 1) {
                                	return -1;
                                }
                                else {
                                	ResultSet set3 = pstmt3.getGeneratedKeys();
                                	id = set3.getInt(1);
                                }
                                
                                
                            } catch (SQLException e) {
                            }
                        	
                        }
                        
                        String sql4 = "UPDATE Item SET Quantity = Quantity + ? WHERE IdI = ?";
                        try (PreparedStatement pstmt4 = conn.prepareStatement(sql4)) {
                            pstmt4.setInt(1, arg2);
                            pstmt4.setInt(2, id);

                            pstmt4.executeUpdate();
                            
                        } catch (SQLException e) {
                        } 
                        
                    } catch (SQLException e) {
                    }
                	
                }
            }           
            
        } catch (SQLException e) {
        }
        
        return -1;
	}

	@Override
	public int completeOrder(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBuyer(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdB FROM Order WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getInt("IdB");
            } else
            	return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public BigDecimal getDiscountSum(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT Discount FROM Order WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("Discount");
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public BigDecimal getFinalPrice(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT FinalPrice FROM Order WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getBigDecimal("FinalPrice");
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(-1);
	}

	@Override
	public List<Integer> getItems(int arg0) {
		Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdI FROM Item WHERE IdO = ?";
        
		List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setInt(1, arg0);

            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                ids.add(set.getInt("IdI"));
            }

            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public int getLocation(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT Location FROM Order WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getInt("Location");
            } else
            	return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public Calendar getRecievedTime(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT ReceivedTime FROM Order WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                if (set.getTimestamp("ReceivedTime") != null) {
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis(set.getTimestamp("ReceivedTime").getTime());
                    return time;
                }
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Calendar getSentTime(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT SentTime FROM [Order] WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                if (set.getTimestamp("SentTime") != null) {
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis(set.getTimestamp("SentTime").getTime());
                    return time;
                }
            }
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getState(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT State FROM [Order] WHERE IdO = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getString("State");
            } else
            	return "";
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	@Override
	public int removeArticle(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
