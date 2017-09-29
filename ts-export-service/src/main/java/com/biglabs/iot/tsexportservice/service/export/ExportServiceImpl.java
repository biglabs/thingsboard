package com.biglabs.iot.tsexportservice.service.export;

import com.biglabs.iot.tsexportservice.dao.TsDeviceDao;
import com.biglabs.iot.tsexportservice.dao.TsExportDao;
import com.biglabs.iot.tsexportservice.data.ExportInfo;
import com.biglabs.iot.tsexportservice.data.DeviceTsData;
import com.biglabs.iot.tsexportservice.utils.ObjectToJsonFile;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.thingsboard.server.dao.service.Validator.validateId;

@Service
@Slf4j
public class ExportServiceImpl implements ExportService {

    @Autowired
    TsExportDao tsExportDao;

    @Autowired
    TsDeviceDao tsDeviceDao;

    @PostConstruct
    public void init() { }

    @Override
    public void export(ExportInfo export, TenantId tenantId, DeferredResult<ResponseEntity> result) {
        List<Device> devices = tsDeviceDao.findDevicesByTenantId(tenantId.getId());
        ListenableFuture<List<DeviceTsData>> future = tsExportDao.export(export, devices);
        Futures.addCallback(future, getTsKvListCallback(result));
    }

    @Override
    public void export(ExportInfo export, CustomerId customerId, DeferredResult<ResponseEntity> result) {
        List<Device> devices = tsDeviceDao.findDevicesByCustomerId(customerId.getId());
        ListenableFuture<List<DeviceTsData>> future = tsExportDao.export(export, devices);
        Futures.addCallback(future, getTsKvListCallback(result));
    }

    @Override
    public List<Device> findDevicesByTenantId(TenantId tenantId) {
        log.trace("Executing findDevicesByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, "Incorrect tenantId " + tenantId);
        List<Device> devices = tsDeviceDao.findDevicesByTenantId(tenantId.getId());
        return devices;
    }

    @Override
    public List<Device> findDevicesByCustomerId(CustomerId customerId) {
        log.trace("Executing findDevicesByCustomerId, customerId [{}]", customerId);
        validateId(customerId, "Incorrect customerId " + customerId);
        List<Device> devices = tsDeviceDao.findDevicesByCustomerId(customerId.getId());
        return devices;
    }

    private FutureCallback<List<DeviceTsData>> getTsKvListCallback(final DeferredResult<ResponseEntity> result) {
        return new FutureCallback<List<DeviceTsData>>() {
            @Override
            public void onSuccess(List<DeviceTsData> data) {
                //results.addAll(data);
                result.setResult(new ResponseEntity<>(data, HttpStatus.OK));
                ObjectToJsonFile.writeJsonFile(data);
            }

            @Override
            public void onFailure(Throwable thrown) {
                log.error("Failed to fetch historical data", thrown);
                result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        };
    }
}
