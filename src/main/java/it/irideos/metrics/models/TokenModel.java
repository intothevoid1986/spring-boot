package it.irideos.metrics.models;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TokenModel extends BaseModel {

  private String id;
  private Date expires;
  private Date issuedAt;
  private List<String> auditIds;
  private List<String> methods;
  private UserModel user;

}
