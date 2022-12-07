package it.irideos.metrics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import it.irideos.metrics.models.TokenModel;

@Mapper
public interface TokenMapper {
  TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

  TokenModel tokenToTokenModel(String response);
}
