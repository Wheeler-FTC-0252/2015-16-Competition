package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

/**
 * Created by lucien on 11/25/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class UltrasoundSensor4Test extends OpMode {
    final int I2C_ADDRESS=0x70;
    final int WRITE_ADDRESS=224;
    final int READ_ADDRESS=225;
    final byte WRITE_VALUE=81;

    byte[] readCache;
    Lock readLock;
    byte[] readData;
    byte[] writeCache;
    Lock writeLock;
    int port=0;
    DeviceInterfaceModule dim;

    public void init() {
        dim = hardwareMap.deviceInterfaceModule.get("dim");

        readCache = dim.getI2cReadCache(port);
        readLock = dim.getI2cReadCacheLock(port);
        writeCache = dim.getI2cWriteCache(port);
        writeLock = dim.getI2cWriteCacheLock(port);
    }

    public void loop() {
        performAction("write", port, I2C_ADDRESS, WRITE_ADDRESS, 1);
        try {
            writeLock.lock();
            writeCache[4] = (byte) WRITE_VALUE;
            /*for (int ii = 0; ii<writeCache.length; ii++){
                telemetry.addData("value " + ii, writeCache[ii]);
            }*/
        } finally {
            writeLock.unlock();
        }
        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep", "failed");
        }

        performAction("read", port, I2C_ADDRESS, READ_ADDRESS, 2);
        dim.readI2cCacheFromController(port);
        readLock.lock();
        readData=Arrays.copyOfRange(readCache,4,6);
        telemetry.addData("distance", TypeConversion.byteArrayToShort(readData));
        for (int ii = 0; ii<readCache.length; ii++){
            telemetry.addData("value " + ii, readCache[ii]);
        }
        readLock.unlock();
    }

    private void performAction(String actionName, int port, int i2cAddress, int memAddress, int memLength) {
        if (actionName.equalsIgnoreCase("read")) dim.enableI2cReadMode(port, i2cAddress, memAddress, memLength);
        if (actionName.equalsIgnoreCase("write")) dim.enableI2cWriteMode(port, i2cAddress, memAddress, memLength);

        dim.setI2cPortActionFlag(port);
        dim.writeI2cCacheToController(port);
        dim.readI2cCacheFromController(port);
    }
}
