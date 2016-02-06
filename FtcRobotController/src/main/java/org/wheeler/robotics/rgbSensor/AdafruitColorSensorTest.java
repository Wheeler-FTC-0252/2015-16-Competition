package org.wheeler.robotics.rgbSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by lucien on 1/24/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class AdafruitColorSensorTest extends OpMode {
	ColorSensor cs;

	public void init() {
		cs = hardwareMap.colorSensor.get("cs");
	}

	public void loop() {
		telemetry.addData("red", cs.red());
		telemetry.addData("green", cs.green());
		telemetry.addData("blue", cs.blue());
	}
}
