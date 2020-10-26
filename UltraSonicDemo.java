package lejos;

import ev3.exercises.library.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
 
import lejos.hardware.port.SensorPort;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class UltraSonicDemo {
	MovePilot pilot;

	public static void main(String[] args) {
		float range;
		UltraSonicSensor uss = new UltraSonicSensor(SensorPort.S4);

		System.out.println("Press any key to start");

		Button.LEDPattern(4); // flash green led and
		Sound.beepSequenceUp(); // make sound when ready.

		Button.waitForAnyPress();
		range = uss.getRange();

		Lcd.print(7, "range=");
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A,2.8).offset(-10.0);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 2.8).offset(10.0);
		Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		UltraSonicDemo usd = new UltraSonicDemo();
		usd.pilot = new MovePilot(myChassis);

		 

		while (range > .25 && Button.ESCAPE.isUp()) {
			//			usd.pilot.setLinearSpeed(20);
			 
			if( range> 0.25) {
				usd.pilot.travel(10);
				range = uss.getRange();
			}
		 
      

			range = uss.getRange();
			Lcd.print(3, "Press Escape twice to Stop");
			Lcd.print(7, 7, "%.3f", range);

			while (range < 0.25 && Button.ESCAPE.isUp()) {
			
				usd.pilot.rotate(-35);
				range = uss.getRange();

				if (range < 0.25 && Button.ESCAPE.isUp()) {
					usd.pilot.rotate(70);
					range = uss.getRange();

				}

			}

		}

		// free up resources.
		uss.close();

		Sound.beepSequence(); // we are done.

		//		Button.waitForAnyPress();
	}
}