package com.securet.ssm.utils;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.securet.ssm.persistence.views.aggregates.SecureTAggregate;

public class EntityUtils {

	public static List<SecureTAggregate> getTicketCountByServiceType(EntityManager entityManager, String namedQuery){
		Query serviceTypeAggregate = entityManager.createNamedQuery(namedQuery);
		List<SecureTAggregate> secureTAggregates = serviceTypeAggregate.getResultList();
		Collections.sort(secureTAggregates);
		return secureTAggregates;		
	}
	

}
