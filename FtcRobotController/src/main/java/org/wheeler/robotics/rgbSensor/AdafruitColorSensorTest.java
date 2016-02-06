package org.wheeler.robotics.rgbSensor;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.AdafruitRGBExample;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucien on 1/24/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */

public class AdafruitColorSensorTest extends OpMode {
	Wire cs;
	public void init() {
		cs = new Wire(hardwareMap, "cs", 0x29);
	}

	public void start() {
		Log.d("ACSensorStatus", "Request Version from Color Sensor");

		cs.requestFrom(0x80 | 0x12, 1);
		while(cs.responseCount() < 1) {
			Log.d("ACSensorStatus", "Waiting for response (" + String.valueOf(cs.responseCount()) + ")");
		}

		cs.getResponse();

		while (!cs.isRead()) {
			cs.getResponse();
			Log.d("ACSensorStatus", "Not Read Mode");
		}

		Log.d("ACSensorStatus", "Reading from Color Sensor");

		int ver = cs.read();
		if (ver == 0x44){
			Log.d("ACSensorStatus", "DEVICE FOUND");
			cs.beginWrite(0x80 | 0x00);
			cs.write(0x01 | 0x02);
			cs.endWrite();

			cs.beginWrite(0x80 | 0x14);
			cs.endWrite();
		}
		else {
			Log.d("ACSensorStatus", "DEVICE FAILED (" + String.valueOf(ver) + ")");
		}

	}

	public void loop() {
		cs.requestFrom(0x17, 2);
		while(cs.responseCount() < 1) {}
		cs.getResponse();
		while (!cs.isRead()) {
			cs.getResponse();
		}
		Log.d("ACSensorStatus", "red: " + String.valueOf(cs.readHL()));

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
