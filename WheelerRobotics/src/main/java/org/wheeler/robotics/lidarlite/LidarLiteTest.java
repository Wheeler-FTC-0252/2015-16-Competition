package org.wheeler.robotics.lidarlite;

import android.util.Log;

import  com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.TypeConversion;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.II2cDeviceClient;
import org.swerverobotics.library.interfaces.TeleOp;
import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucieng on 2/25/16.
 */

@TeleOp()
public class LidarLiteTest extends LinearOpMode {
	II2cDeviceClient ls;
	long writeTime;
	int address=0x62;

	public void runOpMode() throws InterruptedException {
		this.ls = ClassFactory.createI2cDeviceClient(this, ClassFactory.createI2cDevice(hardwareMap.i2cDevice.get("lidar")), address*2, false);

		waitForStart();
		ls.engage();
		waitOneFullHardwareCycle();

		Log.d("readValue", "Version Number: " + String.valueOf(ls.read8(0x41)));
		Log.d("readValue", "I2c Address: " + String.valueOf(ls.getI2cAddr()));

		while (this.opModeIsActive()) {
			ls.write8(0x00, 0x04);
			writeTime = System.currentTimeMillis();
			waitOneFullHardwareCycle();
			while (System.currentTimeMillis()-writeTime<1000 && this.opModeIsActive()) {}
			byte[] value=ls.read(0x10,2);
			Log.d("readValue", "length: " + String.valueOf(value.length)
					+ ", distance0: " + String.valueOf(value[0])
					+ ", distance1: " + String.valueOf(value[1]));
			telemetry.addData("distanceLength", value.length);
			telemetry.addData("distance0", value[0]);
			telemetry.addData("distance1", value[1]);

			waitOneFullHardwareCycle();
			//TODO: try reading version number
			//telemetry.addData("distance", TypeConversion.byteArrayToInt(ls.read(0x0f,2)));
		}

		ls.disengage();
		ls.close();
	}
}
