package com.securet.ssm.persistence.views.aggregates;

public class SecureTAggregate implements Comparable<SecureTAggregate>{

	private String aggFieldKey;
	
	private String aggFieldValue;
	
	private long aggregateValue;

	
	
	public SecureTAggregate(String aggFieldKey, String aggFieldValue, long aggregateValue) {
		super();
		this.aggFieldKey = aggFieldKey;
		this.aggFieldValue = aggFieldValue;
		this.aggregateValue = aggregateValue;
	}

	public String getAggFieldKey() {
		return aggFieldKey;
	}

	public void setAggFieldKey(String aggFieldKey) {
		this.aggFieldKey = aggFieldKey;
	}

	public String getAggFieldValue() {
		return aggFieldValue;
	}

	public void setAggFieldValue(String aggFieldValue) {
		this.aggFieldValue = aggFieldValue;
	}

	public long getAggregateValue() {
		return aggregateValue;
	}

	public void setAggregateValue(long aggregateValue) {
		this.aggregateValue = aggregateValue;
	}
	
	@Override
	public int compareTo(SecureTAggregate aggregate) {
		return (int)(this.aggregateValue - aggregate.aggregateValue);
	}
}
