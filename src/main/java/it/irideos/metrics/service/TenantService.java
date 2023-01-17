package it.irideos.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.irideos.metrics.models.TenantModel;
import it.irideos.metrics.repository.TenantRepository;
import jakarta.transaction.Transactional;

@Service
public class TenantService {

  @Autowired
  private TenantRepository tenantRepository;

  @Transactional
  public TenantModel createTenant(TenantModel tenant) {
    return tenantRepository.save(tenant);
  }
}
