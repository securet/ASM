package com.securet.ssm.services.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.StringPath;
import com.securet.ssm.utils.SecureTUtils;

public class DataTableCriteria implements Serializable{

	public static final String AND = "and";
	public static final String EQUALS = "=";
	public static final String DATA_QUERY = "dataQuery";
	public static final String COUNT_QUERY = "countQuery";
	public static final String AND_FILTER = "AND_FILTER";
	public static final String OR_FILTER = "OR_FILTER";
	public static final String SELECT = "select ";
	public static final String WHERE = " where ";
	public static final String ORDER_BY = " order by ";
	public static final String LIKE = " like ";
	public static final String COUNT = "count";
	public static final String PERCENTILE = "%";
	public static final String DOT = ".";
	public static final String SPACE = " ";
	public static final String QUOTE = "'";
	public static final String START_BRACKET = "(";
	public static final String END_BRACKET = ")";
	public static final String DB_OR = " or ";
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
		StringBuilder andQuery = new StringBuilder();
		StringBuilder orQuery = new StringBuilder();
		Iterator<Map<ColumnCriterias, String>> columnsIterator = dataTableCriteria.getColumns().iterator();
		//let us also check if there are any column criterias - field filters will  "and" filters,   any text search will be "or" regex
		String textToSearch = getDefaultTextToSearch(dataTableCriteria);
		boolean hasNext=columnsIterator.hasNext(); 
		Map<ColumnCriterias,String> columnCriteriasMapping = columnsIterator.next();
		while(hasNext){
			String columnName = columnCriteriasMapping.get(ColumnCriterias.data);
			String columnFilter = columnCriteriasMapping.get(ColumnCriterias.searchValue);
			
			boolean isSearchable = Boolean.valueOf(columnCriteriasMapping.get(ColumnCriterias.searchable));
			_logger.debug("found column name -> "+ColumnCriterias.data+ ",  value->" + columnName);
			if(isSearchable){
				if(textToSearch!=null && !textToSearch.isEmpty()){
					orQuery.append(aliasName).append(DOT).append(columnName).append(LIKE).append(QUOTE).append(PERCENTILE).append(textToSearch).append(PERCENTILE).append(QUOTE);
				}
				if(columnFilter!=null && !columnFilter.isEmpty()){
					andQuery.append(aliasName).append(DOT).append(columnName).append(EQUALS).append(QUOTE).append(columnFilter).append(QUOTE);
				}
			}

			hasNext=columnsIterator.hasNext();
			if(hasNext){
				columnCriteriasMapping = columnsIterator.next();
    			boolean isNextSearchable = Boolean.valueOf(columnCriteriasMapping.get(ColumnCriterias.searchable));
    			if(isNextSearchable){
    				if(textToSearch!=null && !textToSearch.isEmpty() && orQuery.length()>0){
        				orQuery.append(SPACE).append(operator).append(SPACE);
    				}
    				if(columnFilter!=null && !columnFilter.isEmpty()  && andQuery.length()>0){
        				andQuery.append(SPACE).append(AND).append(SPACE);
    				}
    			}
			}
		}
		if(orQuery.length()>0){
			orQuery.insert(0, START_BRACKET).append(END_BRACKET);
		}
		if(andQuery.length()>0){
			andQuery.insert(0, START_BRACKET).append(END_BRACKET);
		}
		
		if(orQuery.length()>0 || andQuery.length()>0){
			baseQuery.append(WHERE).append(SPACE).append(andQuery.toString());
			if(andQuery.length()>0 && orQuery.length()>0){
				baseQuery.append(SPACE).append(AND).append(SPACE);
			}
			baseQuery.append(orQuery.toString());
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
    	queriesToRun.put(AND_FILTER, andQuery.toString());
    	queriesToRun.put(OR_FILTER, andQuery.toString());
    	return queriesToRun;
    }

