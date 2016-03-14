package org.wheeler.robotics.MaxSonar;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Created by lucieng on 3/6/16.
 */

@TeleOp()
public class MaxSonarTest extends LinearOpMode {
    I2cDeviceSynch ds;
    int address = 0x70;

    public void runOpMode() throws InterruptedException {
        ds = ClassFactory.createI2cDeviceSynch(hardwareMap.i2cDevice.get("lidar"), address*2);
        waitForStart();
        ds.engage();

        while (this.opModeIsActive()) {
            ds.write8(224, 81);
            sleep(50);
            byte[] data = ds.read(225, 2);

            Log.d("lidar", "Distance: " + String.valueOf((short) ((data[0] << 8) | (data[1] & 0xFF)))
                    + ", Data 0: " + String.valueOf(data[0])
                    + ", Data 1: " + String.valueOf(data[1]));

            sleep(100);
        }
        ds.disengage();
        ds.close();
    }
}
