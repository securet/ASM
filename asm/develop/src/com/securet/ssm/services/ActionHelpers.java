package com.securet.ssm.services;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		int viewSize = columns.getLength();
		int startIndex = columns.getStart();
		int maxResults = query.getMaxResults();
		
		viewSize = (viewSize!=0)?viewSize:10;		
		
		query.setFirstResult(startIndex);
		query.setMaxResults(viewSize);
		
		@SuppressWarnings("rawtypes")
		List data = query.getResultList();
		
		Query countQuery = entityManager.createQuery(countQueryStr);
		Long totalResults = (Long)countQuery.getSingleResult();
		_logger.debug("query count : "+totalResults);
		
		ListObjects listObjects = new ListObjects();
		listObjects.setData(data);
		listObjects.setRecordsTotal(totalResults.intValue());
		if(maxResults>totalResults.intValue()){
			maxResults=totalResults.intValue();
		}
		
		listObjects.setDraw(columns.getDraw());
		listObjects.setRecordsFiltered(maxResults);
		return listObjects; 
	}

}
