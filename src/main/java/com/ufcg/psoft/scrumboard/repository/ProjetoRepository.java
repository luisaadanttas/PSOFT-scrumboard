package com.ufcg.psoft.scrumboard.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ufcg.psoft.scrumboard.model.Projeto;

@Repository
public class ProjetoRepository {

	private Map<Integer, Projeto> projetos = new HashMap<>();

	private int ultimoId = 0;

	public void add(Projeto projeto) {
		projetos.put(projeto.getId(), projeto);
	}

	public int geraId() {
		return this.ultimoId += 1;
	}

	public boolean existe(int id) {
		return projetos.containsKey(id);
	}

	public Projeto get(int id) {
		return projetos.get(id);
	}

	public Collection<Projeto> getAll() {
		return projetos.values();
	}

	public Projeto remove(int id) {
		return projetos.remove(id);
	}

}
