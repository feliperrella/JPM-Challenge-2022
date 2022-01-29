package com.feliperrella.jpmorgan.ChallengeJP;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;  
import java.sql.ResultSet; 

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


@RestController
public class EndpointController {


	static final String JDBC_DRIVER = "org.h2.Driver";   
   	static final String DB_URL = "jdbc:h2:~/test"; 
	static final String USER = "sa"; 
   	static final String PASS = "";  

	@GetMapping("/CreateNewFile")
	public String CreateNewFile() {
		
		List<CSVModel> csvModels = getCSVData();		
		return writeCSV(csvModels);

	}

	@GetMapping("/financialByTrader")
	public List<FinByTrader> financialByTrader() {

		List<FinByTrader> jsModels = getJSData();
		return jsModels;

	}



	List<CSVModel> getCSVData(){

		Connection conn = null; 
     	Statement stmt = null; 
		List<CSVModel> csvModels = new ArrayList();
      	try { 
			Class.forName(JDBC_DRIVER); 
			conn = DriverManager.getConnection(DB_URL,USER,PASS);  
			stmt = conn.createStatement(); 
			String sql = "select Trader.code, Trader.name, Orderr.Id, Orderr.Ticker, Sum(Trade.Quantity) as QUANTITY, (SUM(Trade.Price  * Trade.Quantity ) / SUM(Trade.Quantity)) as AVGPrice From Orderr inner join Trader on Trader.Code = Orderr.Trader inner join Trade on Trade.OrderId = Orderr.Id group by Orderr.Id;"; 
			ResultSet rs = stmt.executeQuery(sql); 

			while(rs.next()) { 
				CSVModel csv = new CSVModel(rs.getString("CODE"), rs.getString("NAME"), rs.getInt("ID"), rs.getString("TICKER"), rs.getFloat("QUANTITY"),rs.getFloat("AVGPRICE"));
				csvModels.add(csv);
			} 
			rs.close(); 
			} catch(SQLException se) { 
				
				se.printStackTrace(); 
			} catch(Exception e) { 
				
				e.printStackTrace(); 
			} finally { 
				
				try { 
					if(stmt!=null) stmt.close();  
				} catch(SQLException se2) { 
				} 
				try { 
					if(conn!=null) conn.close(); 
				} catch(SQLException se) { 
					se.printStackTrace(); 
				} 
			} 
			return csvModels;
	}

	String writeCSV(List<CSVModel> csvModels) {
		
		File dir = new File("temp/");
		dir.mkdirs();
        try (PrintWriter writer = new PrintWriter(dir + "/test.csv")) {
    
          StringBuilder sb = new StringBuilder();

          sb.append("TraderCode");
          sb.append(',');
          sb.append("TraderName");
          sb.append(',');
          sb.append("OrderID");
          sb.append(',');
          sb.append("Ticker");
          sb.append(',');
          sb.append("Qty");
          sb.append(',');
          sb.append("AVGPrice");
          sb.append('\n');
    
          for (int i = 0; i < csvModels.size(); i++){
              sb.append(csvModels.get(i).TraderCode);
              sb.append(',');
              sb.append(csvModels.get(i).TraderName);
              sb.append(',');
              sb.append(csvModels.get(i).OrderID);
              sb.append(',');
              sb.append(csvModels.get(i).Ticker);
              sb.append(',');
              sb.append(csvModels.get(i).Quantity);
              sb.append(',');
              sb.append(csvModels.get(i).Price);
              sb.append('\n');
          }
    
          writer.write(sb.toString());
		  
		  return dir.getAbsolutePath().toString() + "/test.csv";
    
        } catch (FileNotFoundException e) {
          System.out.println(e.getMessage());
		  return null;
        }


		
    
    }

	List<FinByTrader> getJSData(){
		Connection conn = null; 
     	Statement stmt = null; 
		List<FinByTrader> jsModels = new ArrayList();

      	try { 
         
			Class.forName(JDBC_DRIVER); 
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);  
			
			
			stmt = conn.createStatement(); 
			String sql = "select Trader.CODE , SUM(Trade.Price * Trade.Quantity) SM from Orderr inner join Trader on Orderr.Trader = Trader.Code inner join Trade on Trade.OrderId = Orderr.Id group by Code"; 
			ResultSet rs = stmt.executeQuery(sql); 
         
			

			while(rs.next()) { 
				FinByTrader js = new FinByTrader(rs.getString("CODE"), rs.getFloat("SM"));
				jsModels.add(js);
			} 
			rs.close(); 
			} catch(SQLException se) { 
				se.printStackTrace(); 
			} catch(Exception e) { 
				e.printStackTrace(); 
			} finally { 
				try { 
					if(stmt!=null) stmt.close();  
				} catch(SQLException se2) { 
				} 
				try { 
					if(conn!=null) conn.close(); 
				} catch(SQLException se) { 
					se.printStackTrace(); 
				} 
			} 
			return jsModels;
	}

}
