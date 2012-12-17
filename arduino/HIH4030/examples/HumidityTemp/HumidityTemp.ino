/*
 * HIH4030 Library Example with Temperature Compensation via DS18S20
 * Copyright 2012 Andrew Bythell <abythell@ieee.org>
 *
 * This sketch will print out the sensor's voltage and relative humidity.  To
 * test, exhale on the sensor and watch the voltage and humidity rise and fall.
 * Compare the displayed values either with the datasheet or the factory
 * print-out.  Vout ~= 3.2V when RH ~= 75%.
 *
 * You will need the OneWire and DallasTemperature libraries installed which you can 
 * find here:
 *
 *	http://milesburton.com/Dallas_Temperature_Control_Library#Download	
 *	http://www.pjrc.com/teensy/td_libs_OneWire.html
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
 *	GND		HIH4030 pin 1 / GND / -ve
 *	A[?]		HIH4030 pin 2 / OUT
 *	VCC		HIH4030 pin 3 / 5V / +ve	(5V Arduino)
 *	4.0V - 5.8V	HIH4030 pin 3 / 5V / +ve	(3.3V Arduino)
 *	GND		DS18S20 pin 1 / GND
 *	D[?]		DS18S20 pin 2 / DQ		(plus 4.7K pull-up to VCC)
 *	VCC		DS18S20 pin 3 / VDD
 *
 * 	Review the hardware requirements on the OneWire Library page for info about
 *	connecting and using 1-wire devices.
 * 
 */

#include <OneWire.h>
#include <DallasTemperature.h>
#include <HIH4030.h>

/*
 * The Arduino data IO pin connected to DQ
 */
#define ONE_WIRE_BUS 12		

/*
 * The Arduino analog IO pin connected to OUT
 */
#define HIH4030_PIN A3

/*
 * The voltage supplying +ve - typically 5.0V
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
 * Global variables
 */
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
DeviceAddress thermometer;
HIH4030 uncalibratedHygrometer(HIH4030_PIN, HIH4030_SUPPLY_VOLTAGE, ARDUINO_VCC);
HIH4030 calibratedHygrometer(HIH4030_PIN, HIH4030_SUPPLY_VOLTAGE, ARDUINO_VCC);

void setup(void)
{
  /* serial port */
  Serial.begin(9600);
  
  /* 1-Wire and Temperature Sensor */
  sensors.begin();
  if (!sensors.getAddress(thermometer, 0)) Serial.println("Unable to find DS18S20"); 

  /* hygrometer */ 
  calibratedHygrometer.calibrate(SLOPE, OFFSET);
}

void printSensorData(HIH4030 hygrometer, float temperature) {
  Serial.print("Temperature = ");
  Serial.print(temperature);
  Serial.print("C  Sensor Voltage = ");
  Serial.print(hygrometer.vout());
  Serial.print("V  Relative Humidity = ");
  Serial.print(hygrometer.getSensorRH());
  Serial.print("%  Compensated Humidity = ");
  Serial.print(hygrometer.getTrueRH(temperature));
  Serial.println("%");
}

void loop(void)
{ 
  float temperature;
  sensors.requestTemperatures(); 
  delay(1000);
  temperature = sensors.getTempC(thermometer);
  Serial.print("Uncalibrated: ");
  printSensorData(uncalibratedHygrometer, temperature); 
  Serial.print("Calibrated: ");
  printSensorData(calibratedHygrometer, temperature); 
  Serial.println("");
}
