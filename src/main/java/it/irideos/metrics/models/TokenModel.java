package it.irideos.metrics.models;

import java.util.Date;
import java.util.List;

import org.openstack4j.model.identity.AuthVersion;
import org.openstack4j.model.identity.v2.Token;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TokenModel extends BaseModel implements Token {

  private String id;
  private Date expires;
  private Date issuedAt;
  private List<String> auditIds;
  private List<String> methods;
  private UserModel user;

  @Override
  public AuthVersion getVersion() {
    // TODO Auto-generated method stub
    return null;
  }

}
