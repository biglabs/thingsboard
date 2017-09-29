package com.biglabs.iot.tsexportservice.data;

/**
 * Created by antt on 9/15/17.
 */
public enum EXPORT_FORMAT {

    JSON, CSV;

    public static EXPORT_FORMAT forName(String name) { return EXPORT_FORMAT.valueOf(name.toUpperCase()); }
}
