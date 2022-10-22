package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.model.Desenvolvedor;
import com.ufcg.psoft.scrumboard.model.Estagiario;
import com.ufcg.psoft.scrumboard.model.PapelAbstract;
import com.ufcg.psoft.scrumboard.model.Pesquisador;
import com.ufcg.psoft.scrumboard.model.ProductOwner;
import com.ufcg.psoft.scrumboard.model.ScrumMaster;
import com.ufcg.psoft.scrumboard.model.User;

public enum TipoPapel {

	SCRUM_MASTER ("Scrum Master") {
		@Override
		PapelAbstract instanciaPapel(User user) {
			return new ScrumMaster(user);
		}
	},

	PRODUCT_OWNER ("Product Owner") {
		@Override
		PapelAbstract instanciaPapel(User user) {
			return new ProductOwner(user);
		}
	},

	PESQUISADOR ("Pesquisador") {
		@Override
		PapelAbstract instanciaPapel(User user) {
			return new Pesquisador(user);
		}
	},

	DESENVOLVEDOR ("Desenvolvedor") {
		@Override
		PapelAbstract instanciaPapel(User user) {
			return new Desenvolvedor(user);
		}
	},

	ESTAGIARIO ("Estagiário") {
		@Override
		PapelAbstract instanciaPapel(User user) {
			return new Estagiario(user);
		}
	};

	private final String nome;

	TipoPapel(String nome) {
		this.nome = nome;
	}

	public String nome() {
		return this.nome;
	}

	abstract PapelAbstract instanciaPapel(User user);

}
