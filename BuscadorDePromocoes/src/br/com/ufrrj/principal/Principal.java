package br.com.ufrrj.principal;

import br.com.rural.graficos.PainelPrincipal;
import br.com.ufrrj.base.Connector;
import br.com.ufrrj.base.Twitt;
import br.com.ufrrj.bd.JdbcDao;
import br.com.ufrrj.conexao.twitter.Twitter;

public class Principal {
	
	
	public static void main(String[] args) {
		
		
		PainelPrincipal painel = new PainelPrincipal();
		
		
		Connector connect = new Twitter();
        connect.connect();
        
        Twitt twitt = connect.getTwitt("@pontofrio");
        
        JdbcDao dao = new JdbcDao();
        
        dao.setTwitt(twitt);
        
        
        connect.close();
        
		
	}

}
