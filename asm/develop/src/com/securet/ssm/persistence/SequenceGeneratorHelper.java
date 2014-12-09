package com.securet.ssm.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.securet.ssm.persistence.objects.SequenceGenerator;

public class SequenceGeneratorHelper {

	private static final long START_SEQUENCE = 10000; 
	
	private static final Logger _logger = LoggerFactory.getLogger(SequenceGeneratorHelper.class);
	
	public static final long getNextSequence(String entityName,EntityManager entityManager){
		long sequenceNo = START_SEQUENCE;
		SequenceGenerator sequenceGenerator = new SequenceGenerator();
		sequenceGenerator.setSequenceName(entityName);
		SequenceGenerator foundSequenceGenerator = entityManager.find(SequenceGenerator.class, entityName);
		if(foundSequenceGenerator!=null){
			synchronized (foundSequenceGenerator) {
				sequenceNo = foundSequenceGenerator.getSequenceValue()+1;
				foundSequenceGenerator.setSequenceValue(sequenceNo);				
				entityManager.merge(foundSequenceGenerator);
			}
		}else{
			//sequence does not exists create one now
			entityManager.persist(sequenceGenerator);
		}
		entityManager.flush();
		return sequenceNo;
	}
	
}