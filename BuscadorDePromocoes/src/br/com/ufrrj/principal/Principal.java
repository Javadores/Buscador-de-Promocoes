package br.com.ufrrj.principal;

import org.json.JSONException;
import br.com.ufrrj.base.Connector;
import br.com.ufrrj.bd.TwittDao;
import br.com.ufrrj.conexao.twitter.TwitterConexao;
import br.com.ufrrj.conexao.twitter.util.SearchResults;
import br.com.ufrrj.conexao.twitter.util.Twitt;
import br.com.ufrrj.conexao.twitter.util.Twitts;
import br.com.ufrrj.graficos.PainelPrincipal;

public class Principal {

	public static void buscasNome() throws JSONException {

		Connector connect = new TwitterConexao();

		String[] nomes = { "JohnCcomp", "pontofrio", "hotel_urbano",
				"Groupon_BR" };

		//TwittDao dao = new TwittDao();

		// dao.createTable();

		for (int i = 0; i < nomes.length; i++) {

			Twitts twitts = (Twitts) connect.getUserPosts(nomes[i]);

			Twitts array = (Twitts) twitts;

			for (Twitt t : array.toTwittArray()) {

				System.out.println(t);
				//dao.insertTwitt(t);

			}

		}

		// dao.selectTwitts();

	}

	public static void buscasPorQuery() {

		Connector connect = new TwitterConexao();

		SearchResults resultados = (SearchResults) connect.performASearch("#Natal");

		for (Twitt t : resultados.toTwittsArray()) {

			System.out.println(t.toString());

		}

	}

	public static void main(String[] args) throws JSONException {

		// PainelPrincipal painel = new PainelPrincipal();
		
		//buscasPorQuery();
		buscasNome();

	}
}
