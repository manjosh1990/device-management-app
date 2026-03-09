package com.manjosh.labs.device_service.controller;

import com.manjosh.labs.device_service.dto.DeviceRequest;
import com.manjosh.labs.device_service.dto.DeviceResponse;
import com.manjosh.labs.device_service.service.DeviceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
public class DeviceController {

  @Autowired private DeviceService deviceService;

  @PostMapping
  public DeviceResponse registerDevice(@RequestBody DeviceRequest request) {

    return deviceService.register(request);
  }

  @GetMapping
  public List<DeviceResponse> getAllDevices() {
    return deviceService.getAllDevices();
  }

  @GetMapping("/{deviceId}")
  public DeviceResponse getDevice(@PathVariable String deviceId) {

    return deviceService.getDevice(deviceId);
  }
}
