package com.biglabs.iot.tsexportservice.data;

/**
 * Created by antt on 9/15/17.
 */

public class Export {
    long fromTime;
    long toTime;
    EXPORT_FORMAT exportFormat;
    EXPORT_STATUS exportStatus;

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }

    public EXPORT_FORMAT getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(EXPORT_FORMAT exportFormat) {
        this.exportFormat = exportFormat;
    }

    public EXPORT_STATUS getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(EXPORT_STATUS exportStatus) {
        this.exportStatus = exportStatus;
    }
}
