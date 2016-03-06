package org.wheeler.robotics;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.II2cDeviceClient;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Created by lucieng on 3/5/16.
 */

@TeleOp()
public class BlinkMTest extends LinearOpMode {
    II2cDeviceClient led;
    int address=0x09;

    public void runOpMode() throws InterruptedException {
        led = ClassFactory.createI2cDeviceClient(this, hardwareMap.i2cDevice.get("lidar"), address*2, false);
        led.engage();
        waitOneFullHardwareCycle();
        Log.d("lidar", "LED Address: " + led.read8(0x61));
        waitForStart();
        led.write8(0x6f, 0);
        waitOneFullHardwareCycle();
        while (this.opModeIsActive()) {
            led.write(0x6e, new byte[]{5, 5, 5});
        }
        led.disengage();
        led.close();
    }
}
