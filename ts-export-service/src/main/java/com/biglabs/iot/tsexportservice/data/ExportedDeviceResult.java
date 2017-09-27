package com.biglabs.iot.tsexportservice.data;

import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;

/**
 * Created by antt on 9/27/17.
 */
public class ExportedDeviceResult {
    Device device;
    private List<TsKvEntry> entries;

    public ExportedDeviceResult(Device device, List<TsKvEntry> entries) {
        this.device = device;
        this.entries = entries;
    }
}
