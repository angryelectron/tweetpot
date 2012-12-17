package com.angryelectron.sensor;

/**
 * Calculate relative humidity from the output of a HIH4030 or HIH4031 humidity
 * sensor. <br/>
 *
 * <p>HIH403X Copyright 2012, Andrew Bythell, abythell@ieee.org.</p>
 *
 * <p>This class aims to produce results that are as accurate as possible,
 * allowing the user to adjust temperature, supply voltage, and use factory
 * calibration data when available. The equations used are derived directly from
 * the data sheet, not from assumptions or sample calibration data. For the most
 * accurate results, order part HIH-4030-003 (non-condensing) or the
 * HIH-4031-003 (condensing), which come with calibration data. When possible,
 * 5V should be used to supply the sensor, as the calibration data is only given
 * at this voltage.</p>
 *
 * <p>This is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with the HIH403X Library. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.</p>
 */
public class HIH403X {

    private double vSupply;
    private float slope;
    private float zeroOffset;

    /**
     * Create a HIH403X sensor with the given supply voltage. Relative humidity
     * is calculated using the following equations taken from the datasheet:
     * 
     * <ol>
     *   <li> Vout = (VSupply)(0.0062(sensorRH) + 0.16) 
     *   <li> sensorRH = (Vout - zeroOffset) / slope
     * </ol>
     *
     * <p>Solving (1) for sensorRH:</p> 
     * <ul>
     *   <li> sensorRH = (Vout - (0.16)VSupply) / (0.0062)VSupply
     * </ul>
     *
     * <p>Equate result with (2) to find:</p>
     * 
     * <ul>
     *   <li>zeroOffset = (0.16)
     *   <li>VSupply slope = (0.0062)VSupply
     * </ul>
     *
     * @param supplyVoltage Hardware dependent. The voltage connected to
     * (+ve/5V/pin3) of the sensor. Typically supplyVoltage=5.0, but 4.0 - 5.8V
     * is an acceptable range according to the data sheet. Lower values
     * (supplyVoltage=3.3) do provide meaningful readings, but the accuracy
     * cannot be ensured.
     */
    public HIH403X(double supplyVoltage) {
        vSupply = supplyVoltage;
        slope = (float) (0.0062 * vSupply);
        zeroOffset = (float) (0.16 * vSupply);
    }

    /**
     * Optional: Calibrate the sensor using slope and zero offset values from
     * the factory calibration data. This will override the supply voltage,
     * since factory data is only given for supplyVoltage=5.0. If calibrate() is
     * not called, slope and offset will be calculated based on the supply
     * voltage specified in the constructor using the equations above.
     *
     * @param slope Slope value from factory calibration sheet, in Volts (ie.
     * 0.0306, not 30.6 mV/%RH)
     * @param zeroOffset Zero Offset value from factory calibration sheet, in
     * volts
     */
    public void calibrate(float slope, float zeroOffset) {
        this.slope = slope;
        this.zeroOffset = zeroOffset;
        vSupply = 5.0;
    }

    /**
     * Get relative humidity. This value is not temperature compensated.
     * Calculated using equation (2).
     *
     * @param vout Output voltage (in Volts) of the sensor (pin 2).
     * @return Relative humidity, in percent.
     */
    public double getSensorRH(double vout) {
        return (vout - zeroOffset) / slope;
    }

    /**
     * Get temperature compensated relative humidity. From the datasheet: 
     * <ul>
     *   <li> trueRH = (sensorRH) / (1.0546 - 0.00216 * temperature)
     * </ul>
     *
     * @param vout Output voltage (in Volts) of the sensor (pin 2).
     * @param temperature Temperature in degrees Celcius.
     * @return Temperature compensated relative humidity, in percent.
     */
    public double getTrueRH(double vout, double temperature) {
        return getSensorRH(vout) / (1.0546 - (0.00216 * temperature));
    }
}
