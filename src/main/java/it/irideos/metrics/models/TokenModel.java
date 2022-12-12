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
  private Date issued;
  private List<String> auditsIds;
  private List<String> methods;
  private UserModel user;

}
