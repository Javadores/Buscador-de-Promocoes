package br.com.ufrrj.principal;

import org.json.JSONException;

import br.com.ufrrj.base.Connector;
import br.com.ufrrj.base.Data;
import br.com.ufrrj.conexao.twitter.Twitter;
import br.com.ufrrj.conexao.twitter.util.Twitt;
import br.com.ufrrj.conexao.twitter.util.Twitts;
import br.com.ufrrj.graficos.PainelPrincipal;

public class Principal {
	
	
	public static void main(String[] args) throws JSONException {
		
		
		//PainelPrincipal painel = new PainelPrincipal();
		
		
		Connector connect = new Twitter();
        Data data =  connect.getUserPosts("rafinhabastos");
       
       Twitts array = (Twitts)data;
        int i=0;
       for (Twitt t : array.toTwittArray()) {
    	   
    	   System.out.println(t.getPost());
    	   System.out.println(t.getId()+"\n");
    	   
    	   i++;
		
	}
       System.out.println(i);
       //connect.close();
        
       
        
     
        
       // dao.setTwitt(twitt);
        
        
       // connect.close();
        
		
	}

}
