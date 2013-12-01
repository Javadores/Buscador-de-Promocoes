package br.com.ufrrj.conexao.twitter.util;

public class Twitt {
	
	
	private String id;
	private String usuario;
	private String post;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	
	@Override
	public String toString() {
		
		return "https://twitter.com/"+this.usuario+"/status/"+this.id;
	}

}
