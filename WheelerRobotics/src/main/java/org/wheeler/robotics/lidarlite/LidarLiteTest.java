package org.wheeler.robotics.lidarlite;

import android.util.Log;

import  com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
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
	I2cDeviceSynch ls;
	long writeTime;
	int address=0x62;

	public void runOpMode() throws InterruptedException {
		this.ls = ClassFactory.createI2cDeviceSynch(hardwareMap.i2cDevice.get("lidar"), address*2);

		waitForStart();
		ls.engage();
		sleep(1000);

		Log.d("lidar", "Version Number: " + String.valueOf(ls.read(0x41, 2)[0]));
		Log.d("lidar", "Status: " + String.valueOf(ls.read(0x1, 2)[0]));
		Log.d("lidar", "I2c Address: " + String.valueOf(ls.getI2cAddr()));

		while (this.opModeIsActive()) {
			/*ls.write8(0x00, 0x04);
			sleep(20);
			byte[] value= new byte[] {ls.read8(0x0f),ls.read8(0x10)};
			Log.d("lidar", "length: " + String.valueOf(value.length)
					+ ", distance0: " + String.valueOf(value[0])
					+ ", distance1: " + String.valueOf(value[1])
					+ ", distance: " + String.valueOf((short) ((value[0] << 8) | (value[1] & 0xFF))));
			telemetry.addData("distanceLength", value.length);
			telemetry.addData("distance0", value[0]);
			telemetry.addData("distance1", value[1]);
			*/

			sleep(100);
			//TODO: try reading version number
			//telemetry.addData("distance", TypeConversion.byteArrayToInt(ls.read(0x0f,2)));
		}

		ls.disengage();
		ls.close();
	}
}
