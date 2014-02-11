package controller.bd;

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
public class TweetDao implements Dao<Tweet, ArrayList<Tweet>> {

	private static Statement stmt = null;
	private static Connection conn;

	public TweetDao() {

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
					+ "Tweet(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ " tweet_id varchar(50),tweet varchar(600),username varchar(50),horario time,data date ,CONSTRAINT primaryKey PRIMARY KEY (id))");
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void insert(Tweet tweet) {

		try {

			String sql = "insert into Tweet(tweet_id,tweet,username,horario,data)  values(?,?,?,?,?)";

			PreparedStatement insert = conn.prepareStatement(sql);
			insert.setString(1, tweet.getId());
			insert.setString(2, tweet.getPost());
			insert.setString(3, tweet.getUsuario());
			insert.setTime(4, tweet.getTime());
			insert.setDate(5, tweet.getData());
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

			ResultSet results = stmt.executeQuery("select * from " + " Tweet");
		

			
			
		    int i =0;

			while (results.next()) {
				
				Tweet tweet = new Tweet();
				
				
				tweet.setId(results.getString(2));
			    tweet.setPost(results.getString(3));
				tweet.setUsuario(results.getString(4));
				tweet.setTime(results.getTime(5));
				tweet.setData(results.getDate(6));
				
				array.add(tweet);
				i++;
			}
			
			System.out.println("tamanho "+i);
			
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}

		return array;
	}
	}



