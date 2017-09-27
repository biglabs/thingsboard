package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.Export;
import com.biglabs.iot.tsexportservice.data.ExportedDeviceResult;
import com.datastax.driver.core.*;
import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.BaseTsKvQuery;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.kv.TsKvQuery;
import org.thingsboard.server.dao.timeseries.CassandraBaseTimeseriesDao;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by antt on 9/15/17.
 */
public class CassandraTsExportDao extends CassandraBaseTimeseriesDao implements TsExportDao {
    @Autowired
    Session session;

    private static class DvEntries {
        DeviceId id;
        ListenableFuture<List<TsKvEntry>> entries;

        public DvEntries(DeviceId id, ListenableFuture<List<TsKvEntry>> entries) {
            this.id = id;
            this.entries = entries;
        }
    }

    private class DeviceExportResult {
        DeviceId id;
        List<TsKvEntry> entries;

        public DeviceExportResult(DeviceId id, List<TsKvEntry> entries) {
            this.id = id;
            this.entries = entries;
        }

    }

    @Override
    public ListenableFuture<List<ExportedDeviceResult>> export(Export export, List<Device> devices) {
        return Futures.allAsList(devices.stream().map(device -> export(export, device)).collect(Collectors.toList()));
    }

//    private ListenableFuture<DeviceExportResult> export(Export export, String deviceId) {
//        ListenableFuture<List<TsKvEntry>> allKeys = findAllLatest(DeviceId.fromString(deviceId));
//        ListenableFuture<List<TsKvEntry>> kvEntries = Futures.transform(allKeys, new AsyncFunction<List<TsKvEntry>, List<TsKvEntry>>() {
//            @Override
//            public ListenableFuture<List<TsKvEntry>> apply(List<TsKvEntry> input) throws Exception {
//                List<TsKvQuery> queries = input.stream()
//                                                .map(kv -> new BaseTsKvQuery(kv.getKey(),
//                                                                            export.getFromTime(),
//                                                                            export.getToTime(),
//                                                                            1, Integer.MAX_VALUE,
//                                                                            Aggregation.NONE))
//                                                .collect(Collectors.toList());
//                return findAllAsync(DeviceId.fromString(deviceId), queries);
//            }
//        });
//
//        ListenableFuture<DeviceExportResult> deviceExport = Futures.transform(kvEntries, new Function<List<TsKvEntry>, DeviceExportResult>() {
//            @Nullable
//            @Override
//            public DeviceExportResult apply(@Nullable List<TsKvEntry> input) {
//                return new DeviceExportResult(DeviceId.fromString(deviceId), input);
//            }
//        });
//        return deviceExport;
//    }

    private ListenableFuture<ExportedDeviceResult> export(Export export, Device device) {
        ListenableFuture<List<TsKvEntry>> allKeys = findAllLatest(device.getId());
        ListenableFuture<List<TsKvEntry>> kvEntries = Futures.transform(allKeys, new AsyncFunction<List<TsKvEntry>, List<TsKvEntry>>() {
            @Override
            public ListenableFuture<List<TsKvEntry>> apply(List<TsKvEntry> input) throws Exception {
                List<TsKvQuery> queries = input.stream()
                        .map(kv -> new BaseTsKvQuery(kv.getKey(),
                                export.getFromTime(),
                                export.getToTime(),
                                1, Integer.MAX_VALUE,
                                Aggregation.NONE))
                        .collect(Collectors.toList());
                return findAllAsync(device.getId(), queries);
            }
        });

        ListenableFuture<ExportedDeviceResult> deviceExport = Futures.transform(kvEntries, new Function<List<TsKvEntry>, ExportedDeviceResult>() {
            @Nullable
            @Override
            public ExportedDeviceResult apply(@Nullable List<TsKvEntry> input) {
                return new ExportedDeviceResult(device, input);
            }
        });
        return deviceExport;
    }
}
