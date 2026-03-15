package com.manjosh.labs.device_service.service;

import com.manjosh.labs.device_service.dto.DeviceRequest;
import com.manjosh.labs.device_service.exception.DeviceNotFoundException;
import com.manjosh.labs.device_service.mapper.DeviceMapper;
import com.manjosh.labs.device_service.model.Device;
import com.manjosh.labs.device_service.repository.DeviceRepository;
import com.manjosh.labs.devicecontracts.models.DeviceResponse;
import com.manjosh.labs.devicecontracts.models.DeviceStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

  private final DeviceRepository repository;
  private final DeviceMapper deviceMapper;

  public DeviceResponse register(final DeviceRequest request) {
    final Device device = deviceMapper.toEntity(request);
    device.setStatus(DeviceStatus.REGISTERED);
    device.setCreatedAt(LocalDateTime.now());

    final Device savedDevice = repository.save(device);
    return deviceMapper.toResponse(savedDevice);
  }

  public List<DeviceResponse> getAllDevices() {
    return repository.findAll().stream().map(deviceMapper::toResponse).toList();
  }

  public DeviceResponse getDevice(final String deviceId) {
    final Device device =
        repository
            .findById(deviceId)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

    return deviceMapper.toResponse(device);
  }
}
