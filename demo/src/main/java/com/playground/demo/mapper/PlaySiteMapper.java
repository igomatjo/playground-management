package com.playground.demo.mapper;

import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.model.PlaySiteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaySiteMapper {

  PlaySiteResponse toResponse(PlaySiteEntity playSite);
}
