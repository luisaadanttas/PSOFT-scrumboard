package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Desenvolvedor extends PapelAbstract {

	public Desenvolvedor(User user) {
		super(user);
	}

	@Override
	public String getTipo() {
		return "Desenvolvedor";
	}
	
	protected List<Report> createReport(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState, Map<String, PapelAbstract> users) {
		return this.createReportForUser(userStories, username, usPerState);
	}

}
