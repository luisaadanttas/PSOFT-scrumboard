package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Pesquisador extends PapelAbstract {

	public Pesquisador(User user) {
		super(user);
	}

	@Override
	public String getTipo() {
		return "Pesquisador";
	}
	
	protected List<Report> createReport(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState, Map<String, PapelAbstract> users) {
		return this.createReportForUser(userStories, username, usPerState);
	}

}
