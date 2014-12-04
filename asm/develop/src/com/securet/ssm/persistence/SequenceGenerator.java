package com.securet.ssm.persistence;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceGenerator extends org.hibernate.id.SequenceGenerator{

	private static final Logger _logger = LoggerFactory.getLogger(SequenceGenerator.class);
	
	@Override
	public Serializable generate(SessionImplementor session, Object obj) {
		_logger.debug("found params to generate id: "+obj);
		return super.generate(session, obj);
	}
}