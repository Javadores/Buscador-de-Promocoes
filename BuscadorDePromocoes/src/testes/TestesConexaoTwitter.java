package testes;

import model.conexao.twitter.TwitterConexaoRest;
import junit.framework.TestCase;

public class TestesConexaoTwitter extends TestCase{
	
	TwitterConexaoRest conexao = new TwitterConexaoRest();
	
	public void testConexaoRest(){
		
		assertNotNull(conexao.getUserPosts("JohnCcomp"));
		assertNotNull(conexao.getUserPosts(""));
		assertNotNull(conexao.GetConsumer());
		assertNotNull(conexao.GetFollowers("JohnCcomp"));
		assertNotNull(conexao.GetSearchResults("#promocao"));
		
	}
	
	
	
	
	

}
