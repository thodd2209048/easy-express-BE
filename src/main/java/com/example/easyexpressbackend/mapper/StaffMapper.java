package com.example.easyexpressbackend.mapper;

import com.example.easyexpressbackend.dto.staff.AddStaffDto;
import com.example.easyexpressbackend.dto.staff.UpdateStaffDto;
import com.example.easyexpressbackend.entity.Staff;
import com.example.easyexpressbackend.response.StaffResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper
//        (unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Staff addStaffToStaff (AddStaffDto addStaffDto);

    StaffResponse staffToStaffResponse(Staff staff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateStaff(UpdateStaffDto updateStaffDto, @MappingTarget Staff staff);
}
