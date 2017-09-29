package com.biglabs.iot.tsexportservice.controller;

import com.biglabs.iot.tsexportservice.data.EXPORT_FORMAT;
import com.biglabs.iot.tsexportservice.data.ExportInfo;
import com.biglabs.iot.tsexportservice.exception.TsExportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.data.security.Authority;

@RestController
@RequestMapping("/api")
public class ExportController extends BaseController {

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/export/devices/timeseries", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public DeferredResult<ResponseEntity> export(@RequestParam(required = false) String startTs,
                                                 @RequestParam(required = false) String endTs,
                                                 @RequestParam(required = false) String format) throws TsExportException {
        DeferredResult<ResponseEntity> result = new DeferredResult<ResponseEntity>();
        ExportInfo exportInfo = new ExportInfo(Long.valueOf(startTs), Long.valueOf(endTs),
                EXPORT_FORMAT.forName(format) == EXPORT_FORMAT.JSON ? EXPORT_FORMAT.JSON : EXPORT_FORMAT.CSV);
        if (getCurrentUser().getAuthority() == Authority.TENANT_ADMIN) {
            // TO DO call service
            exportService.export(exportInfo, getCurrentUser().getTenantId(), result);
        } else if (getCurrentUser().getAuthority() == Authority.CUSTOMER_USER) {
            // TO DO call service
            exportService.export(exportInfo, getCurrentUser().getCustomerId(), result);
        }
        return result;
    }

}
