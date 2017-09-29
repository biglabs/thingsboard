package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.DeviceTsData;
import com.biglabs.iot.tsexportservice.data.ExportInfo;
import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.Device;
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

@Component
@Slf4j
public class CassandraTsExportDao implements TsExportDao {

    @Autowired
    CassandraBaseTimeseriesDao timeseriesDao;

    @Override
    public ListenableFuture<List<DeviceTsData>> export(ExportInfo exportInfo, List<Device> devices) {
        return Futures.allAsList(devices.stream().map(device -> export(exportInfo, device)).collect(Collectors.toList()));
    }

    private ListenableFuture<DeviceTsData> export(ExportInfo exportInfo, Device device) {
        return  Futures.transform(timeseriesDao.findAllLatest(device.getId()), new AsyncFunction<List<TsKvEntry>, DeviceTsData>() {
            @Override
            public ListenableFuture<DeviceTsData> apply(List<TsKvEntry> input) throws Exception {
                List<TsKvQuery> queries = input.stream()
                        .map(kv -> new BaseTsKvQuery(kv.getKey(),
                                exportInfo.getStartTs(),
                                exportInfo.getEndTs(),
                                1, Integer.MAX_VALUE,
                                Aggregation.NONE))
                        .collect(Collectors.toList());
                //return timeseriesDao.findAllAsync(device.getId(), queries);
                return Futures.transform(timeseriesDao.findAllAsync(device.getId(), queries), new Function<List<TsKvEntry>, DeviceTsData>() {
                    @Nullable
                    @Override
                    public DeviceTsData apply(@Nullable List<TsKvEntry> input) {
                        return new DeviceTsData(device, input);
                    }
                });
            }
        });
    }
}
