package br.com.ufrrj.conexao.twitter.util;

import br.com.ufrrj.base.Data;
/**
 * 
 * @author john
 *classe que tera as informaçoes do perfil. obs:por enquanto so precisamos do screen_name do usuario
 */
public class Profile implements Data<String> {
	
	private String user;

	@Override
	public String getData() {
		
		return user;
	}

	@Override
	public void setData(String data) {
		
		user = data;
		
		
	}

}
