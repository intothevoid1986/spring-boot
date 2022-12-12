package it.irideos.metrics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenMapper {

  TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);
  // TokenModel tokenToTokenModel(ToEntity token);

}