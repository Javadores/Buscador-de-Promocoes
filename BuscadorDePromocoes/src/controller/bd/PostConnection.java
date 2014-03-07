package controller.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PostConnection {
	 
	    private static Connection conn = null;
	  
	    /**
	     * metodo responsavel por criar a conexão jdbc com o derby
	     */
	    public static Connection createConnection(String url)
	    {
	        try
	        {
	            Class.forName("org.postgresql.Driver").newInstance();
	            //Get a connection
	            conn = DriverManager.getConnection(url); 
	        }
	        catch (Exception except)
	        {
	            except.printStackTrace();
	        }
	        
	        return conn;
	    }
	    
	    
	    
	
	

}
