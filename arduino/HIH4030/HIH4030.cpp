/*
 * HIH4030 / HIH4031 Humidity Sensor Library for Arduino 1.X
 * Copyright 2012 Andrew Bythell <abythell@ieee.org>
 *
 * This library aims to produce results that are as accurate as possible,
 * allowing the user to adjust temperature, supply voltage, and use factory
 * calibration data when available.  The equations used are explained below and
 * are derived directly from the data sheet, not from assumptions or sample
 * calibration data.
 *
 * For the most accurate results, order part HIH-4030-003 (non-condensing) or
 * the HIH-4031-003 (condensing), which come with calibration data.  When
 * possible, 5V should be used to supply the sensor, as the calibration data is
 * only given at this voltage, but if other supply voltages are used, the
 * library will account for it.
 * 
 * The HIH4030 Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The HIH4030 Library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the HIH4030 Library.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

#include <HIH4030.h>

/*
 * Constructor.
 *  dataPin: the Arduino IO pin connected to the sensor's data pin (pin 2)
 *  supplyVoltage: the voltage supplying the humidity sensor (pins 1,3 - typ. 5.0)
 *  referenceVoltage: typically equal to the Arduino's VCC, unless you are using 
 *    analogReference() - see http://arduino.cc/it/Reference/AnalogReference
 */
HIH4030::HIH4030(uint8_t dataPin, float supplyVoltage, float referenceVoltage){
  pin = dataPin;
  vSupply = supplyVoltage;
  vRef = referenceVoltage;
  pinMode(pin, INPUT);
  
  /*
   * Relative Humidity is calculated using the following equations taken from the datasheet:
   *    (1) Vout = (VSupply)(0.0062(sensorRH) + 0.16)
   *    (2) sensorRH = (Vout - zeroOffset) / slope
   *
   * Solving (1) for sensorRH:   
   *    sensorRH = (Vout - (0.16)VSupply) / (0.0062)VSupply
   *
   * Equate result with (2) to find:
   *    zeroOffset = (0.16)VSupply
   *    slope = (0.0062)VSupply
   */   
   slope = 0.0062 * vSupply;
   zeroOffset = 0.16 * vSupply;  
}

/*
 * Calibrate the sensor using Factory Calibration Data.  Can only be used when
 * vSupply = 5V (as factory data is only supplied at this voltage)
 */
void HIH4030::calibrate(float newSlope, float newZeroOffset) {
  vSupply = 5.0;
  slope = newSlope;
  zeroOffset = newZeroOffset;
}

/*
 * Convert sensor reading into relative humidity using equation (2)
 */ 
float HIH4030::getSensorRH() {
  return ((vout() - zeroOffset) / slope);
}

/*
 * Get temperature-compensated relative humity. From data sheet: 
 *  (3) trueRH = sensorRH / (1.0546 - 0.00216T)
 */
float HIH4030::getTrueRH(float temperature) {  
  return getSensorRH() / (1.0546 - (0.00216 * temperature)); 
}

/*
 * Get sensor output voltage by converting the
 * value returned by analogRead() into an actual voltage.
 * Assumes 10-bit (2^10 = 1024) A/D resolution.
 */
float HIH4030::vout() {
  return (float)(analogRead(pin)) * vRef / 1023;
}

