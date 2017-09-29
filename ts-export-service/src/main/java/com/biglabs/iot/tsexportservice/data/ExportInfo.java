package com.biglabs.iot.tsexportservice.data;

/**
 * Created by antt on 9/15/17.
 */

public class ExportInfo {
    private Long startTs;
    private Long endTs;
    private EXPORT_FORMAT exportFormat;

    public ExportInfo(Long startTs, Long endTs, EXPORT_FORMAT exportFormat) {
        this.startTs = startTs;
        this.endTs = endTs;
        this.exportFormat = exportFormat;
    }

    public Long getStartTs() { return startTs; }

    public void setStartTs(Long startTs) { this.startTs = startTs; }

    public Long getEndTs() { return endTs; }

    public void setEndTs(Long endTs) { this.endTs = endTs; }

    public EXPORT_FORMAT getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(EXPORT_FORMAT exportFormat) {
        this.exportFormat = exportFormat;
    }

}
