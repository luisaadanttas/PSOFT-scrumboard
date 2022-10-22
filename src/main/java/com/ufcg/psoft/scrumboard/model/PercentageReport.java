package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PercentageReport implements Report {
	private Map<String, Map<String, Double>> reports;
	
	public PercentageReport() {
		this.reports = new HashMap<String, Map<String, Double>>();
	}
	
	@SuppressWarnings("unchecked")
	public void addReport(String name, Map<String, ? extends Object> reportValue) {
		this.reports.put(name, (Map<String, Double>) reportValue);
	}
	
	public Map<String, ? extends Object> getReports() {
		return this.reports;
	}
	
	protected Collection<Map<String, Double>> reportValues() {
		return this.reports.values();
	}
	
}
