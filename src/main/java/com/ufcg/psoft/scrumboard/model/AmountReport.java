package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AmountReport implements Report {
	private Map<String, Map<String, Integer>> reports;
	
	public AmountReport() {
		this.reports = new HashMap<String, Map<String, Integer>>();
	}
	
	@SuppressWarnings("unchecked")
	public void addReport(String name, Map<String, ? extends Object> reportValue) {
		this.reports.put(name, (Map<String, Integer>) reportValue);
	}
	
	public Map<String, ? extends Object> getReports() {
		return this.reports;
	}
	
	protected Collection<Map<String, Integer>> reportValues() {
		return this.reports.values();
	}
	
}
