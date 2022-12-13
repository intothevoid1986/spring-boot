package it.irideos.metrics.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.openstack4j.model.identity.v3.Token;

import it.irideos.metrics.models.TokenModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface GnocchiApiMapper {

  GnocchiApiMapper INSTANCE = Mappers.getMapper(GnocchiApiMapper.class);

  TokenModel tokenToTokenModel(Token token);

}