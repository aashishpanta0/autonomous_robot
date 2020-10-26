package lejos;

import ev3.exercises.library.*;  
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
 
//This is the final and working JAVA file

import lejos.hardware.port.Port;
 
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
 
import lejos.utility.Delay;

public class project1 {
	MovePilot pilot;

	public static void main(String[] args) {
		Port port = LocalEV3.get().getPort("S4");

		EV3UltrasonicSensor ultra = new EV3UltrasonicSensor(port);

		SensorMode distancefinder = (SensorMode) ultra.getDistanceMode();

		float[] sample = new float[ultra.sampleSize()];
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.6).offset(-7);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.6).offset(7);
//		Wheel sensormotor = WheeledChassis.modelWheel(Motor.D, 0).offset(4);

		Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		UltraSonicDemo usd = new UltraSonicDemo();
		usd.pilot = new MovePilot(myChassis);
		Button.LEDPattern(4);
		Sound.beep();
		LCD.drawString("Press any key ", 0, 0);
		Button.waitForAnyPress();
		while (true) {
			

			distancefinder.fetchSample(sample, 0);
			Delay.msDelay(100);

			if (sample[0] > 0.5) {
				LCD.drawString("Distance: " + sample[0], 0, 0);

				usd.pilot.travel(30);

				distancefinder.fetchSample(sample, 0);
				Delay.msDelay(100);
			} else if (sample[0] <= 0.5) {
				Motor.D.rotate(-90);
				LCD.drawString("Distance: " + sample[0], 0, 0);
				distancefinder.fetchSample(sample, 0);
				if (sample[0] < 0.5) {
					Motor.D.rotate(90);
					usd.pilot.arc(5,90);
					
					Delay.msDelay(100);
					distancefinder.fetchSample(sample, 0);
					

				} else {
					usd.pilot.arc(5,-90);
					Motor.D.rotate(90);

				}

				//				LCD.drawString("less than 25 cm", 0, 0);
				Delay.msDelay(100);
			}
			 
			if (Button.ESCAPE.isDown()) {
				usd.pilot.stop();
				 
				ultra.close();
				System.exit(0);
			}

		}

	}
}