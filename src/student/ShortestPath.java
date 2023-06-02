package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rs.etf.sab.operations.GeneralOperations;

public class ShortestPath {
	
	private static ShortestPath shortestPath = null;
	
	public GeneralOperations general;
	
	private ShortestPath() {

	}
	
	private class City {
		public int cityId;
		public int remainingDistance;
	}
	
	private static HashMap<Integer, List<City>> cityMap = new HashMap<>();
	
	public static ShortestPath getInstance() {
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
				+ "    JOIN PATH p2 ON (c.Town2 = p2.IdT1 OR c.Town2 = p2.IdT2 OR c.Town1 = p2.IdT1 OR c.Town1 = p2.IdT2)\r\n"
				+ "    WHERE c.[Level] < 20 AND c.Path NOT LIKE '%' + CAST(p2.IdT2 AS VARCHAR(MAX)) + '%'\r\n"
				+ "\r\n"
				+ ")\r\n"
				+ "SELECT Town1, Town2, MIN(Distance) AS SmallestDistance, Path AS ShortestPath\r\n"
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
				+ "    JOIN PATH p2 ON (c.Town2 = p2.IdT1 OR c.Town2 = p2.IdT2 OR c.Town1 = p2.IdT1 OR c.Town1 = p2.IdT2)\r\n"
				+ "    WHERE c.[Level] < 20 AND c.Path NOT LIKE '%' + CAST(p2.IdT2 AS VARCHAR(MAX)) + '%'\r\n"
				+ "\r\n"
				+ ")\r\n"
				+ "SELECT Town1, Town2, MIN(Distance) AS SmallestDistance, Path AS ShortestPath\r\n"
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
        		return set.getString("ShortestPath");
        	}
        	else return "";
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
		return "";
	}
	
	public int ClosestTownWithShop(int town) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Store";
		
		List<Integer> ids = new ArrayList<>();
		BigDecimal shortest_distance = new BigDecimal(1000000);
		int closest_town_with_shop = 0;
		
		try (Statement stmt = conn.createStatement(); ResultSet set = stmt.executeQuery(sql)) {

            while (set.next()) {
                ids.add(set.getInt("IdT"));
            }
            
            for (Integer id : ids) {
            	if (id.intValue() == town) return town;
            	
            	BigDecimal distance = this.ShortestDistance(town, id.intValue());
            	if (distance.intValue() < shortest_distance.intValue()) {
            		shortest_distance = distance;
            		closest_town_with_shop = id.intValue();
            	}
            }
            
            return closest_town_with_shop;
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
		return -1;
	}
	
	public int maximalDistanceFromClosestShop(int orderId, int location) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT FROM Item I JOIN Article A ON I.IdA = A.IdA JOIN Store S ON A.IdS = S.IdS WHERE I.IdO = ?";
		
		List<Integer> ids = new ArrayList<>();
		int longest_distance = 0;
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, orderId);

			ResultSet set = stmt.executeQuery();
			
            while (set.next()) {
                ids.add(set.getInt("IdT"));
            }
            
            for (Integer id : ids) {
            	
            	BigDecimal distance = this.ShortestDistance(location, id.intValue());
            	if (distance.intValue() > longest_distance) {
            		longest_distance = distance.intValue();
            	}
            }
            
            return longest_distance;
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
		return -1;
	}
	
	public void addOrderToCityMap(int orderId) {
		Connection conn = DB.getInstance().getConnection();
		
		String sql = "SELECT IdT, Location FROM Buyer B JOIN [Order] O ON B.IdB = O.IdB WHERE O.IdO = ?";
		int buyerLocation = 0;
		int orderLocation = 0;
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, orderId);

			ResultSet set = stmt.executeQuery();
			
            if (set.next()) {
                buyerLocation = set.getInt("IdT");
                orderLocation = set.getInt("Location");
            }
            
            String path = this.ShortestPathBetween(orderLocation, buyerLocation);
            List<Integer> towns = new ArrayList<>();

            String[] parts = path.split("->");
            for (String part : parts) {
                int number = Integer.parseInt(part.trim());
                towns.add(number);
            }
            
            if (towns.get(0) != orderLocation) {
            	Collections.reverse(towns);
            }
            
            if (towns.size() == 1) {
            	City c = new City();
            	c.cityId = towns.get(0);
            	c.remainingDistance = 0;
            	List<City> l = new ArrayList<>();
            	l.add(c);
            	ShortestPath.cityMap.put(new Integer(orderId), l);
            } else {
            	List<City> list = new ArrayList<>();
            	for (int i = 1; i < towns.size(); i++) {
            		BigDecimal distance = this.ShortestDistance(towns.get(i-1), towns.get(i));
            		City c = new City();
            		c.cityId = towns.get(i);
            		c.remainingDistance = distance.intValue();
            		list.add(c);
            	}
            	ShortestPath.cityMap.put(new Integer(orderId), list);
            }
                                   
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		
	}
	
	public void passedDays(int daysPassed) {
		Set<Integer> keySet = ShortestPath.cityMap.keySet();
		
		for (Integer key : keySet) {
			
			List<City> list = ShortestPath.cityMap.get(key);
			
			Connection conn = DB.getInstance().getConnection();
			
			String sql = "SELECT DaysLeft, Location FROM [Order] WHERE IdO = ?";	
			int daysLeft = 0;
			int daysProcessed = 0;
			int location = 0;
			
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, key);

				ResultSet set = stmt.executeQuery();
				
	            if (set.next()) {
	                daysLeft = set.getInt("DaysLeft");
	                location = set.getInt("Location");
	            }
	            
	            if (daysLeft > 0) {
	            	if (daysLeft > daysPassed) {
	            		daysLeft = daysLeft - daysPassed;
	            		daysProcessed = daysPassed;
	            	} else {
	            		daysProcessed = daysLeft;
	            		daysLeft = 0;	            	
	            	}
	            	
	            	String sql2 = "UPDATE [Order] SET DaysLeft = ? WHERE IdO = ?";	
	    			
	    			try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
	    				stmt2.setInt(1, daysLeft);
	    				stmt2.setInt(2, key);
	    				
	    				int rows = stmt2.executeUpdate();
	    				
	    				if (rows < 1) throw new SQLException();
	    				
	    			} catch (SQLException e) {
	    	        	e.printStackTrace();
	    	        }
	            }
	            
	            int newLocation = location;
	            
	            while (daysProcessed < daysPassed) {
	            	if (list.size() == 0) {
	            		break;
	            	}
	            	City c = list.get(0);
	            	int left = daysPassed - daysProcessed;
	            	if (c.remainingDistance <= left) {
	            		newLocation = c.cityId;
	            		daysProcessed += c.remainingDistance;
	            		list.remove(0);
	            	} else {
	            		c.remainingDistance -= left;
	            		daysProcessed += left;
	            	}
	            }
	            
	            ShortestPath.cityMap.put(key, list);
	            
	            if (newLocation != location) {
	            	String sql2 = "UPDATE [Order] SET Location = ? WHERE IdO = ?";	
	    			
	    			try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
	    				stmt2.setInt(1, newLocation);
	    				stmt2.setInt(2, key);
	    				
	    				int rows = stmt2.executeUpdate();
	    				
	    				if (rows < 1) throw new SQLException();
	    				
	    			} catch (SQLException e) {
	    	        	e.printStackTrace();
	    	        }
	            }
	            
	            if (list.size() == 0) {
	            	String sql2 = "UPDATE [Order] SET State = ?, ReceivedTime = ? WHERE IdO = ?";	
	            	
	            	java.util.Date utilDate = this.general.getCurrentTime().getTime();
	                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	                
	                long updatedTime = sqlDate.getTime() - (daysPassed * 24L * 60L * 60L * 1000L);
	                sqlDate.setTime(updatedTime);

	                updatedTime = sqlDate.getTime() + (daysProcessed * 24L * 60L * 60L * 1000L);
	                sqlDate.setTime(updatedTime);
	    			
	    			try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
	    				stmt2.setString(1, "arrived");
	    				stmt2.setDate(2, sqlDate);
	    				stmt2.setInt(3, key);
	    				
	    				int rows = stmt2.executeUpdate();
	    				
	    				if (rows < 1) throw new SQLException();
	    				
	    				ShortestPath.cityMap.remove(key);
	    				
	    			} catch (SQLException e) {
	    	        	e.printStackTrace();
	    	        }
	            }
	            
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
		}
	}

}
