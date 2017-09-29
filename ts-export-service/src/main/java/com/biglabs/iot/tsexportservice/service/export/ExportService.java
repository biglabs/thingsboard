package com.biglabs.iot.tsexportservice.service.export;

import com.biglabs.iot.tsexportservice.data.ExportInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;

import java.util.List;

public interface ExportService {
    // input
    //      from/to, list<customer>, export format
    // Validate input export range 3 month maximum
    // (async) QUERY timeseries, store result, update export job information (e.g output_path, export status)
    //

    void export(ExportInfo export, TenantId tenantId, DeferredResult<ResponseEntity> result);
    void export(ExportInfo export, CustomerId customerId, DeferredResult<ResponseEntity> result);
    List<Device> findDevicesByTenantId(TenantId tenantId);
    List<Device> findDevicesByCustomerId(CustomerId customerId);
}
