package br.com.ufrrj.conexao.twitter.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.ufrrj.base.Data;

public class SearchResults implements Data<JSONArray>{
	
    private JSONArray dados;

	@Override
	public JSONArray getData() {
		// TODO Auto-generated method stub
		return dados;
	}

	@Override
	public void setData(JSONArray data) {
		
		this.dados = data;
		
	}
	
	public ArrayList<Twitt> toTwittsArray(){
		
		ArrayList<Twitt> twitts = new ArrayList<Twitt>();
		try{
		for(int i =0 ; i< dados.length();i++){
			
			Twitt twitt = new Twitt();
		
				
				JSONObject result = dados.getJSONObject(i);
			    twitt.setId(result.getString("id_str"));
			    twitt.setPost(result.getString("text"));
				
				JSONObject aux = new JSONObject(result.getString("user"));
				
				twitt.setUsuario(aux.getString("screen_name"));
				
				twitts.add(twitt);
				
		
			
		}
		}catch(Exception e){
			
		}
		return twitts;
		
	}

}
