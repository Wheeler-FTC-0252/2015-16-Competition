package org.wheeler.robotics.treadBot;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by lucien on 1/16/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */

public class TreadBotAutonomous extends LinearOpMode {
	//------------------------------CONSTANTS
	//DRIVE
	double ENCODER_MULTIPLIER = 10000.0/90.0;

	//  DRIVE
	private double INITIAL_SPEED = 1.0;
	private double INITIAL_DISTANCE = 220.0 * ENCODER_MULTIPLIER;
	private double SLOW_SPEED = 0.15;
	private double SLOW_DISTANCE = 80.0 * ENCODER_MULTIPLIER;
	private double LEFT_DRIVE_GAIN = 0.83; // Balance real speeds of motors

	//  COLOR SENSOR
	double MIDDLE_RED_BOUND = 3400;
	double MIDDLE_GREEN_BOUND = 5300;
	double MIDDLE_BLUE_BOUND = 5000;

	double LEFT_RED_BOUND = 6;
	double LEFT_GREEN_BOUND = 6;
	double LEFT_BLUE_BOUND = 6;

	double RIGHT_RED_BOUND = 6;
	double RIGHT_GREEN_BOUND = 6;
	double RIGHT_BLUE_BOUND = 6;



	//  ROTATE DRIVE
	int ROTATE_MAX_DISTANCE = 100;
	double ROTATE_SPEED = 0.75;

	//------------------------------OTHER
	//DRIVE
	DcMotor leftDrive;
	DcMotor rightDrive;

	//COLOR SENSORS
	ColorSensor colorLeft;
	double[] colorLeftValues;
	double colorLeftWhite;
	double[] colorLeftInitial;

	ColorSensor colorRight;
	double[] colorRightValues;
	double colorRightWhite;
	double[] colorRightInitial;

	ColorSensor colorMiddle;
	double[] colorMiddleValues;
	double colorMiddleWhite;
	double[] colorMiddleInitial;

	//  SHORT DISTANCE SENSORS
	UltrasonicSensor dsBack;

	// ARM
	DcMotor armRaiseMotor;
	double armRaiseSpeed = 0.5;
	double armRaisePosition = -15300;
	DcMotor armRotateMotor;
	double armRotateSpeed = 0.5;
	double armRotatePosition = -6110;

	//RELEASE
	Servo releaseLeftServo;
	Servo releaseRightServo;
	double releaseServoStart = 0;
	double releaseServoExtended = 0.8;

	//PLOW
	double plowExtended=0.95;
	Servo leftPlow;
	Servo rightPlow;


	private void motorEncoderPosition(){
		Log.d("TBAutoStatus", "ml=" + String.format("%16s", leftDrive.getCurrentPosition())
				+ ", " + "mr=" + String.format("%16s", rightDrive.getCurrentPosition()));
		telemetry.addData("motorLeft", leftDrive.getCurrentPosition());
		telemetry.addData("motorRight", rightDrive.getCurrentPosition());
	}


	private void readMiddleColor(){
		double red = colorMiddle.red() - colorMiddleInitial[0];
		double green = colorMiddle.green()  - colorMiddleInitial[1];
		double blue = colorMiddle.blue() - colorMiddleInitial[2];
		double intensity = red + green + blue;

		// Do we have a strong-enough reading?
		if (intensity > 300){
			colorMiddleValues = new double[] {red, green, blue};

			// Calculate color
			colorMiddleWhite = 1-(maxValue(colorMiddleValues) - minValue(colorMiddleValues))/maxValue(colorMiddleValues);
			// If it is greater than 0.4 than it is white
		}
		else
		{
			// White value -> black when insufficient intensity
			colorMiddleWhite = 0.;
		}
		Log.d("TBAutoStatus", "r1=" + String.format("%4s", red)
				+ ", " + "g1=" + String.format("%4s", green)
				+ ", " + "b1=" + String.format("%4s", blue)
				+ ", " + "w1=" + String.format("%1.1f", colorMiddleWhite));

		telemetry.addData("middleRed", red);
		telemetry.addData("middleGreen", green);
		telemetry.addData("middleBlue", blue);
		telemetry.addData("middleWhite", colorMiddleWhite);
	}

