package org.wheeler.robotics;

import android.util.Log;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

import org.swerverobotics.library.interfaces.TeleOp;

import java.util.concurrent.locks.Lock;

/**
 * Created by lucieng on 3/13/16.
 * @see com.qualcomm.ftcrobotcontroller.opmodes.LinearI2cAddressChange
 */

@TeleOp()
public class BlinkMDirectReadTest extends LinearOpMode {
    // The port where your sensor is connected.
    int port = 0;

    byte[] readCache;
    Lock readLock;
    byte[] writeCache;
    Lock writeLock;

    //Blinkm i2c address (multiplied by 2 to convert to 8 bit)
    int currentAddress = 0x9*2;

    //Blinkm Memory Address that stores the i2c address
    int memoryAddress = 0x61;

    //Length of data that needs to be returned
    int dataLength=1;

    // I2c addresses on Modern Robotics devices must be divisible by 2, and between 0x7e and 0x10
    // Different hardware may have different rules.
    // Be sure to read the requirements for the hardware you're using!
    // If you use an invalid address, you may make your device completely unusable.

    DeviceInterfaceModule dim;

    @Override
    public void runOpMode() throws InterruptedException {

        // set up the hardware devices we are going to use
        dim = hardwareMap.deviceInterfaceModule.get("dim");

        readCache = dim.getI2cReadCache(port);
        readLock = dim.getI2cReadCacheLock(port);
        writeCache = dim.getI2cWriteCache(port);
        writeLock = dim.getI2cWriteCacheLock(port);

        // wait for the start button to be pressed
        waitForStart();
        Log.d("cacheValues", "\n\n==========================OPMODE START==========================");

        // Start timing here
        long startTime = System.nanoTime();

        performAction("read", port, currentAddress, memoryAddress, dataLength);

        while(!dim.isI2cPortReady(port)) {
            telemetry.addData("I2cAddressChange", "waiting for the port to be ready...");
            sleep(1000);
        }


        // Loops until data has been loaded successfuly into readCache
        int count = 0;
        while (count<100000 && this.opModeIsActive()) {
            count++;
            dim.readI2cCacheFromController(port);
            try {
            readLock.lock();
            if(readCache[0] != 0) {break;} // Cache has been updated, slave has written values
            } finally {
                readLock.unlock();
            }
        }

        // Look at timer to see how long the operation took
        Log.d("cacheValues", "It took " + (System.nanoTime()-startTime) + "ns");

        Log.d("cacheValues", "\n" + count + " ==========================NEW READ==========================");


        //Print values from readCache
        try {
            readLock.lock();
            Log.d("cacheValues", "cache length: " + readCache.length);
            for (byte ii = 0; ii < readCache.length; ii++) {

                //Limit to first 8 and last values
                if(ii<=7 || ii>=31) {
                    Log.d("cacheValues", "cache value " + ii + ": " + readCache[ii]);
                }
            }
        } finally {
            readLock.unlock();
        }


        Log.d("cacheValues", "\n" + " ==========================ENDS==========================\n\n");
    }

    // From LinearI2cAddressChange - made by MR
    private void performAction(String actionName, int port, int i2cAddress, int memAddress, int memLength) {
        if (actionName.equalsIgnoreCase("read")) dim.enableI2cReadMode(port, i2cAddress, memAddress, memLength);
        if (actionName.equalsIgnoreCase("write")) dim.enableI2cWriteMode(port, i2cAddress, memAddress, memLength);

        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);
        dim.readI2cCacheFromController(port);
    }
}
