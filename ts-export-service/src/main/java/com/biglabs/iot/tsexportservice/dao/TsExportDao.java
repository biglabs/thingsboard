package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.Export;
import com.biglabs.iot.tsexportservice.data.ExportedDeviceResult;
import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Device;

import java.util.List;

/**
 * Created by antt on 9/19/17.
 */
public interface TsExportDao {
    ListenableFuture<List<ExportedDeviceResult>> export(Export export, List<Device> deviceIds);
}
