package br.com.ufrrj.base;


	/**
	 * 
	 * @param url
	 * interface utilizada para a implementação do strategy
	 * @return
	 */
public interface Connector {
  
	public Data getUserPosts(String url);
	public Data getProfile(String url);
	
	public void close();

}
