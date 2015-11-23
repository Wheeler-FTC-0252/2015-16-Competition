package org.wheeler.robotics.i2c;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
/*
Short/Byte conversions where helped by:
http://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vise-versa
*/

/**
 * Created by lucien on 11/6/15.
 */
public class LegacyModuleI2cDevice implements I2cController.I2cPortReadyCallback {
    private final byte[] readCache;
    private final Lock readLock;
    private final byte[] writeCache;
    private final Lock writeLock;
    private final I2cDevice i2cDevice;
    private final int i2cAddress;
    private final int IO_START = 4;

    public LegacyModuleI2cDevice(HardwareMap hardwareMap, String deviceName, int i2cAddress) {
        this.i2cDevice =  hardwareMap.i2cDevice.get(deviceName);
        this.readCache = i2cDevice.getI2cReadCache();
        this.readLock = i2cDevice.getI2cReadCacheLock();
        this.writeCache = i2cDevice.getI2cWriteCache();
        this.writeLock = i2cDevice.getI2cWriteCacheLock();
        this.i2cAddress=i2cAddress;
        i2cDevice.registerForI2cPortReadyCallback(this);
    }

    public void writeData(int address, byte[] data) {
        this.i2cDevice.enableI2cWriteMode(i2cAddress, address, data.length);

        try {
            this.writeLock.lock();
            for(byte ii=0; ii<data.length; ii++){
                this.writeCache[ii+IO_START] = data[ii];
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public void writeByte(int address, byte data){
        byte[] dataArray = {data};
        writeData(address, dataArray);
    }

    public void writeShort(int address, short data){
        byte[] dataArray = TypeConversion.shortToByteArray(data,ByteOrder.LITTLE_ENDIAN);
        writeData(address, dataArray);
    }

    public byte[] readData(int address, int length) {
        this.i2cDevice.enableI2cReadMode(i2cAddress, address, length);
        byte[] readVal;
        try {
            this.readLock.lock();
            readVal = Arrays.copyOfRange(this.readCache, IO_START, (IO_START) + length);
        } finally {
            this.readLock.unlock();
        }
        return readVal;
    }

    public byte readByte(int address) throws Exception{
        int length = 1;
        byte[] data = readData(address,length);

        // catching faulty returns
        if(data.length>length){
            throw new Exception("To many return values (" + data.length +")");
        }
        else if(data.length<length){
            throw new Exception("To little return values (" + data.length + ")");
        }
        else {
            //save to assume there is <length> number of elements
            return data[0];
        }
    }

    public short readShort(int address) {
        byte[] data = readData(address, 2);
        return TypeConversion.byteArrayToShort(data, ByteOrder.LITTLE_ENDIAN);
    }

    public void portIsReady(int port) {
        this.i2cDevice.setI2cPortActionFlag();
        this.i2cDevice.readI2cCacheFromController();
        this.i2cDevice.writeI2cPortFlagOnlyToController();
    }
}
