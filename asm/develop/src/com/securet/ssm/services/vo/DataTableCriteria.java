package com.securet.ssm.services.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.securet.ssm.utils.SecureTUtils;

public class DataTableCriteria implements Serializable{

	public static final String DATA_QUERY = "dataQuery";
	public static final String COUNT_QUERY = "countQuery";
	private static final String SELECT = "select ";
	private static final String WHERE = " where ";
	private static final String ORDER_BY = " order by ";
	private static final String LIKE = " like ";
	private static final String COUNT = "count";
	private static final String PERCENTILE = "%";
	private static final String DOT = ".";
	private static final String SPACE = " ";
	private static final String QUOTE = "'";
	private static final String START_BRACKET = "(";
	private static final String END_BRACKET = ")";
	private static final String DB_OR = " or ";
	private static final Logger _logger = LoggerFactory.getLogger(DataTableCriteria.class);
	/*
	 * columns[0][data]	name
		columns[0][name]	
		columns[0][orderable]	true
		columns[0][search][regex]	false
		columns[0][search][value]	
		columns[0][searchable]	true

	 */
    private int draw;
    private int start;
    private int length;

    private Map<SearchCriterias, String> search;

    private List<Map<ColumnCriterias, String>> columns;

    private List<Map<OrderCriterias, String>> order;

    public enum SearchCriterias {
        value,
        regex
    }
    public enum OrderCriterias {
        column,
        dir
    }
    public enum ColumnCriterias {
        data,
        name,
        searchable,
        orderable,
        searchValue,
        searchRegex
    }
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Map<SearchCriterias, String> getSearch() {
		return search;
	}
	public void setSearch(Map<SearchCriterias, String> search) {
		this.search = search;
	}
	public List<Map<ColumnCriterias, String>> getColumns() {
		return columns;
	}
	public void setColumns(List<Map<ColumnCriterias, String>> columns) {
		this.columns = columns;
	}
	public List<Map<OrderCriterias, String>> getOrder() {
		return order;
	}
	public void setOrder(List<Map<OrderCriterias, String>> order) {
		this.order = order;
	}
    
    public static Map<String,String> makeSimpleQueriesFromCriteria(String entityName,DataTableCriteria dataTableCriteria,String operator){
    	StringBuilder baseQuery = new StringBuilder();
    	StringBuilder countQuery = new StringBuilder();
    	StringBuilder dataQuery = new StringBuilder();
    	String aliasName = SecureTUtils.decapitalize(entityName);
    	baseQuery.append(" from ").append(entityName).append(SPACE).append(aliasName).append(SPACE);
    	if(dataTableCriteria.search!=null && dataTableCriteria.search.size()>0){
    		String textToSearch = dataTableCriteria.search.get(SearchCriterias.value);
    		if(textToSearch!=null & !textToSearch.isEmpty() & !dataTableCriteria.getColumns().isEmpty()){
    			baseQuery.append(WHERE);
    			Iterator<Map<ColumnCriterias, String>> dataTableIterator = dataTableCriteria.getColumns().iterator();
	    		while(dataTableIterator.hasNext()){
	    			Map<ColumnCriterias,String> columnCriteriasMapping = dataTableIterator.next();
	    			String columnName = columnCriteriasMapping.get(ColumnCriterias.data);
    				_logger.debug("found column name -> "+ColumnCriterias.data+ ",  value->" + columnName);
    				baseQuery.append(aliasName).append(DOT).append(columnName).append(LIKE).append(QUOTE).append(PERCENTILE).append(textToSearch).append(PERCENTILE).append(QUOTE);
    				if(dataTableIterator.hasNext()){
    					baseQuery.append(SPACE).append(operator).append(SPACE);
    				}
	    		}
    		}
    	}
    	if(dataTableCriteria.order!=null && dataTableCriteria.order.size()>0){
			baseQuery.append(ORDER_BY);//incase column arre not found.. query will fail.. 
    		for(Map<OrderCriterias, String> orderByField :dataTableCriteria.order){
    			String indexStr = orderByField.get(OrderCriterias.column);
    			try{
	    			int index = Integer.valueOf(indexStr);
	    			Map<ColumnCriterias,String> columnCriteria = dataTableCriteria.columns.get(index);
	    			if(columnCriteria!=null){
		    			String fieldName=columnCriteria.get(ColumnCriterias.data);
		    			baseQuery.append(SPACE).append(fieldName).append(SPACE).append(orderByField.get(OrderCriterias.dir));
	    			}else{
	    				_logger.warn("No field on index:"+index + " for columns " + dataTableCriteria.columns);
	    			}
    			}catch(NumberFormatException e){
    				_logger.error("could not format index: "+indexStr,e);
    			}
    		}
    	}
    	if(_logger.isDebugEnabled())_logger.debug("query to run : "+baseQuery.toString());
    	Map<String,String> queriesToRun = new HashMap<String, String>();
    	dataQuery.append(SELECT).append(SPACE).append(aliasName).append(SPACE).append(baseQuery.toString());
    	String anyColumn = dataTableCriteria.columns.get(0).get(ColumnCriterias.data); 
    	countQuery.append(SELECT).append(SPACE).append(COUNT).append(START_BRACKET).append(anyColumn).append(END_BRACKET).append(SPACE).append(baseQuery.toString());
    	queriesToRun.put(DATA_QUERY, dataQuery.toString());
    	queriesToRun.put(COUNT_QUERY, countQuery.toString());
    	return queriesToRun;
    }
	
	private static Map<ColumnCriterias, String> makeColumns(String field) {
		Map<ColumnCriterias,String> columns = new HashMap<DataTableCriteria.ColumnCriterias, String>();
		columns.put(ColumnCriterias.data, field);
		columns.put(ColumnCriterias.name, "test");
		return columns;
	}

	private static Map<OrderCriterias, String> makeOrderBy(String field,String order) {
		Map<OrderCriterias,String> columns = new HashMap<OrderCriterias, String>();
		columns.put(OrderCriterias.column, field);
		columns.put(OrderCriterias.dir, order);
		return columns;
	}

	@Override
	public String toString() {
		return "DataTableCriteria [draw=" + draw + ", start=" + start + ", length=" + length + ", search=" + search + ", columns=" + columns + ", order=" + order + "]";
	}
    
    public static void main(String[] args) {
		DataTableCriteria dataTableCriteria = new DataTableCriteria();
		List<Map<ColumnCriterias, String>>  columnDefs = new ArrayList<Map<ColumnCriterias,String>>();
		Map<ColumnCriterias, String> columns = makeColumns("type");
		columnDefs.add(columns);
		columns = makeColumns("name");
		columnDefs.add(columns);
		dataTableCriteria.setColumns(columnDefs);

		Map<SearchCriterias, String> searchCriteria = new HashMap<DataTableCriteria.SearchCriterias, String>();
		searchCriteria.put(SearchCriterias.value, "secure");
		dataTableCriteria.setSearch(searchCriteria);
		
		List<Map<OrderCriterias, String>>  orderDefs = new ArrayList<Map<OrderCriterias,String>>();
		
		Map<OrderCriterias, String> orderBy = makeOrderBy("1", "desc");

		orderDefs.add(orderBy);
		
		orderBy = makeOrderBy("0", "asc");

		orderDefs.add(orderBy);
		dataTableCriteria.setOrder(orderDefs);
		
		System.out.println(makeSimpleQueriesFromCriteria("Organization", dataTableCriteria,DB_OR));
		
	}
}
