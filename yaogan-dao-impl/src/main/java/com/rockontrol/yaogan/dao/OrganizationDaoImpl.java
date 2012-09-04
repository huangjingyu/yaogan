package com.rockontrol.yaogan.dao;

import org.springframework.stereotype.Repository;

import com.rockontrol.yaogan.model.Organization;

@Repository("orgDao")
public class OrganizationDaoImpl extends BaseDaoImpl<Organization> implements
      IOrganizationDao {

}
