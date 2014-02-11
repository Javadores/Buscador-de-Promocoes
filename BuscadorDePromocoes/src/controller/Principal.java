package controller;


import java.util.ArrayList;

import model.base.Connector;
import model.conexao.twitter.TwitterConexaoRest;
import model.conexao.twitter.TwitterConexaoStreaming;
import model.conexao.twitter.util.Tweet;
import model.conexao.twitter.util.DataRetrieved;

import org.json.JSONException;

import controller.bd.Dao;
import controller.bd.TweetDao;
import view.PainelPrincipal;


public class Principal {

	public static void buscasNome() throws JSONException {

		Connector connect = (Connector) new TwitterConexaoRest();

		String[] nomes = { "JohnCcomp", "pontofrio", "hotel_urbano",
				"Groupon_BR" };

		//Dao dao = new TwittDao();

		// dao.criaBanco();

		for (int i = 0; i < nomes.length; i++) {

			DataRetrieved tweets = (DataRetrieved) connect.getUserPosts(nomes[i]);

			

			for (Tweet t : tweets.getData()) {

				System.out.println(t);
				//dao.insert(t);

			}

		}

		// dao.selectTwitts();

	}

	public static void buscasPorQuery() {

		Connector connect = (Connector) new TwitterConexaoRest();

		DataRetrieved resultados = (DataRetrieved) connect.performASearch("#lightAmaldiçoada");

		for (Tweet t : resultados.getData()) {

			System.out.println(t.toString());

		}

	}
	
	public static void streaming(){
		
		     Connector  conn = (Connector) new TwitterConexaoStreaming();
		    // nesta implementação ele já insere direto no banco de dados
		     conn.getUserPosts("https://stream.twitter.com/1.1/statuses/filter.json");
		     
		     Dao dao = new TweetDao();
		     dao.select("");

		
	}

	public static void main(String[] args) throws JSONException {

		 PainelPrincipal painel = new PainelPrincipal();
		
		//buscasPorQuery();
		//buscasNome();
		streaming();

	}
}
