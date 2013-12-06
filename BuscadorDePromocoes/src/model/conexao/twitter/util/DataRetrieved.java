package model.conexao.twitter.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

import model.base.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * classe responsavel por armazenar o retorno do twitter
 * 
 * atualizado pela ultima vez em 25/11/13
 */
public class DataRetrieved implements Data<ArrayList<Tweet>>{
       
	ArrayList array;
	
	@Override
	public void setData(ArrayList<Tweet> data) {
		
		array = data;
		
	}

	@Override
	public ArrayList<Tweet> getData() {
		
		return array;
	}
	
	

}
