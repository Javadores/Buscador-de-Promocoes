package br.com.ufrrj.conexao.twitter.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.ufrrj.base.Data;

/**
 * 
 * classe responsavel por armazenar o retorno do twitter
 * 
 * atualizado pela ultima vez em 25/11/13
 */
public class Twitts implements Data<JSONArray>{
       
	JSONArray array;
	@Override
	public JSONArray getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(JSONArray array) {
		
	   this.array = array;
	   
	}
	
	public ArrayList<Twitt> toTwittArray() throws JSONException{
		
		ArrayList<Twitt> arrayTwitts = new ArrayList<Twitt>();
		
		for(int i =0 ; i < array.length() ;i++){
			
			JSONObject result = array.getJSONObject(i);
			Twitt twitt = new Twitt();
			twitt.setId(result.getString("id_str"));
			twitt.setPost(result.getString("text"));

			arrayTwitts.add(twitt);
			
		}
		
		return arrayTwitts;
		
	}

}