    public static String getDefaultTextToSearch(DataTableCriteria dataTableCriteria) {
		String textToSearch=null;
		if(dataTableCriteria.search!=null && dataTableCriteria.search.size()>0){
    		textToSearch = dataTableCriteria.search.get(SearchCriterias.value);
    	}
		return textToSearch;
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

	public static boolean hasOrderByField(DataTableCriteria columns,String columnName){
		if(columns.getOrder()!=null){
			for(Map<OrderCriterias, String> orderByField :columns.getOrder()){
				String indexStr = orderByField.get(OrderCriterias.column);
				try{
					int index = Integer.valueOf(indexStr);
					Map<ColumnCriterias,String> columnCriteria = columns.getColumns().get(index);
					if(columnCriteria!=null){
						String fieldName=columnCriteria.get(ColumnCriterias.data);
						try{
							Integer.parseInt(fieldName);
							//if fieldName is a number.. try looking up on name...
							fieldName=columnCriteria.get(ColumnCriterias.name);
						}catch(Exception e){
							//if it is not a number continue.. 
						}
						return columnName.equals(fieldName);
					}
				}catch(NumberFormatException e){
					_logger.error("could not format index: "+indexStr,e);
				}
			}
		}
		return false;
	}
	public void makeOrderByExpression(DataTableCriteria columns, JPASQLQuery listTicketsQuery, Map<String, Expression> fieldExprMapping) {
		for(Map<OrderCriterias, String> orderByField :columns.getOrder()){
			String indexStr = orderByField.get(OrderCriterias.column);
			try{
				int index = Integer.valueOf(indexStr);
				Map<ColumnCriterias,String> columnCriteria = columns.getColumns().get(index);
				if(columnCriteria!=null){
					String fieldName=columnCriteria.get(ColumnCriterias.data);
					try{
						Integer.parseInt(fieldName);
						//if fieldName is a number.. try looking up on name...
						fieldName=columnCriteria.get(ColumnCriterias.name);
					}catch(Exception e){
						//if it is not a number continue.. 
					}
					String dir =orderByField.get(OrderCriterias.dir);
					Order order = Order.ASC;
					if(dir.equals("desc")){
						order = Order.DESC;
					}
					Expression fieldNameExpr = fieldExprMapping.get(fieldName);
					if(fieldNameExpr==null){
						fieldNameExpr = new StringPath(fieldName);
					}
					listTicketsQuery.orderBy(new OrderSpecifier(order, fieldNameExpr));
					_logger.debug("Orderby fieldName:"+fieldName);
				}else{
					_logger.warn("No field on index:"+index + " for columns " + columns.getColumns());
				}
			}catch(NumberFormatException e){
				_logger.error("could not format index: "+indexStr,e);
			}
		}
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
		columns.put(ColumnCriterias.searchable, "true");
		columnDefs.add(columns);

		columns = makeColumns("status");
		columns.put(ColumnCriterias.searchable, "true");
		//columns.put(ColumnCriterias.searchValue, "1234");
		columnDefs.add(columns);

		columns = makeColumns("issue");
		columns.put(ColumnCriterias.searchable, "true");
		//columns.put(ColumnCriterias.searchValue, "8888");
		columnDefs.add(columns);
		dataTableCriteria.setColumns(columnDefs);

		Map<SearchCriterias, String> searchCriteria = new HashMap<DataTableCriteria.SearchCriterias, String>();
		searchCriteria.put(SearchCriterias.value, "secure");
		dataTableCriteria.setSearch(searchCriteria);
		
		List<Map<OrderCriterias, String>>  orderDefs = new ArrayList<Map<OrderCriterias,String>>();
		
		Map<OrderCriterias, String> orderBy = makeOrderBy("2", "desc");

		orderDefs.add(orderBy);
		
		orderBy = makeOrderBy("0", "asc");

		orderDefs.add(orderBy);
		dataTableCriteria.setOrder(orderDefs);
		
		System.out.println(makeSimpleQueriesFromCriteria("Organization", dataTableCriteria,DB_OR));
		System.out.println(hasOrderByField(dataTableCriteria, "name"));
		
	}
}
