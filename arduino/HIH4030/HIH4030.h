/*
 * HIH4030 / HIH4031 Humidity Sensor Library for Arduino 1.X
 * Copyright 2012 Andrew Bythell <abythell@ieee.org>
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
*/

#ifndef HIH4030_h
#define HIH4030_h

#include <Arduino.h>

class HIH4030 {
  
  public:
    HIH4030(uint8_t dataPin, float supplyVoltage, float referenceVoltage);
    void calibrate(float slope, float zeroOffset);
    float getSensorRH();
    float getTrueRH(float temperature);
    float vout();
   
  private:
    uint8_t pin;        /* IO pin connected to sensor's data pin */
    float vSupply;      /* voltage supplying the humidity sensor */
    float slope;        /* value may be calculated or factory calibrated */
    float zeroOffset;   /* value may be calcualted or factory calibrated */
    float vRef;         /* analog voltage reference, in volts */
};

#endif /* HIH4030_h */
