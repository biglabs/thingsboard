package com.biglabs.iot.tsexportservice.service.export;

import com.biglabs.iot.tsexportservice.dao.TsDeviceDao;
import com.biglabs.iot.tsexportservice.dao.TsExportDao;
import com.biglabs.iot.tsexportservice.data.EXPORT_FORMAT;
import com.biglabs.iot.tsexportservice.data.ExportInfo;
import com.biglabs.iot.tsexportservice.data.DeviceTsData;
import com.biglabs.iot.tsexportservice.utils.ObjectToJsonFile;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Autowired
    ObjectToJsonFile objectToJsonFile;

    @PostConstruct
    public void init() { }

    @Override
    public void export(ExportInfo exportInfo, TenantId tenantId, DeferredResult<ResponseEntity> result) {
        List<Device> devices = findDevicesByTenantId(tenantId);
        ListenableFuture<List<DeviceTsData>> future = tsExportDao.export(exportInfo, devices);
        Futures.addCallback(future, getDeviceTsDataListCallback(result, exportInfo));
    }

    @Override
    public void export(ExportInfo exportInfo, CustomerId customerId, DeferredResult<ResponseEntity> result) {
        List<Device> devices = findDevicesByCustomerId(customerId);
        ListenableFuture<List<DeviceTsData>> future = tsExportDao.export(exportInfo, devices);
        Futures.addCallback(future, getDeviceTsDataListCallback(result, exportInfo));
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

    private FutureCallback<List<DeviceTsData>> getDeviceTsDataListCallback(final DeferredResult<ResponseEntity> result, ExportInfo exportInfo) {
        return new FutureCallback<List<DeviceTsData>>() {
            @Override
            public void onSuccess(List<DeviceTsData> data) {
                result.setResult(new ResponseEntity<>(data, HttpStatus.OK));
                String format = exportInfo.getExportFormat() == EXPORT_FORMAT.JSON ? ".json" : ".csv";
                objectToJsonFile.writeJsonFile(data, RandomStringUtils.randomAlphanumeric(20) + format);
            }

            @Override
            public void onFailure(Throwable thrown) {
                log.error("Failed to fetch historical data", thrown);
                result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        };
    }
}