	private void readLeftColor(){
		double red = colorLeft.red() - colorLeftInitial[0];
		double green = colorLeft.green()  - colorLeftInitial[1];
		double blue = colorLeft.blue() - colorLeftInitial[2];
		double intensity = red + green + blue;

		// Do we have a strong-enough reading?
		if (intensity > 6){
			colorLeftValues = new double[] {red, green, blue};

			// Calculate color
			colorLeftWhite = 1-(maxValue(colorLeftValues) - minValue(colorLeftValues))/maxValue(colorLeftValues);
			// If it is greater than 0.4 than it is white
		}
		else
		{
			// White value -> black when insufficient intensity
			colorLeftWhite = 0.;
		}
		Log.d("TBAutoStatus", "r2=" + String.format("%4s", red)
				+ ", " + "g2=" + String.format("%4s", green)
				+ ", " + "b2=" + String.format("%4s", blue)
				+ ", " + "w2=" + String.format("%1.1f", colorLeftWhite));

		telemetry.addData("leftRed", red);
		telemetry.addData("leftGreen", green);
		telemetry.addData("leftBlue", blue);
		telemetry.addData("leftWhite", colorLeftWhite);
	}

	private void readRightColor(){
		double red = colorRight.red() - colorRightInitial[0];
		double green = colorRight.green()  - colorRightInitial[1];
		double blue = colorRight.blue() - colorRightInitial[2];
		double intensity = red + green + blue;

		// Do we have a strong-enough reading?
		if (intensity > 6){
			colorRightValues = new double[] {red, green, blue};

			// Calculate color
			colorRightWhite = 1-(maxValue(colorRightValues) - minValue(colorRightValues))/maxValue(colorRightValues);
			// If it is greater than 0.4 than it is white
		}
		else
		{
			// White value -> black when insufficient intensity
			colorRightWhite = 0.;
		}
		Log.d("TBAutoStatus", "r3=" + String.format("%4s", red)
				+ ", " + "g3=" + String.format("%4s", green)
				+ ", " + "b3=" + String.format("%4s", blue)
				+ ", " + "w3=" + String.format("%1.1f", colorRightWhite));

		telemetry.addData("leftRed", red);
		telemetry.addData("leftGreen", green);
		telemetry.addData("leftBlue", blue);
		telemetry.addData("leftWhite", colorRightWhite);
	}


	private static double maxValue(double[] values) {
		double max = values[0];
		for (int ii = 0; ii < values.length; ii++) {
			if (values[ii] > max) {
				max = values[ii];
			}
		}
		return max;
	}

	private static double minValue(double[] values) {
		double min = values[0];
		for (int ii = 0; ii < values.length; ii++) {
			if (values[ii] < min) {
				min = values[ii];
			}
		}
		return min;
	}

	public void runOpMode() throws InterruptedException {
		//DRIVE
		leftDrive = hardwareMap.dcMotor.get("left");
		leftDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		leftDrive.setDirection(DcMotor.Direction.FORWARD);

		rightDrive = hardwareMap.dcMotor.get("right");
		rightDrive.setDirection(DcMotor.Direction.REVERSE);
		rightDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		waitOneFullHardwareCycle();

		//COLOR SENSORS
		colorLeft = hardwareMap.colorSensor.get("colorLeft");
		colorRight = hardwareMap.colorSensor.get("colorRight");
		colorMiddle = hardwareMap.colorSensor.get("colorMiddle");

		//  SHORT DISTANCE SENSORS
		dsBack = hardwareMap.ultrasonicSensor.get("dsBack");

		// ARM
		armRaiseMotor = hardwareMap.dcMotor.get("armRaise");
		armRaiseMotor.setDirection(DcMotor.Direction.REVERSE);
		armRaiseMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		armRotateMotor = hardwareMap.dcMotor.get("armRotate");
		armRotateMotor.setDirection(DcMotor.Direction.REVERSE);
		armRotateMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		// RELEASE
		releaseLeftServo = hardwareMap.servo.get("releaseLeft");
		releaseLeftServo.setDirection(Servo.Direction.FORWARD);
		releaseRightServo = hardwareMap.servo.get("releaseRight");
		releaseRightServo.setDirection(Servo.Direction.REVERSE);
		releaseLeftServo.setPosition(releaseServoStart);
		releaseRightServo.setPosition(releaseServoStart);

		// PLOW
		leftPlow = hardwareMap.servo.get("plowLeft");
		leftPlow.setDirection(Servo.Direction.FORWARD);
		rightPlow = hardwareMap.servo.get("plowRight");
		rightPlow.setDirection(Servo.Direction.REVERSE);
		leftPlow.setPosition(plowExtended);
		rightPlow.setPosition(plowExtended);

		Log.d("TBAutoStatus", "Initialized variables");


		waitForStart();

		colorMiddleInitial = new double[]{colorMiddle.red(), colorMiddle.green(), colorMiddle.blue()};
		colorLeftInitial = new double[]{colorLeft.red(), colorLeft.green(), colorLeft.blue()};
		colorRightInitial = new double[]{colorRight.red(), colorRight.green(), colorRight.blue()};
		waitOneFullHardwareCycle();
		colorMiddleInitial = new double[]{colorMiddle.red(), colorMiddle.green(), colorMiddle.blue()};
		colorLeftInitial = new double[]{colorLeft.red(), colorLeft.green(), colorLeft.blue()};
		colorRightInitial = new double[]{colorRight.red(), colorRight.green(), colorRight.blue()};

		// ARM
		armRaiseMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		armRotateMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


		//------------------------------INITIAL DRIVE (not guided)
		Log.d("TBAutoStatus", "Started INITIAL DRIVE");
		//leftDrive.setPower(0);
		//leftDrive.setTargetPosition(INITIAL_DISTANCE);

		//rightDrive.setPower(0);
		//rightDrive.setTargetPosition(INITIAL_DISTANCE);

		// TURN ON ENCODERS FOR FEEDBACK
		leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		waitOneFullHardwareCycle();


		//
		// DRIVE FORWARD BY 180 cm AT HIGH SPEED - TO REACH THE EDGE OF THE BOX
		//
		leftDrive.setPower(INITIAL_SPEED * LEFT_DRIVE_GAIN);
		rightDrive.setPower(INITIAL_SPEED);

		while (leftDrive.getCurrentPosition() < INITIAL_DISTANCE && rightDrive.getCurrentPosition() < INITIAL_DISTANCE) {
			waitOneFullHardwareCycle(); // need this otherwise code can't stop
			readMiddleColor();
			motorEncoderPosition();
		}


		Log.d("TBAutoStatus", "Execute temporary stop");
		telemetry.addData("Execute temporary stop", "yes");

		// Temporary Stop
		leftDrive.setPower(0);
		rightDrive.setPower(0);

		//
		// Stop code for a moment
		//
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < 1000) {
			waitOneFullHardwareCycle();
		}

