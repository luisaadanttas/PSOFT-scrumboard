package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Projeto {

	private final int id;

	private String nome;

	private String descricao;

	private String instituicaoParceira;

	private ScrumMaster scrumMaster;
	
	private int lastUSId;

	private Map<String, PapelAbstract> membros;
	
	private Map<Integer, UserStory> userStories;

	public Projeto(int id, ScrumMaster scrumMaster, String nome,
			String descricao, String instituicaoParceira) {
		this.id = id;
		this.scrumMaster = scrumMaster;
		this.nome = nome;
		this.descricao = descricao;
		this.instituicaoParceira = instituicaoParceira;
		this.membros = new HashMap<>();
		this.addMembro(scrumMaster);
		this.lastUSId = 0;
		this.userStories = new HashMap<>();
	}

	public int getId() {
		return id;
	}
	
	public int getNewUSId() {
		this.lastUSId += 1;
		return this.lastUSId;
	}

	public ScrumMaster getScrumMaster() {
		return this.scrumMaster;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getInstituicaoParceira() {
		return instituicaoParceira;
	}

	public void setInstituicaoParceira(String instituicaoParceira) {
		this.instituicaoParceira = instituicaoParceira;
	}

	public void addMembro(PapelAbstract membro) {
		this.membros.put(membro.getUsername(), membro);

		if (membro.getTipo().equals("Product Owner")) {
			for (UserStory us : getUserStories())
				us.addListenerUsChangedToDone(membro);
		}
	}

	public boolean temMembro(String username) {
		return this.membros.containsKey(username);
	}

	public PapelAbstract getMembro(String username) {
		return this.membros.get(username);
	}
	
	public Map<String, PapelAbstract> getMembroEPapel() {
		return this.membros;
	}

	public Collection<PapelAbstract> getMembros() {
		return membros.values();
	}

	public void removeMembro(String username) {
		PapelAbstract membroRemovido = this.membros.remove(username);
		for (UserStory us : this.getUserStories()) {
			us.removeAssignee(username);
			us.removeListener(membroRemovido);
		}
	}
	
	public void addUserStory(UserStory userStory) {
		this.userStories.put(userStory.getId(), userStory);

		userStory.addListenerUsChangedDescription(scrumMaster);
		userStory.addListenerUsChangedState(scrumMaster);
		userStory.addListenerTaskFinished(scrumMaster);

		for (PapelAbstract membro : getMembros()) {
			if (membro.getTipo().equals("Product Owner"))
				userStory.addListenerUsChangedToDone(membro);
		}
	}
	
	public void removeUserStory(int userStoryId) {
		this.userStories.remove(userStoryId);
	}

	public UserStory getUserStory(int userStoryId) {
		return this.userStories.get(userStoryId);
	}
	
	public Collection<UserStory> getUserStories() {
		return this.userStories.values();
	}

	public int getQtdUs() {
		return this.lastUSId;
	}
}
