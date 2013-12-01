package br.com.ufrrj.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.ufrrj.conexao.twitter.util.Twitt;

/**
 * 
 * @author john Classe que fará a persistencia dos dados
 */
public class TwittDao {

	private static Statement stmt = null;
	private static Connection conn;

	public TwittDao() {

		try {
			conn =DerbyConnection.createConnection(
					"jdbc:derby://localhost:1527/NetworksData;create=true;");
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void createTable() {
		try {
           stmt = conn.createStatement();
			stmt.execute("create table "
					+ "Twitt(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ " twitt_id varchar(50),twitt varchar(600),username varchar(50),CONSTRAINT primaryKey PRIMARY KEY (id))");

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void insertTwitt(Twitt twitt) {
		try {

			String sql = "insert into Twitt(twitt_id,twitt,username)  values(?,?,?)";

			PreparedStatement insert = conn.prepareStatement(sql);
			insert.setString(1, twitt.getId());
			insert.setString(2, twitt.getPost());
			insert.setString(3, twitt.getUsuario());

			insert.execute();

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}

	}

	public void selectTwitts() {
		try {
			
			stmt =conn.createStatement();

			ResultSet results = stmt.executeQuery("select * from " + " Twitt");

			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {

				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out
					.println("\n-------------------------------------------------");

			while (results.next()) {
				int id = results.getInt(1);
				String twittid = results.getString(2);
				String twitt = results.getString(3);
				String user = results.getString(4);
				System.out.println(id + "\t\t" + twittid + "\t\t" + twitt+"\t\t"+user);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private static void shutdown() {
		try {
			if (stmt != null) {
				stmt.close();
			}

		} catch (SQLException sqlExcept) {

		}

	}

}
