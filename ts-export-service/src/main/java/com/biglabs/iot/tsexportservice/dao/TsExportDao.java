package com.biglabs.iot.tsexportservice.dao;

import com.biglabs.iot.tsexportservice.data.Export;
import com.datastax.driver.core.ResultSetFuture;

import java.util.List;
import java.util.Optional;

/**
 * Created by antt on 9/19/17.
 */
public interface TsExportDao {
    Optional<List<ResultSetFuture>> export(Export export, List<String> deviceIds);
}
