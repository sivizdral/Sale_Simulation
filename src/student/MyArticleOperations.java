package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rs.etf.sab.operations.ArticleOperations;

public class MyArticleOperations implements ArticleOperations {

	@Override
	public int createArticle(int arg0, String arg1, int arg2) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "INSERT INTO Article (Price, QuantityOnStock, Name, IdS) VALUES (?, 0, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);)
		{
        	pstmt.setInt(1, arg2);
        	pstmt.setString(2, arg1);
        	pstmt.setInt(3, arg0);
			
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

}
