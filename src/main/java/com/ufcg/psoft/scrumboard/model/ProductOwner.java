package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProductOwner extends PapelAbstract {

	public ProductOwner(User user) {
		super(user);
	}

	@Override
	public String getTipo() {
		return "Product Owner";
	}
	
	protected List<Report> createReport(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState, Map<String, PapelAbstract> users) {
		return this.createReportForUser(userStories, username, usPerState);
	}

}
