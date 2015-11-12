package org.wheeler.robotics.i2c;

import com.qualcomm.robotcore.hardware.LegacyModule;

import java.nio.ByteBuffer;
/*
Short/Byte conversions where helped by:
http://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vise-versa
*/

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
    }
    public void writeByte(int memAddress, byte data){
        byte[] dataArray= {data};
        write(memAddress, dataArray);
    }

    public void writeShort(int memAddress, short data){
        write(memAddress,ByteBuffer.allocate(2).putShort(data).array());
        //writing array made from short
    }


    public byte[] read(int memAddress, int length){
        while(legacyModule.isI2cPortInReadMode(port)){}
        legacyModule.enableI2cReadMode(port, i2cAddress, memAddress, length);
        return legacyModule.getCopyOfReadBuffer(port);
    }

    public byte readByte(int memAddress) throws Exception{
        byte[] values=read(memAddress, 1);
        if(values.length > 0){
            return read(memAddress, 1)[0];
        }

        throw new Exception("Read did not return any values");
    }

    public short readShort(int memAddress){
        return ByteBuffer.wrap(read(memAddress, 2)).getShort();
    }
}
