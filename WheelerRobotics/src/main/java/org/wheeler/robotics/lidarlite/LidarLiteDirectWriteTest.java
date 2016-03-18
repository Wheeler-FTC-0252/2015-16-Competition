package org.wheeler.robotics.lidarlite;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;

import org.swerverobotics.library.interfaces.TeleOp;

import java.util.concurrent.locks.Lock;

/**
 * Created by lucieng on 3/13/16.
 * @see com.qualcomm.ftcrobotcontroller.opmodes.LinearI2cAddressChange
 */

@TeleOp()
public class LidarLiteDirectWriteTest extends LinearOpMode {
    // The port where your sensor is connected.
    int port = 0;

    byte[] readCache;
    Lock readLock;
    byte[] writeCache;
    Lock writeLock;

    //Blinkm i2c address (multiplied by 2 to convert to 8 bit)
    int currentAddress = 0x62*2;

    //Blinkm Memory Address that stores the i2c address
    int memoryAddress = 0x0;

    //Length of data that needs to be returned
    int dataLength=1;

    // I2c addresses on Modern Robotics devices must be divisible by 2, and between 0x7e and 0x10
    // Different hardware may have different rules.
    // Be sure to read the requirements for the hardware you're using!
    // If you use an invalid address, you may make your device completely unusable.

    I2cDevice lidar;

    @Override
    public void runOpMode() throws InterruptedException {

        // set up the hardware devices we are going to use
        lidar = hardwareMap.i2cDevice.get("lidar");

        readCache = lidar.getI2cReadCache();
        readLock = lidar.getI2cReadCacheLock();
        writeCache = lidar.getI2cWriteCache();
        writeLock = lidar.getI2cWriteCacheLock();

        // wait for the start button to be pressed
        waitForStart();
        Log.d("cacheValues", "\n\n==========================OPMODE START==========================");

        // Start timing here
        long startTime = System.nanoTime();

        while(!lidar.isI2cPortReady()) {
            Log.d("cacheValues", "waiting for the port to be ready...");
            sleep(1000);
        }

        try {
            writeLock.lock();
            writeCache[4] = 0x4;
        } finally {
            writeLock.unlock();
        }

        //Print values from writeCache
        try {
            writeLock.lock();
            Log.d("cacheValues", "cache length: " + writeCache.length);
            for (byte ii = 0; ii < writeCache.length; ii++) {

                //Limit to first 8 and last values
                if(ii<=7 || ii>=31) {
                    Log.d("cacheValues", "cache value " + ii + ": " + writeCache[ii]);
                }
            }
        } finally {
            writeLock.unlock();
        }

        performAction("write", currentAddress, memoryAddress, dataLength);

        // Look at timer to see how long the operation took
        Log.d("cacheValues", "It took " + (System.nanoTime()-startTime) + "ns");

        lidar.close();
        Log.d("cacheValues", "\n" + " ==========================ENDS==========================\n\n");
    }

    // From LinearI2cAddressChange - made by MR
    private void performAction(String actionName, int i2cAddress, int memAddress, int memLength) {
        if (actionName.equalsIgnoreCase("read")) lidar.enableI2cReadMode(i2cAddress, memAddress, memLength);
        if (actionName.equalsIgnoreCase("write")) lidar.enableI2cWriteMode(i2cAddress, memAddress, memLength);

        lidar.setI2cPortActionFlag();
        lidar.writeI2cCacheToController();
        lidar.readI2cCacheFromController();
    }
}
