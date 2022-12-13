package it.irideos.metrics.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openstack4j.model.identity.v3.Token;
import org.springframework.beans.factory.annotation.Autowired;

import it.irideos.metrics.models.TokenModel;

// @Mapper
// public interface TokenMapper {

//   TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);
//   // TokenModel tokenToTokenModel(ToEntity token);

// }
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface TokenMapper {

  @Autowired
  Token tknToken = null;

  @Mapping(target = "id", source = "id")
  TokenModel tokenToTokenModel(Token token);

}