/*
 * Sample sketch showing the HIH4030 library in use without temperature compensation.
 * Copyright 2012 Andrew Bythell <abythell@ieee.org>
 *
 * This sketch will print out the sensor's voltage and relative humidity.  To
 * test, exhale on the sensor and watch the voltage and humidity rise and fall.
 * Compare the displayed values either with the datasheet or the factory
 * print-out.  Vout ~= 3.2V when RH ~= 75% at room temperature.
 *
 * For the most accurate results, order part HIH-4030-003 (non-condensing) or
 * the HIH-4031-003 (condensing), which come with calibration data.  When
 * possible, 5V should be used to supply the sensor, as the calibration data is
 * only given at this voltage, but if other supply voltages are used, the
 * library will account for it.
 *
 * Basic Hardware Setup:
 *
 *	ARDUINO 	SENSOR
 *	-------		------
 *	GND		pin 1 / GND / -ve
 *	A[?]		pin 2 / OUT
 *	VCC		pin 3 / 5V / +ve	(5V Arduino)
 *	4.0V - 5.8V	pin 3 / 5V / +ve	(3.3V Arduino)
 */

#include <HIH4030.h>

/*
 * Set the name of the Arduino's Analog IO Pin that is connected to the sensor's
 * OUT / data pin.
 */
#define HIH4030_PIN A3

/*
 * Set the voltage that is supplying pin3/+ve. Typical=5.0 
 */ 
#define HIH4030_SUPPLY_VOLTAGE  5.0

/*
 * Set the analog reference voltage.  Unless you have set it explicity using
 * analogReference(), this will be the same voltage as your Arduino, either 3.3 or 5.0
 */
#define ARDUINO_VCC 5.0

/*
 * Set Factory Calibration Values, if available.  Here we'll use the values from
 * the datasheet's Table 2: Example Data Printout
 */
#define SLOPE 0.03068
#define OFFSET 0.958

/*
 * Create 2 instances of the sensor, one calibrated, one not, to show the difference
 * calibration makes.
 */
HIH4030 uncalibratedHygrometer(HIH4030_PIN, HIH4030_SUPPLY_VOLTAGE, ARDUINO_VCC);
HIH4030 calibratedHygrometer(HIH4030_PIN, HIH4030_SUPPLY_VOLTAGE, ARDUINO_VCC);

void setup(void)
{
  /* serial port */
  Serial.begin(9600);

  /* calibration */
  calibratedHygrometer.calibrate(SLOPE, OFFSET);

}

void printSensorData(HIH4030 hygrometer) {
  Serial.print("Sensor Voltage = ");
  Serial.print(hygrometer.vout());
  Serial.print("V  Relative Humidity = ");
  Serial.print(hygrometer.getSensorRH());
  Serial.println("%");
}

void loop(void)
{ 
  Serial.print("Uncalibrated: ");
  printSensorData(uncalibratedHygrometer);
  Serial.print("Calibrated: ");
  printSensorData(calibratedHygrometer);
  Serial.println("");
  delay(1000);
}

