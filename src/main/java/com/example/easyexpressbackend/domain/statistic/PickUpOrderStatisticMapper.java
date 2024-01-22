package com.example.easyexpressbackend.domain.statistic;

import com.example.easyexpressbackend.config.MapstructConfig;
import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public interface PickUpOrderStatisticMapper {
    PickUpOrderStatisticMapper INSTANCE = Mappers.getMapper(PickUpOrderStatisticMapper.class);

    PickUpOrderStatisticResponse toPickUpOrderStatisticResponse(PickUpOrderStatistic pickUpOrderStatistic);
}
