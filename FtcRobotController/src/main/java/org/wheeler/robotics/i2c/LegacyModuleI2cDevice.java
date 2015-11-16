package org.wheeler.robotics.i2c;

import com.qualcomm.robotcore.hardware.I2cController;
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
    private final int port;
    private final LegacyModule lModule;
    private final int i2cAddress;
    private final int IO_START = 4;

    public LegacyModuleI2cDevice(LegacyModule legacyModule, int physicalPort, int i2cAddress) {
        this.lModule = legacyModule;
        this.readCache = legacyModule.getI2cReadCache(physicalPort);
        this.readLock = legacyModule.getI2cReadCacheLock(physicalPort);
        this.writeCache = legacyModule.getI2cWriteCache(physicalPort);
        this.writeLock = legacyModule.getI2cWriteCacheLock(physicalPort);
        this.port = physicalPort;
        this.i2cAddress=i2cAddress;
        legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
    }

    public void write(int address, byte[] data) {
        this.lModule.enableI2cWriteMode(this.port, i2cAddress, address, data.length);

        try {
            this.writeLock.lock();
            for(byte ii=IO_START; ii<IO_START+data.length; ii++){
                this.writeCache[ii] = data[ii];
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public void writeByte(int address, byte data){
        byte[] dataArray = {data};
        write(address, dataArray);
    }

    public void writeShort(int address, short data){
        byte[] dataArray = TypeConversion.shortToByteArray(data,ByteOrder.LITTLE_ENDIAN);
        write(address,dataArray);
    }

    public byte[] read(int address, int length) {
        this.lModule.enableI2cReadMode(this.port, i2cAddress, address, length);
        byte[] readVal;
        try {
            this.readLock.lock();
            readVal = Arrays.copyOfRange(this.readCache, IO_START, (IO_START-1) + length);
        } finally {
            this.readLock.unlock();
        }
        return readVal;
    }

    public byte readByte(int address) throws Exception{
        int length = 1;
        byte[] data = read(address,length);

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
        byte[] data = read(address, 2);
        return TypeConversion.byteArrayToShort(data, ByteOrder.LITTLE_ENDIAN);
    }

    public void portIsReady(int port) {
        this.lModule.setI2cPortActionFlag(port);
        this.lModule.readI2cCacheFromController(port);
        this.lModule.writeI2cPortFlagOnlyToController(port);
    }
}
