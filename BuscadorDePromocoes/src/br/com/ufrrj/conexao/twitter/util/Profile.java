package br.com.ufrrj.conexao.twitter.util;


import org.json.JSONException;
import org.json.JSONObject;

import br.com.ufrrj.base.Data;
/**
 * 
 * @author john
 *classe que tera as informaçoes do perfil. obs:por enquanto so precisamos do screen_name do usuario
 */
public class Profile implements Data<JSONObject> {
	
    private JSONObject dados;

	@Override
	public JSONObject getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(JSONObject data) {
		// TODO Auto-generated method stub
		
	}
	
	public String getUserNick(){
		
		String retorno =null;
		
		try {
		 retorno = dados.getString("screen_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
		
	}
	

	
}
