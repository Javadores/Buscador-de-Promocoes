package model.base;


	/**
	 * 
	 * @param url
	 * interface utilizada para a implementação do strategy
	 * @return
	 */
public interface Connector {
  
	public Data getUserPosts(String url);
	public Data getProfile(String url);
	public Data performASearch(String query);
	
	

}
