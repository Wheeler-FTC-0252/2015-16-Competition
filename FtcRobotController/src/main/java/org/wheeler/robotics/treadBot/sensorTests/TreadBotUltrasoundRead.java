package org.wheeler.robotics.treadBot.sensorTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by lucien on 1/26/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class TreadBotUltrasoundRead extends OpMode {
	boolean filter = true;
	UltrasonicSensor dsBack;

	public void init() {
		dsBack = hardwareMap.ultrasonicSensor.get("dsBack");
	}

	public void loop() {
		double value = dsBack.getUltrasonicLevel();

		if (value != 0.0 || !filter){
			telemetry.addData("cm", value);
		}
		else {
			telemetry.addData("filtering", "yes");
		}
	}
}
