package org.wheeler.robotics.lidarlite;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;

import java.util.concurrent.locks.Lock;

/**
 * Created by lucieng on 3/13/16.
 * @see com.qualcomm.ftcrobotcontroller.opmodes.LinearI2cAddressChange
 */

@TeleOp()
public class LidarLiteDirectBothReadTest extends LinearOpMode {
    I2cDeviceSynch lidar;

    //Lidar i2c address (multiplied by 2 to convert to 8 bit)
    int i2cAddress = 0x62*2;

    @Override
    public void runOpMode() throws InterruptedException {
        // set up the hardware devices we are going to use
        lidar = ClassFactory.createI2cDeviceSynch(hardwareMap.i2cDevice.get("lidar"), i2cAddress);
        waitForStart();
        lidar.write8(0x0, 0x0, true);
        sleep(1);
        Log.d("lidar", "value[0]=" + lidar.read8(0xf));
        sleep(1);
        Log.d("lidar", "value[1]=" + lidar.read8(0x10));
    }
}
