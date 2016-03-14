package org.wheeler.robotics.lidarlite;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;
import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucieng on 2/25/16.
 */

@TeleOp()
public class LidarLiteWireTest extends LinearOpMode {
	Wire ls;
	long writeTime;
	int address=0x62;

	public void runOpMode() throws InterruptedException {
		this.ls = new Wire(hardwareMap, "lidar", address*2);

		waitForStart();

		while (opModeIsActive()) {
			ls.requestFrom(0x41, 1);
			Log.d("lidar", "responce count: " + ls.responseCount());
			if (ls.responseCount()>1){
				ls.getResponse();
				while (!ls.isRead() && opModeIsActive()){
					Log.d("lidar", "read mode: " + ls.isRead());
				}
				Log.d("lidar", "Version Number: " + String.valueOf(ls.read()));
			}
		}

		//ls.disengage();
		ls.close();
	}
}
