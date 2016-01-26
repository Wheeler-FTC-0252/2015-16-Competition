package org.wheeler.robotics.treadBot.sensorTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by lucien on 1/26/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class TreadBotRGBRead extends OpMode {
	ColorSensor colorLeft;
	ColorSensor colorRight;
	ColorSensor colorMiddle;

	public void init() {
		colorLeft = hardwareMap.colorSensor.get("colorLeft");
		colorLeft.setI2cAddress(0x42);

		colorRight = hardwareMap.colorSensor.get("colorRight");
		colorRight.setI2cAddress(0x3c);

		colorMiddle = hardwareMap.colorSensor.get("colorMiddle");
	}

	public void loop() {
		//telemetry.addData("LeftAddress", colorLeft.getI2cAddress());
		telemetry.addData("LeftAlpha", colorLeft.alpha());
		telemetry.addData("LeftRed", colorLeft.red());
		telemetry.addData("LeftGreen", colorLeft.green());
		telemetry.addData("LeftBlue", colorLeft.blue());

		telemetry.addData("RightAlpha", colorRight.alpha());
		telemetry.addData("RightRed", colorRight.red());
		telemetry.addData("RightGreen", colorRight.green());
		telemetry.addData("RightBlue", colorRight.blue());

		telemetry.addData("middleAlpha", colorMiddle.alpha());
		telemetry.addData("middleRed", colorMiddle.red());
		telemetry.addData("middleGreen", colorMiddle.green());
		telemetry.addData("middleBlue", colorMiddle.blue());
	}
}
