package com.biglabs.iot.tsexportservice.dao;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Select;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.model.nosql.DeviceEntity;
import org.thingsboard.server.dao.nosql.CassandraAbstractModelDao;
import org.thingsboard.server.dao.util.NoSqlDao;

import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.thingsboard.server.dao.model.ModelConstants.*;

@Component
@Slf4j
@NoSqlDao
public class CassandraTsDeviceDao extends CassandraAbstractModelDao<DeviceEntity, Device> implements TsDeviceDao {

    @Override
    protected Class<DeviceEntity> getColumnFamilyClass() {
        return DeviceEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DEVICE_COLUMN_FAMILY_NAME;
    }

    @Override
    public List<Device> findDevicesByTenantId(UUID tenantId) {
        log.debug("Try to find devices by tenantId [{}]", tenantId);
        List<DeviceEntity> deviceEntities = findDevicesWithClause(DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                eq(DEVICE_TENANT_ID_PROPERTY, tenantId));

        log.trace("Found devices [{}] by tenantId [{}]", deviceEntities, tenantId);
        return DaoUtil.convertDataList(deviceEntities);
    }

    @Override
    public List<Device> findDevicesByCustomerId(UUID customerId) {
        log.debug("Try to find devices by customerId [{}]", customerId);
        List<DeviceEntity> deviceEntities = findDevicesWithClause(DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                eq(DEVICE_CUSTOMER_ID_PROPERTY, customerId));

        log.trace("Found devices [{}] by customerId [{}]", deviceEntities, customerId);
        return DaoUtil.convertDataList(deviceEntities);
    }

    protected List<DeviceEntity> findDevicesWithClause(String searchView, Clause clause) {
        Select select = select().from(searchView);
        Select.Where query = select.where();
        query.and(clause);

        return findListByStatement(query);
    }

}
