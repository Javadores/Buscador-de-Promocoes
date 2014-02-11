package model.conexao.twitter.util;

import java.sql.Date;
import java.sql.Time;

public class Tweet {
	
	
	private String id;
	private String usuario;
	private String post;
	private Time time;
    private Date data;
	


	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	
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
