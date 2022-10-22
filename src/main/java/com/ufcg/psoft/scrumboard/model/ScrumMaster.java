package com.ufcg.psoft.scrumboard.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScrumMaster extends PapelAbstract {

	public ScrumMaster(User user) {
		super(user);
	}

	@Override
	public String getTipo() {
		return "Scrum Master";
	}
	
	protected List<Report> createReport(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState, Map<String, PapelAbstract> users) {
		List<Report> reports = new ArrayList<Report>();
		
		for (String key: users.keySet()) {
			ScrumMasterReport userReport = new ScrumMasterReport();
			
			List<Report> userReports = users.get(key).createReportForUser(userStories, key, usPerState);
			
			userReport.addReport("Distribuição para " + key, userReports.get(0).getReports());
			userReport.addReport("Porcentagem de USs para " + key, userReports.get(1).getReports());
			
			reports.add(userReport);
		}
		
		AmountReport usPerStateReport = new AmountReport();
		usPerStateReport.addReport("Distribuição das USs por estado", usPerState);
		PercentageReport usPercentagePerState = new PercentageReport();
		usPercentagePerState.addReport("Porcentagem das USs por estado", this.calculatePercentage(usPerState, userStories.size()));
		
		reports.add(usPercentagePerState);
		reports.add(usPerStateReport);
		
		return reports;
	}

}
