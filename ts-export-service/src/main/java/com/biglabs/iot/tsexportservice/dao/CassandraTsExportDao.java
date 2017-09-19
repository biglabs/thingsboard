package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.Export;
import com.datastax.driver.core.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.dao.nosql.CassandraAbstractAsyncDao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by antt on 9/15/17.
 */
public class CassandraTsExportDao extends CassandraAbstractAsyncDao implements TsExportDao {
    @Autowired
    Session session;

    @Override
    public Optional<List<ResultSetFuture>> export(Export export, List<String> deviceIds) {
        // use paging
        final String QUERY = "SELECT * " +
                                "FROM ts_kv_cf " +
                                "WHERE entity_type = 'DEVICE' " +
                                    "AND entity_id = :deviceId " +
                                    "AND :from <= ts AND ts <= :to ";
        PreparedStatement prepStmt = session.prepare(QUERY);
        List<ResultSetFuture> futures = Lists.newArrayListWithExpectedSize(deviceIds.size());
        for (String dId : deviceIds) {
            BoundStatement bStmt = prepStmt.bind()
                    .setString("deviceId", dId)
                    .setLong("from", export.getFromTime())
                    .setLong("to", export.getToTime());
            ResultSetFuture f = session.executeAsync(bStmt);
            futures.add(f);
        }

        return Optional.of(futures);
    }
}
