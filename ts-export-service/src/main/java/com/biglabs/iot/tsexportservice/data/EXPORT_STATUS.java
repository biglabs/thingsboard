package com.biglabs.iot.tsexportservice.data;

/**
 * Created by antt on 9/15/17.
 */
public enum EXPORT_STATUS {

    PROCESSING, DONE, NO_DATA, FAILED;

    public static EXPORT_STATUS forName(String name) {
        return EXPORT_STATUS.valueOf(name.toUpperCase());
    }
}