		// RESET DISTANCES ON MOTORS
	/*waitOneFullHardwareCycle();
	leftDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	rightDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	waitOneFullHardwareCycle();
	leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	waitOneFullHardwareCycle();*/

		Log.d("TBAutoStatus", "Reset Encoders");
		telemetry.addData("Reset Encoders", "yes");

		//
		// TRANSITION TO SLOW SPEED AS WE ENTER BOX
		// LOOK FOR WHITE LINE OR MAX OUT DISTANCE TRAVELLED
		//
		leftDrive.setPower(SLOW_SPEED);
		rightDrive.setPower(SLOW_SPEED);


		while (true) {
			readMiddleColor();
			motorEncoderPosition();

			if (colorMiddleWhite > 0.4) {
				Log.d("TBAutoStatus", "Color White >0.4");
				telemetry.addData("Color White >0.4", "yes");
				break;
			}
			if (leftDrive.getCurrentPosition() > INITIAL_DISTANCE + SLOW_DISTANCE ||
					rightDrive.getCurrentPosition() > INITIAL_DISTANCE + SLOW_DISTANCE) {
				Log.d("TBAutoStatus", "Exceeded max distance in box");
				telemetry.addData("Exceeded max distance in box", "yes");
				break;
			}

			waitOneFullHardwareCycle(); // need this otherwise code can't stop
		}

		Log.d("TBAutoStatus", "Finished slow section");
		telemetry.addData("Finished slow section", "yes");


		//
		// STOP
		//

		Log.d("TBAutoStatus", "Stop on line");
		telemetry.addData("Stop on line", "yes");

		motorEncoderPosition();
		waitOneFullHardwareCycle();
		leftDrive.setPower(0);
		rightDrive.setPower(0);

		startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < 1000) {
			waitOneFullHardwareCycle();
		}

		if (false) {
			//
			// ROTATE
			//
			leftDrive.setPower(-1 * ROTATE_SPEED);
			rightDrive.setPower(ROTATE_SPEED);
			while (true) {
				readLeftColor();
				readRightColor();
				motorEncoderPosition();
				if (colorLeftWhite > 0.4 || colorRightWhite > 0.4) {
					Log.d("TBAutoStatus", "Back Color White >0.4");
					telemetry.addData("Color White >0.4", "yes");
					break;
				}
				waitOneFullHardwareCycle(); // need this otherwise code can't stop
			}

			//
			// DEPOSIT
			//

			armRaiseMotor.setPower(armRaiseSpeed);
			while (armRaiseMotor.getCurrentPosition() < armRaisePosition) {
				telemetry.addData("rotating ar", "true");
				waitOneFullHardwareCycle();
			}
			armRaiseMotor.setPower(0);

			armRotateMotor.setPower(armRotateSpeed);
			while (armRotateMotor.getCurrentPosition() < armRotatePosition) {
				waitOneFullHardwareCycle();
			}
			armRotateMotor.setPower(0);
			//END
		}
	}
}