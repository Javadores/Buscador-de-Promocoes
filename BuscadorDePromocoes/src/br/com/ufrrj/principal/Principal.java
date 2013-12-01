package br.com.ufrrj.principal;

import org.json.JSONException;

import br.com.ufrrj.base.Connector;
import br.com.ufrrj.base.Data;
import br.com.ufrrj.bd.TwittDao;
import br.com.ufrrj.conexao.twitter.TwitterConexao;
import br.com.ufrrj.conexao.twitter.util.Twitt;
import br.com.ufrrj.conexao.twitter.util.Twitts;
import br.com.ufrrj.graficos.PainelPrincipal;

public class Principal {

	public static void main(String[] args) throws JSONException {

		// PainelPrincipal painel = new PainelPrincipal();

		TwittDao dao = new TwittDao();
		Connector connect = new TwitterConexao();

		String[] nomes = { "JohnCcomp","pontofrio", "hotel_urbano", "Groupon_BR" };
        
		//dao.createTable();
		
		for (int i = 0; i < nomes.length; i++) {

			Data data = connect.getUserPosts(nomes[i]);
            Data data2 = connect.getProfile(nomes[i]);
            
			Twitts array = (Twitts) data;
			
			
			 for (Twitt t : array.toTwittArray()) {

				t.setUsuario((String)data2.getData());
				dao.insertTwitt(t);

			}

		}

		dao.selectTwitts();

	}

	}

