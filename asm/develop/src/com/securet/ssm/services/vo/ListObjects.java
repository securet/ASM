package com.securet.ssm.services.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.securet.ssm.persistence.objects.SecureTObject.SimpleObject;

public class ListObjects implements Serializable {

	private int draw;//field list size
	private int recordsTotal;//total no of records
	private int recordsFiltered;//records to display
	private List<String> columnsNames;

	//@JsonView(SimpleObject.class)
	private Object data;
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public List<String> getColumnsNames() {
		return columnsNames;
	}
	public void setColumnsNames(List<String> columnsNames) {
		this.columnsNames = columnsNames;
	}

	@Override
	public String toString() {
		return "ListObjects [draw=" + draw + ", recordsTotal=" + recordsTotal + ", recordsFiltered=" + recordsFiltered + ", data=" + data + "]";
	}
	
	
}
