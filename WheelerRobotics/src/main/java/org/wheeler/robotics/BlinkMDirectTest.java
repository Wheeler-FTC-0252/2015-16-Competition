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
 */

@TeleOp()
public class BlinkMDirectTest extends LinearOpMode {
    // These byte values are common with most Modern Robotics sensors.
    public static final int READ_MODE = 0x80;
    public static final int ADDRESS_MEMORY_START = 0x0;
    public static final int TOTAL_MEMORY_LENGTH = 0x0c;
    public static final int BUFFER_CHANGE_ADDRESS_LENGTH = 0x03;

    // The port where your sensor is connected.
    int port = 0;

    byte[] readCache;
    Lock readLock;
    byte[] writeCache;
    Lock writeLock;

    //Blinkm i2c address
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

        //ModernRoboticsUsbDeviceInterfaceModule.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);

        // wait for the start button to be pressed
        waitForStart();
        Log.d("cacheValues", "\n\n==========================OPMODE START==========================");

        // *** Start a clock here
        long startTime = System.nanoTime();

        performAction("read", port, currentAddress, memoryAddress, dataLength);

        while(!dim.isI2cPortReady(port)) {
            telemetry.addData("I2cAddressChange", "waiting for the port to be ready...");
            sleep(1000);
        }




        // update the local cache
        int count = 0;
        while (count<100000 && this.opModeIsActive()) {
            count++;
            dim.readI2cCacheFromController(port);
            try {
            readLock.lock();
            if(readCache[0] != 0) {break;} // Cache has been modified, slave has written values
            } finally {
                readLock.unlock();
            }
        }

        // *** End a clock here to see how long the operation took
        Log.d("cacheValues", "It took " + (System.nanoTime()-startTime) + "ns");

        Log.d("cacheValues", "\n" + count + " ==========================NEW READ==========================");


        try {
            readLock.lock();
            Log.d("cacheValues", "cache length: " + readCache.length);
            for (byte ii = 0; ii < readCache.length; ii++) {
                if(ii<=7 || ii>=31) {
                    Log.d("cacheValues", "cache value " + ii + ": " + readCache[ii]);
                }
            }
        } finally {
            readLock.unlock();
        }


        Log.d("cacheValues", "\n" + " ==========================ENDS==========================\n\n");


        // make sure the first bytes are what we think they should be.
        /*int count = 0;
        int[] initialArray = {READ_MODE, currentAddress, ADDRESS_MEMORY_START, TOTAL_MEMORY_LENGTH, FIRMWARE_REV, MANUFACTURER_CODE, SENSOR_ID};
        while (!foundExpectedBytes(initialArray, readLock, readCache)) {
            telemetry.addData("I2cAddressChange", "Confirming that we're reading the correct bytes...");
            dim.readI2cCacheFromController(port);
            sleep(1000);
            count++;
            // if we go too long with failure, we probably are expecting the wrong bytes.
            if (count >= 10)  {
                telemetry.addData("I2cAddressChange", String.format("Looping too long with no change, probably have the wrong address. Current address: 0x%02x", currentAddress));
                hardwareMap.irSeekerSensor.get(String.format("Looping too long with no change, probably have the wrong address. Current address: 0x%02x", currentAddress));
            }
        }*/

        // Enable writes to the correct segment of the memory map.
        //performAction("write", port, currentAddress, memoryAddress, dataLength);

        /*waitOneFullHardwareCycle();

        // Write out the trigger bytes, and the new desired address.
        writeNewAddress();
        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);

        telemetry.addData("I2cAddressChange", "Giving the hardware some time to make the change...");

        // Changing the I2C address takes some time.
        for (int i = 0; i < 5000; i++) {
            waitOneFullHardwareCycle();
        }

        // Query the new address and see if we can get the bytes we expect.
        dim.enableI2cReadMode(port, newAddress, ADDRESS_MEMORY_START, TOTAL_MEMORY_LENGTH);
        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);

        int[] confirmArray = {READ_MODE, newAddress, ADDRESS_MEMORY_START, TOTAL_MEMORY_LENGTH, FIRMWARE_REV, MANUFACTURER_CODE, SENSOR_ID};
        while (!foundExpectedBytes(confirmArray, readLock, readCache)) {
            telemetry.addData("I2cAddressChange", "Have not confirmed the changes yet...");
            dim.readI2cCacheFromController(port);
            sleep(1000);
        }

        telemetry.addData("I2cAddressChange", "Successfully changed the I2C address." + String.format("New address: 0x%02x", newAddress));
        RobotLog.i("Successfully changed the I2C address." + String.format("New address: 0x%02x", newAddress));*/

        /**** IMPORTANT NOTE ******/
        // You need to add a line like this at the top of your op mode
        // to update the I2cAddress in the driver.
        //irSeeker.setI2cAddress(newAddress);
        /***************************/

    }

    /*
    private boolean foundExpectedBytes(int[] byteArray, Lock lock, byte[] cache) {
        try {
            lock.lock();
            boolean allMatch = true;
            StringBuilder s = new StringBuilder(300 * 4);
            String mismatch = "";
            for (int i = 0; i < byteArray.length; i++) {
                s.append(String.format("expected: %02x, got: %02x \n", TypeConversion.unsignedByteToInt((byte) byteArray[i]), cache[i]));
                if (TypeConversion.unsignedByteToInt(cache[i]) != TypeConversion.unsignedByteToInt( (byte) byteArray[i])) {
                    mismatch = String.format("i: %d, byteArray[i]: %02x, cache[i]: %02x", i, byteArray[i], cache[i]);
                    allMatch = false;
                }
            }
            RobotLog.e(s.toString() + "\n allMatch: " + allMatch + ", mismatch: " + mismatch);
            return allMatch;
        } finally {
            lock.unlock();
        }
    }
    */

    // From MR - standard function
    private void performAction(String actionName, int port, int i2cAddress, int memAddress, int memLength) {
        if (actionName.equalsIgnoreCase("read")) dim.enableI2cReadMode(port, i2cAddress, memAddress, memLength);
        if (actionName.equalsIgnoreCase("write")) dim.enableI2cWriteMode(port, i2cAddress, memAddress, memLength);

        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);
        dim.readI2cCacheFromController(port);
    }

    /*private void writeNewAddress() {
        try {
            writeLock.lock();
            writeCache[4] = (byte) newAddress;
            writeCache[5] = TRIGGER_BYTE_1;
            writeCache[6] = TRIGGER_BYTE_2;
        } finally {
            writeLock.unlock();
        }
    }*/
}
