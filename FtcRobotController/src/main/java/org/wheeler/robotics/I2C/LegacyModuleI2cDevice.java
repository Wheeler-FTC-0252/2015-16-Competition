package org.wheeler.robotics.I2c;

import com.qualcomm.robotcore.hardware.LegacyModule;

/**
 * Created by lucien on 11/6/15.
 */
public class LegacyModuleI2cDevice {
    private LegacyModule legacyModule;
    private int port;
    private int i2cAddress;

    public LegacyModuleI2cDevice(LegacyModule lModule, int port, int i2cAddress){
        legacyModule=lModule;
        this.port = port;
        this.i2cAddress=i2cAddress;
    }

    public void write(int memAddress, byte[] data){
        while(legacyModule.isI2cPortInWriteMode(port)){}
        legacyModule.enableI2cWriteMode(port, i2cAddress, memAddress, data.length);
        legacyModule.copyBufferIntoWriteBuffer(port, data);
        legacyModule.setI2cPortActionFlag(port);
        //compassModule.writeI2cCacheToController(port);
    }
    public void writeByte(int memAddress, byte data){
        byte[] dataArray= {data};
        write(memAddress,dataArray);
    }

    public byte[] read(int memAddress, int length){
        while(legacyModule.isI2cPortInReadMode(port)){}
        legacyModule.enableI2cReadMode(port, i2cAddress, memAddress, length);
        //compassModule.readI2cCacheFromController(port);
        return legacyModule.getCopyOfReadBuffer(port);
    }
    public byte readByte(int memAddress){
        return read(memAddress, 1)[0];
    }
}
