package com.biglabs.iot.tsexportservice.data;

import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;

/**
 * Created by antt on 9/27/17.
 */
public class DeviceTsData {
    private Device device;
    private List<TsKvEntry> entries;

    public DeviceTsData(Device device, List<TsKvEntry> entries) {
        this.device = device;
        this.entries = entries;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<TsKvEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TsKvEntry> entries) {
        this.entries = entries;
    }
}
