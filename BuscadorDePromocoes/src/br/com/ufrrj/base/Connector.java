package br.com.ufrrj.base;

public interface Connector {
	
	public void connect();
	public Twitt getTwitt(String url);
	public void close();
		
	

}
