package br.com.ufrrj.base;
/**
 * 
 * @author john
 * objeto retornado pelas classes que implementam connector
 * @param <E>
 */
public interface Data<E> {

	public E getData();

	public void setData(E data);

}
