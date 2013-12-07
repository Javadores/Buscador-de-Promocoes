package controller.classificacao;

import model.conexao.twitter.util.Tweet;
import model.promocao.Promocao;
import model.promocao.PromocaoProduto;
import model.promocao.PromocaoServico;
import model.promocao.PromocaoViagem;


public class Classificador implements Classificator<Tweet> {

	@Override
	public Promocao classify(Tweet dado) {

		if (dado.getUsuario().equals("Groupon_BR")) {

			PromocaoServico prom = new PromocaoServico();

			return prom;
		} else if (dado.getUsuario().equals("pontofrio")) {

			PromocaoProduto prom = new PromocaoProduto();

			return prom;

		} else if (dado.getUsuario().equals("hotel_urbano")) {

			PromocaoViagem prom = new PromocaoViagem();

			return prom;

		}

		return null;
	}

}
