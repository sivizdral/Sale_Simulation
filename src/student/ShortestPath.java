package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortestPath {
	
	ShortestPath shortestPath = null;
	
	private ShortestPath() {
		
	}
	
	public ShortestPath getInstance() {
		if (shortestPath == null) shortestPath = new ShortestPath();
		return shortestPath;
	}
	
	public BigDecimal ShortestDistance(int town1, int town2) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "WITH CTE AS (\r\n"
				+ "    SELECT t1.IdT AS Town1, t2.IdT AS Town2, p1.Distance AS Distance,\r\n"
				+ "           CAST(t1.IdT AS VARCHAR(MAX)) + '->' + CAST(t2.IdT AS VARCHAR(MAX)) AS Path, 1 AS [Level]\r\n"
				+ "    FROM PATH p1\r\n"
				+ "    JOIN TOWN t1 ON p1.IdT1 = t1.IdT\r\n"
				+ "    JOIN TOWN t2 ON p1.IdT2 = t2.IdT\r\n"
				+ "    \r\n"
				+ "    UNION ALL\r\n"
				+ "    \r\n"
				+ "    SELECT c.Town1, p2.IdT2, CAST(c.Distance + p2.Distance AS decimal(10,3)) AS Distance,\r\n"
				+ "           c.Path + '->' + CAST(p2.IdT2 AS VARCHAR(MAX)), c.[Level] + 1\r\n"
				+ "    FROM CTE c\r\n"
				+ "    JOIN PATH p2 ON c.Town2 = p2.IdT1\r\n"
				+ "    WHERE c.[Level] < 20\r\n"
				+ ")\r\n"
				+ "SELECT TOP 1 Town1, Town2, MIN(Distance) AS SmallestDistance, Path AS ShortestPath\r\n"
				+ "FROM CTE\r\n"
				+ "WHERE (Town1 = ? AND Town2 = ?) OR (Town1 = ? AND Town2 = ?)\r\n"
				+ "GROUP BY Town1, Town2, Path\r\n"
				+ "ORDER BY SmallestDistance ASC;";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, town1);
        	stmt.setInt(2, town2);
        	stmt.setInt(3, town2);
        	stmt.setInt(4, town1);
        	
        	ResultSet set = stmt.executeQuery();
        	
        	if (set.next()) {
        		return set.getBigDecimal("SmallestDistance");
        	}
        	else return new BigDecimal(-1);
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
		return new BigDecimal(-1);
	}
	
	public String ShortestPathBetween(int town1, int town2) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "WITH CTE AS (\r\n"
				+ "    SELECT t1.IdT AS Town1, t2.IdT AS Town2, p1.Distance AS Distance,\r\n"
				+ "           CAST(t1.IdT AS VARCHAR(MAX)) + '->' + CAST(t2.IdT AS VARCHAR(MAX)) AS Path, 1 AS [Level]\r\n"
				+ "    FROM PATH p1\r\n"
				+ "    JOIN TOWN t1 ON p1.IdT1 = t1.IdT\r\n"
				+ "    JOIN TOWN t2 ON p1.IdT2 = t2.IdT\r\n"
				+ "    \r\n"
				+ "    UNION ALL\r\n"
				+ "    \r\n"
				+ "    SELECT c.Town1, p2.IdT2, CAST(c.Distance + p2.Distance AS decimal(10,3)) AS Distance,\r\n"
				+ "           c.Path + '->' + CAST(p2.IdT2 AS VARCHAR(MAX)), c.[Level] + 1\r\n"
				+ "    FROM CTE c\r\n"
				+ "    JOIN PATH p2 ON c.Town2 = p2.IdT1\r\n"
				+ "    WHERE c.[Level] < 20\r\n"
				+ ")\r\n"
				+ "SELECT TOP 1 Town1, Town2, MIN(Distance) AS SmallestDistance, Path AS ShortestPath\r\n"
				+ "FROM CTE\r\n"
				+ "WHERE (Town1 = ? AND Town2 = ?) OR (Town1 = ? AND Town2 = ?)\r\n"
				+ "GROUP BY Town1, Town2, Path\r\n"
				+ "ORDER BY SmallestDistance ASC;";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, town1);
        	stmt.setInt(2, town2);
        	stmt.setInt(3, town2);
        	stmt.setInt(4, town1);
        	
        	ResultSet set = stmt.executeQuery();
        	
        	if (set.next()) {
        		return set.getString("Path");
        	}
        	else return "";
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
		return "";
	}

}
