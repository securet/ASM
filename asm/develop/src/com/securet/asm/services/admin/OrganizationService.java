package com.securet.asm.services.admin;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securet.asm.persistence.objects.Organization;

@Service
@Repository
@RequestMapping("/admin")
public class OrganizationService {

	private static final Logger _logger = LoggerFactory.getLogger(OrganizationService.class);

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	private Organization organization;

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
		this.entityManagerFactory=entityManagerFactory;
	}
	
	public void setEntityManager(EntityManager entityManager){
		this.entityManager=entityManager;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@RequestMapping("/createorg")
	public String createOrg(Model model){
		_logger.debug("model :"+model.asMap());
		return "ping";
	}
	
	@Transactional
	public void createOrganization() {
		//System.out.println("emf: "+emf);
		//entityManager = entityManagerFactory.createEntityManager();
		entityManager.persist(organization);
	}

}
