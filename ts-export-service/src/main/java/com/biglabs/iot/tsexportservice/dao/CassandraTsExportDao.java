package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.Export;
import com.datastax.driver.core.*;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.BaseTsKvQuery;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.kv.TsKvQuery;
import org.thingsboard.server.dao.timeseries.CassandraBaseTimeseriesDao;

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

    @Override
    public List<ResultSetFuture> export(Export export, List<String> deviceIds) {
        // use paging
//        List<DvEntries> alldevKeys = deviceIds.stream()
//                .map(id -> new DvEntries(DeviceId.fromString(id), findAllLatest(DeviceId.fromString(id))))
//                .collect(Collectors.toList());
//        ListenableFuture<List<List<TsKvEntry>>> devKeys = Futures.allAsList();
//        ListenableFuture<List<ResultSet>> results = Futures.transform(devKeys, new AsyncFunction<List<List<TsKvEntry>>, List<ResultSet>>() {
//            @Override
//            public ListenableFuture<List<ResultSet>> apply(List<List<TsKvEntry>> input) throws Exception {
//                 List<TsKvQuery> queries = input.stream()
//                                            .flatMap(List::stream)
//                                            .map(kv -> new BaseTsKvQuery(kv.getKey(), export.getFromTime(), export.getToTime(), 1, Integer.MAX_VALUE, Aggregation.NONE))
//                                            .collect(Collectors.toList());
//                return findAllAsync()
//            }
//        });

//        PreparedStatement prepStmt = session.prepare(QUERY);
//        List<ResultSetFuture> futures = Lists.newArrayListWithExpectedSize(deviceIds.size());
//        for (String dId : deviceIds) {
//            BoundStatement bStmt = prepStmt.bind()
//                    .setString("deviceId", dId)
//                    .setLong("from", export.getFromTime())
//                    .setLong("to", export.getToTime());
//            ResultSetFuture f = session.executeAsync(bStmt);
//            futures.add(f);
//        }

        return null;
    }

    private ListenableFuture<List<TsKvEntry>> export(Export export, String deviceId) {
        ListenableFuture<List<TsKvEntry>> allKeys = findAllLatest(DeviceId.fromString(deviceId));
        ListenableFuture<List<TsKvEntry>> results = Futures.transform(allKeys, new AsyncFunction<List<TsKvEntry>, List<TsKvEntry>>() {
            @Override
            public ListenableFuture<List<TsKvEntry>> apply(List<TsKvEntry> input) throws Exception {
                List<TsKvQuery> queries = input.stream()
                                                .map(kv -> new BaseTsKvQuery(kv.getKey(),
                                                                            export.getFromTime(),
                                                                            export.getToTime(),
                                                                            1, Integer.MAX_VALUE,
                                                                            Aggregation.NONE))
                                                .collect(Collectors.toList());
                return findAllAsync(DeviceId.fromString(deviceId), queries);
            }
        });
        return results;
    }
}
