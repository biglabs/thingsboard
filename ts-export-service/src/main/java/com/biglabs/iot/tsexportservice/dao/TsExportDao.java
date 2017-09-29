package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.ExportInfo;
import com.biglabs.iot.tsexportservice.data.DeviceTsData;
import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.Device;

import java.util.List;

/**
 * Created by antt on 9/19/17.
 */
public interface TsExportDao {
    ListenableFuture<List<DeviceTsData>> export(ExportInfo export, List<Device> devices);
}
