/*
 * HanEventHandler
 * Copyright 2010, Andrew Bythell, abythell@ieee.org
 */
package com.angryelectron.han;

public interface HanEventHandler {
    void onSensorReading(Object sensor);
    void onError(Object sensor);
}
