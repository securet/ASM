package com.securet.ssm.services;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.securet.ssm.persistence.objects.SecureTObject;
import com.securet.ssm.persistence.objects.User;
import com.securet.ssm.services.vo.DataTableCriteria;
import com.securet.ssm.services.vo.ListObjects;

public class ActionHelpers {

	public static final Logger _logger = LoggerFactory.getLogger(ActionHelpers.class);
	
	private EntityManager entityManager;

	public  EntityManager getEntityManager() {
		return entityManager;
	}

	public  void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<SecureTObject> getDataForView(String namedQuery){
		List<SecureTObject> objects = entityManager.createNamedQuery(namedQuery).getResultList();
		return objects;
	}
	
	public static ListObjects loadSimpleObjects(EntityManager entityManager,DataTableCriteria columns, String entityName,String operator,HttpServletRequest request){
		Map<String,String> queriesToRun = DataTableCriteria.makeSimpleQueriesFromCriteria(entityName, columns, operator);
		String dataQueryStr = queriesToRun.get(DataTableCriteria.DATA_QUERY);
		String countQueryStr = queriesToRun.get(DataTableCriteria.COUNT_QUERY);
		Query query = entityManager.createQuery(dataQueryStr);
		Query countQuery = entityManager.createQuery(countQueryStr);
		Map<String,Query> jpaQueriesToRun = new HashMap<String, Query>();
		jpaQueriesToRun.put(DataTableCriteria.DATA_QUERY, query);
		jpaQueriesToRun.put(DataTableCriteria.COUNT_QUERY, countQuery);
		ListObjects listObjects = listSimpleObjectFromQuery(entityManager, columns, jpaQueriesToRun);
		return listObjects; 
	}

	public static ListObjects listSimpleObjectFromQuery(EntityManager entityManager, DataTableCriteria columns, Map<String,Query> queriesToRun) {
		Query query = queriesToRun.get(DataTableCriteria.DATA_QUERY);
		int viewSize = columns.getLength();
		int startIndex = columns.getStart();
		int maxResults = query.getMaxResults();
		
		viewSize = (viewSize!=0)?viewSize:10;		
		
		query.setFirstResult(startIndex);
		query.setMaxResults(viewSize);
		
		@SuppressWarnings("rawtypes")
		List data = query.getResultList();
		
		Query countQuery = queriesToRun.get(DataTableCriteria.COUNT_QUERY);
		Object totalResults = countQuery.getSingleResult();
		int totalRecords = 0;
		if(totalResults instanceof Long){
			totalRecords = ((Long)totalResults).intValue();
		}else if(totalResults instanceof BigInteger){
			totalRecords = ((BigInteger)totalResults).intValue();
		}else{
			totalRecords=(int)totalResults;
		}
		_logger.debug("query count : "+totalResults);
		
		ListObjects listObjects = new ListObjects();
		listObjects.setData(data);
		listObjects.setRecordsTotal(totalRecords);
		if(maxResults>totalRecords){
			maxResults=totalRecords;
		}
		
		listObjects.setDraw(columns.getDraw());
		listObjects.setRecordsFiltered(maxResults);
		return listObjects;
	}

	public static ListObjects listSimpleObjectFromQueryDSL(DataTableCriteria columns, Map<String,JPASQLQuery> queriesToRun, Expression<?> resultListExpression, NumberExpression<Long> countExpression) {
		JPASQLQuery query = queriesToRun.get(DataTableCriteria.DATA_QUERY);
		setQueryLimitOptions(columns, query);
		
		@SuppressWarnings("rawtypes")
		List data = query.list(resultListExpression);
		
		JPASQLQuery countQuery = queriesToRun.get(DataTableCriteria.COUNT_QUERY);
		
		Long totalResults = countQuery.uniqueResult(countExpression);
		int totalRecords = ((Long)totalResults).intValue();
		_logger.debug("query count : "+totalResults);
		
		ListObjects listObjects = new ListObjects();
		listObjects.setData(data);
		listObjects.setRecordsTotal(totalRecords);
/*		if(maxResults>totalRecords){
			maxResults=totalRecords;
		}
*/		
		listObjects.setDraw(columns.getDraw());
		listObjects.setRecordsFiltered(totalRecords);
		return listObjects;
	}

	public static void setQueryLimitOptions(DataTableCriteria columns, JPASQLQuery query) {
		int viewSize = columns.getLength();
		int startIndex = columns.getStart();
		viewSize = (viewSize!=0)?viewSize:10;		
		query.offset(startIndex);
		query.limit(viewSize);
	}
}
