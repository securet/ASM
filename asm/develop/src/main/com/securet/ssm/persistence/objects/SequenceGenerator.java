package com.securet.ssm.persistence.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sequence_generator")
public class SequenceGenerator extends SecureTObject {

	@Id
	private String sequenceName;
	
	private long sequenceValue;

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public long getSequenceValue() {
		return sequenceValue;
	}

	public void setSequenceValue(long sequenceValue) {
		this.sequenceValue = sequenceValue;
	}
	
	
}
