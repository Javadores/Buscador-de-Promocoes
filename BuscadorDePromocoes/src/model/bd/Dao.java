package model.bd;

import model.base.Data;

public interface Dao<E,T>{
	
	public void criaBanco();
	public void insert(E data);
	public T select(String query);

}
