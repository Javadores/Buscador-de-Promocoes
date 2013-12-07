package model.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.base.Data;
import model.conexao.twitter.util.Profile;
import model.conexao.twitter.util.Tweet;

/**
 * 
 * @author john Classe que fará a persistencia dos dados
 */
public class TwittDao implements Dao<Tweet, ArrayList<Tweet>> {

	private static Statement stmt = null;
	private static Connection conn;

	public TwittDao() {

		try {
			conn = DerbyConnection
					.createConnection("jdbc:derby://localhost:1527/NetworksData;create=true;");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void criaBanco() {

		try {
			stmt = conn.createStatement();
			stmt.execute("create table "
					+ "Twitt(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ " twitt_id varchar(50),twitt varchar(600),username varchar(50),CONSTRAINT primaryKey PRIMARY KEY (id))");

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void insert(Tweet tweet) {

		try {

			String sql = "insert into Twitt(twitt_id,twitt,username)  values(?,?,?)";

			PreparedStatement insert = conn.prepareStatement(sql);
			insert.setString(1, tweet.getId());
			insert.setString(2, tweet.getPost());
			insert.setString(3, tweet.getUsuario());

			insert.execute();

			insert.close();

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}

	}

	@Override
	public ArrayList<Tweet> select(String query) {
		
		ArrayList<Tweet> array = new ArrayList<Tweet>();

		try {

			stmt = conn.createStatement();

			ResultSet results = stmt.executeQuery("select * from " + " Twitt");

			ResultSetMetaData rsmd = results.getMetaData();
		    

			while (results.next()) {
				
				Tweet tweet = new Tweet();
				
				
				tweet.setId( results.getString(2));
			    tweet.setPost(results.getString(3));
				tweet.setUsuario( results.getString(4));
				
				array.add(tweet);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}

		return array;
	}

}
