package org.wheeler.robotics.treadBot;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.library.ultrasoundSensor.MaxSonarSensor;

import java.text.DecimalFormat;

/**
 * Created by lucien on 1/18/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class TreadBotGuided extends OpMode {
	double OPTIMAL_RATIO = 214./325.; // 214/325 (L / R)
	double MOTOR_GAIN = 1;
	double MAX_CHANGE = 10;

	MaxSonarSensor dsLeft;
	int previousLeft;
	MaxSonarSensor dsRight;
	int previousRight;
	DecimalFormat decimalFormatter;
	DecimalFormat intFormatter;
	long readTime;

	public void init() {
		dsLeft = new MaxSonarSensor(hardwareMap, "dsLeft", 0xE0);
		previousLeft = dsLeft.getDistance();
		dsRight = new MaxSonarSensor(hardwareMap, "dsRight", 0xE6);
		previousRight = dsLeft.getDistance();

		decimalFormatter = new DecimalFormat("0.000");
		intFormatter = new DecimalFormat("000");
		readTime = System.currentTimeMillis();
	}

	public void loop() {
		int leftDistance = dsLeft.getDistance();
		/*if (Math.abs(leftDistance - previousLeft) > MAX_CHANGE){
			leftDistance = previousLeft;
		}*/


		int rightDistance = dsRight.getDistance();
		/*if (Math.abs(leftDistance - previousLeft) > MAX_CHANGE){
			leftDistance = previousLeft;
		}*/

		double dsRatio = (double) leftDistance / (double) rightDistance;
		double currentRatio = OPTIMAL_RATIO / dsRatio;
		double leftSpeed = currentRatio*MOTOR_GAIN;
		double rightSpeed = (1/currentRatio)*MOTOR_GAIN;

		if (System.currentTimeMillis()-readTime > 1000 ) {
			//Log.d("TBAutoStatus", "--------------------NEW----------------");

			telemetry.addData("leftDistance", leftDistance);
			//Log.d("TBAutoStatus", "leftDistance == " + String.valueOf(leftDistance));

			telemetry.addData("rightDistance", rightDistance);
			//Log.d("TBAutoStatus", "rightDistance == " + String.valueOf(rightDistance));

			telemetry.addData("ratio", dsRatio);
			//Log.d("TBAutoStatus", "dsRatio == " + String.valueOf(dsRatio));

			telemetry.addData("optimalRatio / ratio", OPTIMAL_RATIO / dsRatio);
			//Log.d("TBAutoStatus", "optimalRatio / dsRatio == " + String.valueOf(currentRatio));

			telemetry.addData("leftSpeed", leftSpeed);
			//Log.d("TBAutoStatus", "leftSpeed == " + String.valueOf(leftSpeed));

			telemetry.addData("rightSpeed", rightSpeed);
			//Log.d("TBAutoStatus", "rightSpeed == " + String.valueOf(rightSpeed));

			Log.d("TBAutoStatus", String.format("%03d", leftDistance) + ", "
					+ String.format("%03d", rightDistance) + ", "
					+ String.valueOf(decimalFormatter.format(currentRatio)) + ", "
					+ String.valueOf(decimalFormatter.format(leftSpeed)) + ", "
					+ String.valueOf(decimalFormatter.format(rightSpeed)));
			readTime = System.currentTimeMillis();
		}
	}
}
