package org.wheeler.robotics.BlinkM;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.II2cDeviceClient;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Created by lucieng on 3/5/16.
 */

@TeleOp()
public class BlinkMTest extends LinearOpMode {
    I2cDeviceSynch led;
    int address=0x09;

    public void runOpMode() throws InterruptedException {
        led = ClassFactory.createI2cDeviceSynch(hardwareMap.i2cDevice.get("lidar"), address*2);

        waitForStart();
        led.engage();
        waitOneFullHardwareCycle();
        Log.d("lidar", "LED Address: " + led.read8(0x61));
        led.write8(0x6f, 0);
        waitOneFullHardwareCycle();
        led.write(0x6e, new byte[]{100, 100, 5});
        waitOneFullHardwareCycle();
        Log.d("lidar", "Red: " + led.read(0x67,3)[0]
            + ", Green: " + led.read(0x67,3)[1]
            + ", Blue: " + led.read(0x67,3)[2]);
        while (this.opModeIsActive()) {}
        led.disengage();
        led.close();
    }
}
