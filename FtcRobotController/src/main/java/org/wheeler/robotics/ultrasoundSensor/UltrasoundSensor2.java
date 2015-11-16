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
        //this.lModule.enableI2cWriteMode(this.port, I2C_ADDRESS, WRITE_ADDRESS, 1);
        try {
            this.writeLock.lock();
            this.writeCache[4] = (byte)WRITE_VALUE;
        } finally {
            this.writeLock.unlock();
        }
    }

    /*
    private void enableDataRead() {
        this.lModule.enableI2cReadMode(this.port, I2C_ADDRESS, READ_ADDRESS, 2);
    }
    */

    private byte[] readData() {
        byte[] readVal;
        try {
            this.readLock.lock();
            readVal = Arrays.copyOfRange(this.readCache, 4, 6);
        } finally {
            this.readLock.unlock();
        }
        return readVal;
    }

    public byte[] readArray() throws Exception {
        performAction("write", this.port, this.I2C_ADDRESS, this.WRITE_ADDRESS, 1);
        startMeasurement();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e){
            throw new Exception("Sleep thread failed");
        }

        performAction("read",this.port,this.I2C_ADDRESS, this.READ_ADDRESS, 2);
        return readData();
    }

    public short readRaw() throws Exception {
        byte[] data = readArray();
        return TypeConversion.byteArrayToShort(data, ByteOrder.LITTLE_ENDIAN);
    }

    private void performAction(String actionName, int port, int i2cAddress, int memAddress, int memLength) {
        if (actionName.equalsIgnoreCase("read")) this.lModule.enableI2cReadMode(port, i2cAddress, memAddress, memLength);
        if (actionName.equalsIgnoreCase("write")) this.lModule.enableI2cWriteMode(port, i2cAddress, memAddress, memLength);

        this.lModule.setI2cPortActionFlag(port);
        this.lModule.writeI2cCacheToController(port);
        this.lModule.readI2cCacheFromController(port);
    }

    public void portIsReady(int port) {
        this.lModule.setI2cPortActionFlag(port);
        this.lModule.readI2cCacheFromController(port);
        this.lModule.writeI2cPortFlagOnlyToController(port);
    }

    public byte[] getReadCache(){
        return readCache;
    }
}
