package org.wheeler.robotics;

/**
 * Created by lucien on 6/19/15.
 *
 * Class for the Mindsensors Motor Multiplexer
 */

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LegacyModule;
import java.lang.Runtime;
import java.util.concurrent.locks.Lock;


public class MMUX {
    private int i2cAddress=0x06;
    private int portNumber;
    private LegacyModule legacyModule;


    //Default Motor A & B Memory Addresses:
    static final public int motorAPowerAddress=0x46;
    static final public int motorBPowerAddress=0x4E;
    static final public int powerMemAddressLength=1;
    static final public int motorAEncoderTargetAddress=0x42;
    static final public int motorBEncoderTargetAddress=0x4A;
    static final public int encoderTargetAddressLength=4;
    static final public int motorATimedRunAddress=0x47;
    static final public int motorBTimedRunAddress=0x4F;
    static final public int timedRunAddressLength=1;
    static final public int motorAEncoderValueAddress=0x62;
    static final public int motorBEncoderValueAddress=0x66;
    static final public int encoderValueAddressLength=4;

    //motor A and B variables:
    public MMUXMotor motorA;
    public MMUXMotor motorB;

    private class threadEnd extends Thread {
        /*LegacyModule legacyModule = MMUX.this.legacyModule;
        int i2cAddress = MMUX.this.i2cAddress;
        int portNumber = MMUX.this.portNumber;
        int motorAPowerAddress = MMUX.this.motorAPowerAddress;
        int motorBPowerAddress = MMUX.this.motorBPowerAddress;*/

        public void run(){
            MMUX.this.motorA.setPower((byte) 0);
            MMUX.this.motorB.setPower((byte) 0);
        }
    }


    public MMUX(HardwareMap hwMap, String legacyModuleName, int portNumber) {
        //HardwareMap hwMap = new HardwareMap();
        //IDK if ^^^^^^^^ stops it working, I just thought that the user inputting it is a better way
        legacyModule = hwMap.legacyModule.get(legacyModuleName);
        this.portNumber = portNumber;
        this.motorA = new MMUXMotor(motorAPowerAddress
                , powerMemAddressLength
                , motorAEncoderTargetAddress
                , encoderTargetAddressLength
                , motorATimedRunAddress
                , timedRunAddressLength
                , motorAEncoderValueAddress
                , encoderValueAddressLength);
        this.motorB = new MMUXMotor(motorBPowerAddress
                , powerMemAddressLength
                , motorBEncoderTargetAddress
                , encoderTargetAddressLength
                , motorBTimedRunAddress
                , timedRunAddressLength
                , motorBEncoderValueAddress
                , encoderValueAddressLength);
        threadEnd quitCode = new threadEnd();
        Runtime.getRuntime().addShutdownHook(quitCode);
    }

    public void write(int memAddress, int memLength, byte byteValue) {
        byte[] byteValuesArray = {byteValue};
        write(memAddress, memLength, byteValuesArray);
    }

    public synchronized void write(int memAddress, int memLength, byte[] byteValues) {
        while (legacyModule.isI2cPortReady(portNumber)==false) {}
        legacyModule.enableNxtI2cWriteMode(portNumber, i2cAddress, memAddress, memLength);
        Lock lock = legacyModule.getI2cWriteCacheLock(portNumber);
        legacyModule.getI2cWriteCache(portNumber);
    }

    public synchronized byte[] read (int memAddress, int memLength) {
        Lock readLock = legacyModule.getI2cReadCacheLock(portNumber);
        legacyModule.enableNxtI2cReadMode(portNumber, i2cAddress, memAddress, memLength);
        legacyModule.readI2cCacheFromModule(portNumber);
        readLock.lock();
        byte[] cacheVal = legacyModule.getI2cReadCache(portNumber);
        readLock.unlock();
        return cacheVal;
    }


    public class MMUXMotor {
        //Addresses and Address Lengths
        private int powerMemAddress;
        private int powerMemAddressLength;
        private int encoderTargetAddress;
        private int encoderTargetAddressLength;
        private int timedRunAddress;
        private int timedRunAddressLength;
        private int encoderValueAddress;
        private int encoderValueAddressLength;

        public MMUXMotor(int powerMemAddress
                , int powerMemAddressLength
                , int encoderTargetAddress
                , int encoderTargetAddressLength
                , int timedRunAddress
                , int timedRunAddressLength
                , int encoderValueAddress
                , int encoderValueAddressLength)
        {
            //setting address varibles to inputted values
            this.powerMemAddress=powerMemAddress;
            this.powerMemAddressLength=powerMemAddressLength;
            this.encoderTargetAddress=encoderTargetAddress;
            this.encoderTargetAddressLength=encoderTargetAddressLength;
            this.timedRunAddress=timedRunAddress;
            this.timedRunAddressLength=timedRunAddressLength;
            this.encoderValueAddress=encoderValueAddress;
            this.encoderValueAddressLength=encoderValueAddressLength;
        }

        public void setPower(byte motorLevel){
            MMUX.this.write(powerMemAddress, powerMemAddressLength, motorLevel);
        }
        public void setEncoderTarget(byte encoderPosition) {
            MMUX.this.write(encoderTargetAddress, encoderTargetAddressLength, encoderPosition);
        }
        public byte[] getEncoderTarget() {
            return MMUX.this.read(encoderTargetAddress, encoderTargetAddressLength);
        }
        public void setTimedRun(byte runTime){
            MMUX.this.write(timedRunAddress, timedRunAddressLength, runTime);
        }
        public byte[] getTimedRun() {
            return MMUX.this.read(timedRunAddress, timedRunAddressLength);
        }
        public byte[] getEncoderValue() {
            return MMUX.this.read(encoderValueAddress,encoderValueAddressLength);
        }
    }
}
