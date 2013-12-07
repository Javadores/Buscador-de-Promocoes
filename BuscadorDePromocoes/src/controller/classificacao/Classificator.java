package controller.classificacao;

import model.promocao.Promocao;
import model.promocao.PromocaoViagem;

public interface Classificator<E> {
	
	public Promocao classify(E dado);

}
