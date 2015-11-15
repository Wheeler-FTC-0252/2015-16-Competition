package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

/**
 * Created by lucien on 11/14/15.
 */
public class UltrasoundSensor2 implements I2cPortReadyCallback{
    private final byte[] readCache;
    private final Lock readLock;
    private final byte[] writeCache;
    private final Lock writeLock;
    private final int port;
    private final LegacyModule lModule;
    private final int I2C_ADDRESS=0x70;
    private final int WRITE_ADDRESS=224;
    private final int WRITE_VALUE=81;
    private final int READ_ADDRESS=225;


    public UltrasoundSensor2(LegacyModule legacyModule, int physicalPort) {
        this.lModule = legacyModule;
        this.readCache = legacyModule.getI2cReadCache(physicalPort);
        this.readLock = legacyModule.getI2cReadCacheLock(physicalPort);
        this.writeCache = legacyModule.getI2cWriteCache(physicalPort);
        this.writeLock = legacyModule.getI2cWriteCacheLock(physicalPort);
        this.port = physicalPort;
        legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
    }

    private void startMeasurement() {
        this.lModule.enableI2cWriteMode(this.port, I2C_ADDRESS, WRITE_ADDRESS, 1);

        try {
            this.writeLock.lock();
            this.writeCache[0] = (byte)WRITE_VALUE;
        } finally {
            this.writeLock.unlock();
        }
    }

    private void enableDataRead() {
        this.lModule.enableI2cReadMode(this.port, I2C_ADDRESS, READ_ADDRESS, 2);
    }

    private byte[] readData() {
        byte[] readVal;
        try {
            this.readLock.lock();
            readVal = Arrays.copyOf(this.readCache, this.readCache.length);
        } finally {
            this.readLock.unlock();
        }
        return readVal;
    }

    public short readRaw() throws Exception {
        startMeasurement();
        try {
            Thread.sleep(300);
        } catch(InterruptedException e){
            throw new Exception("Sleep thread failed");
        }
        enableDataRead();
        byte[] data = readData();
        return TypeConversion.byteArrayToShort(data, ByteOrder.LITTLE_ENDIAN);
    }

    public void portIsReady(int i) {}
}
