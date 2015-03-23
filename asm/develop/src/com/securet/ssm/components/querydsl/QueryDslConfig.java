package com.securet.ssm.components.querydsl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLTemplates;

@Component
public class QueryDslConfig {

	private static final Logger _logger = Logger.getLogger(QueryDslConfig.class);
	
	@Bean(name="queryDslConfg")
	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SQLTemplates getSQLTemplate(){
		return new MySQLTemplates();
	}
	
}
