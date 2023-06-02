package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.ShopOperations;

public class ci190183_ShopOperations implements ShopOperations {

	@Override
	public int createShop(String arg0, String arg1) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql1 = "SELECT IdT FROM Town WHERE Name = ?";
		
		int cityId = 0;
		
		try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);)
		{
        	pstmt1.setString(1, arg1);
			
        	ResultSet set = pstmt1.executeQuery();

            if (set.next()) {
                cityId = set.getInt("IdT");
            } else
            	return -1;
            
            String sql2 = "INSERT INTO Store (IdT, AccountBalance, Name) VALUES (?, 0, ?)";
    		
    		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);)
    		{
            	pstmt2.setInt(1, cityId);
            	pstmt2.setString(2, arg0);
    			int rowsInserted = pstmt2.executeUpdate();
    			
    			ResultSet generatedKeys = pstmt2.getGeneratedKeys();
    			
    			if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        
        } catch (SQLException e) {
        	e.printStackTrace();
        }
			
		return -1;
	}

	@Override
	public int getArticleCount(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT QuantityOnStock FROM Article WHERE IdA = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getInt("QuantityOnStock");
            } else
            	return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public List<Integer> getArticles(int arg0) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdA FROM Article WHERE IdS = ?";
        
		List<Integer> ids = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setInt(1, arg0);

            ResultSet set = stmt.executeQuery();

            while (set.next()) {
                ids.add(set.getInt("IdA"));
            }

            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public int getCity(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Store WHERE IdS = ?";
		
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
	public int getDiscount(int arg0) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT Discount FROM Store WHERE IdS = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg0);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return set.getInt("Discount");
            } else
            	return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public int increaseArticleCount(int arg0, int arg1) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "UPDATE Article SET QuantityOnStock = QuantityOnStock + ? WHERE IdA = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg1);
        	pstmt.setInt(2, arg0);
			
        	int rows = pstmt.executeUpdate();
        	
        	if (rows < 1) return -1;
        	
        	String sql2 = "SELECT QuantityOnStock FROM Article WHERE IdA = ?";
    		
    		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);)
    		{
            	pstmt2.setInt(1, arg0);
    			
            	ResultSet set = pstmt2.executeQuery();
            	
            	if (set.next()) {
            		return set.getInt("QuantityOnStock");
            	}
            	else return -1;
                
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public int setCity(int arg0, String arg1) {
		Connection conn = DB.getInstance().getConnection();
		
		int cityId = 0;
		
		String sql = "SELECT IdT FROM Town WHERE Name = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setString(1, arg1);
			
        	ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                cityId = set.getInt("IdT");
            } else
            	return -1;
            
            String sql2 = "UPDATE Store SET IdT = ? WHERE IdS = ?";
    		
    		try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);)
    		{
            	pstmt2.setInt(1, cityId);
            	pstmt2.setInt(2, arg0);
    			
            	int ret = pstmt2.executeUpdate();

                if (ret > 0) return 1;
                else return -1;
                
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	public int setDiscount(int arg0, int arg1) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "UPDATE Store SET Discount = ? WHERE IdS = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
        	pstmt.setInt(1, arg1);
        	pstmt.setInt(2, arg0);
			
        	int rows = pstmt.executeUpdate();

            if (rows > 0) return 1;
            else return -1;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}

}
