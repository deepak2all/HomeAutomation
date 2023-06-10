package com.home.iot.mapper;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceMapper extends GenericMapper<Device, DeviceDTO> {
    @Override
    @Mapping(target = "id", ignore = true)
    Device asEntity(DeviceDTO dto);

}
