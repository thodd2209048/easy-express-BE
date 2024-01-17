package com.example.easyexpressbackend.domain.staff;

import com.example.easyexpressbackend.domain.staff.dto.AddStaffDto;
import com.example.easyexpressbackend.domain.staff.response.CrudStaffResponse;
import com.example.easyexpressbackend.domain.staff.dto.UpdateStaffDto;
import com.example.easyexpressbackend.domain.staff.response.StaffIdNameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//        (unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Staff addStaffToStaff (AddStaffDto addStaffDto);

    @Mapping(target = "hubName", ignore = true)
    CrudStaffResponse staffToCrudStaffResponse(Staff staff);

    StaffIdNameResponse staffToStaffIdNameResponse(Staff staff);

    Staff copy(Staff staff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateStaff(UpdateStaffDto updateStaffDto, @MappingTarget Staff staff);
}
