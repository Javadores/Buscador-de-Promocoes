package br.com.ufrrj.base;

public interface Connector {

	public Data getUserPosts(String url);

	public void close();

}
