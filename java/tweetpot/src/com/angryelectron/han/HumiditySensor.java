/**
 * XBee HumiditySensor.
 * Copyright 2012, Andrew Bythell <abythell@ieee.org> 
 * 
 * This class will read values sent from a remote XBee that is periodically
 * sampling A0.  A0 is the output of a connected HIH4031 humidity
 * sensor.  The samples are received by a local XBee connected via serial port.
 * Received samples are converted to a voltage, then converted to relative 
 * humidity using the HIH4030 library.
 * 
 * It is assumed that the HIH4031 is supplied using the same 3.3V supplying the 
 * XBee.  This provides less accurate results (since +ve(min) is 4V) but makes
 * the hardware simple.
 */ 

package com.angryelectron.han;

import com.angryelectron.sensor.HIH403X;
import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;

public class HumiditySensor implements PacketListener {       
    
    /*
     * Assuming default values for the XBee, and that the humidity sensor's
     * supply voltage is the same as the XBee's
     */
    private final double vcc = 3.3;
    private final double adResolution = 1023;   //10-bit (2^10) resolution
    
    private HIH403X hih4030 = new HIH403X(vcc);
    private XBee xbee = new XBee();
    private HanEventHandler eventHandler; 
    private double relativeHumidity;

    /**
     * Constructor.
     * @param eventHandler An instance of HanEventHandler that will receive the
     * sensor's output.
     */
    public HumiditySensor(HanEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    /**
     * Start listening for sensor readings.
     * @param port Serial port connected to the local XBee (/dev/ttyUSB0 or COM3)
     * @param baud Baud rate of local XBee (typically 9600).
     * @throws XBeeException
     */
    public void start(String port, Integer baud) throws XBeeException {
        if (!xbee.isConnected()) {
            xbee.open(port, baud);
            xbee.addPacketListener(this);
        }
    }                
    
    /**
     * Stop listening for sensor readings.
     */
    public void stop() {
        xbee.removePacketListener(this);
        xbee.close();
    }
    
    /**
     * Internal use only.  Called when the XBee PacketListener receives
     * a response packet.  If the response contains an humidity sensor
     * reading, it is converted to relative humidity and the HanEventHandler
     * is invoked.
     * @param xbr  The response packet received.
     */
    @Override
    public void processResponse(XBeeResponse xbr) {
        if (xbr.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) {
                ZNetRxIoSampleResponse sample = (ZNetRxIoSampleResponse) xbr;
                setRelativeHumidity(sample.getAnalog0());
                eventHandler.onSensorReading(this);
            }
    }
    
    /**
     * Set relative humidity.  Converts XBee analog sample to actual voltage.
     * @param analogOutput The analog sample value from the sample response packet.
     */
    private void setRelativeHumidity(Integer analogOutput) {                                
        double vout = (analogOutput * vcc / adResolution);
        relativeHumidity = hih4030.getSensorRH(vout);
    }
    
    /**
     * Get relative humidity.  Call from within HanEventHandler to retrieve
     * sensor reading.
     * @return Non-temperature compensated relative humidity, in percent.
     */
    public double getRelativeHumidity() {
        return relativeHumidity;
    }
    
}
