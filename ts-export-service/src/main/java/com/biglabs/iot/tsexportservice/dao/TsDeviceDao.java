package com.biglabs.iot.tsexportservice.dao;

import org.thingsboard.server.common.data.Device;

import java.util.List;
import java.util.UUID;

/**
 * Created by antt on 9/19/17.
 */
public interface TsDeviceDao {
    List<Device> findDevicesByTenantId(UUID tenantId);
    List<Device> findDevicesByCustomerId(UUID customerId);
}
