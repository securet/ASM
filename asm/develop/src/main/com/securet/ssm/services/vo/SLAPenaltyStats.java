package com.securet.ssm.services.vo;

public class SLAPenaltyStats {

	private String vendorOrganization;
	private long noOfSites;
	private int noOfIssues;
	private double totalPenalty;

	public String getVendorOrganization() {
		return vendorOrganization;
	}
	public void setVendorOrganization(String vendorOrganization) {
		this.vendorOrganization = vendorOrganization;
	}
	public long getNoOfSites() {
		return noOfSites;
	}
	public void setNoOfSites(long noOfSites) {
		this.noOfSites = noOfSites;
	}
	public int getNoOfIssues() {
		return noOfIssues;
	}
	public void setNoOfIssues(int noOfIssues) {
		this.noOfIssues = noOfIssues;
	}
	public double getTotalPenalty() {
		return totalPenalty;
	}
	public void setTotalPenalty(double totalPenalty) {
		this.totalPenalty = totalPenalty;
	}
	
}
