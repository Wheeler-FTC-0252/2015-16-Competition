// Copyed from the library com.qualcom.harware.HiTechnicNxtCompassSensor (decompiled)

package org.wheeler.robotics.compassSensor;

import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;

public class CompassSensor2 extends CompassSensor implements I2cPortReadyCallback {
    public static final byte I2C_ADDRESS = 2;
    public static final byte MODE_CONTROL_ADDRESS = 65;
    public static final byte CALIBRATION = 67;
    public static final byte MEASUREMENT = 0;
    public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
    public static final byte ONE_DEGREE_HEADING_ADDER = 67;
    public static final byte CALIBRATION_FAILURE = 70;
    public static final byte DIRECTION_START = 7;
    public static final byte DIRECTION_END = 9;
    public static final double INVALID_DIRECTION = -1.0D;
    public static final int HEADING_WORD_LENGTH = 2;
    public static final int COMPASS_BUFFER = 65;
    public static final int COMPASS_BUFFER_SIZE = 5;
    private final ModernRoboticsUsbLegacyModule lModule;
    private final byte[] readCache;
    private final Lock readLock;
    private final byte[] writeCache;
    private final Lock writeLock;
    private final int port;
    private CompassMode compassMode;
    private boolean isWrite;
    private boolean i;

    public CompassSensor2 (ModernRoboticsUsbLegacyModule legacyModule, int physicalPort) {
        this.compassMode = CompassMode.MEASUREMENT_MODE;
        this.isWrite = false;
        this.i = false;
        legacyModule.enableI2cReadMode(physicalPort, 2, 65, 5);
        this.lModule = legacyModule;
        this.readCache = legacyModule.getI2cReadCache(physicalPort);
        this.readLock = legacyModule.getI2cReadCacheLock(physicalPort);
        this.writeCache = legacyModule.getI2cWriteCache(physicalPort);
        this.writeLock = legacyModule.getI2cWriteCacheLock(physicalPort);
        this.port = physicalPort;
        legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
    }

    public double getDirection() {
        if(this.isWrite) {
            return -1.0D;
        } else if(this.compassMode == CompassMode.CALIBRATION_MODE) {
            return -1.0D;
        } else {
            Object var1 = null;

            byte[] var5;
            try {
                this.readLock.lock();
                var5 = Arrays.copyOfRange(this.readCache, 7, 9);
            } finally {
                this.readLock.unlock();
            }

            return (double)TypeConversion.byteArrayToShort(var5, ByteOrder.LITTLE_ENDIAN);
        }
    }

    public String status() {
        return String.format("NXT Compass Sensor, connected via device %s, port %d", new Object[]{this.lModule.getSerialNumber().toString(), Integer.valueOf(this.port)});
    }

    public void setMode(CompassMode mode) {
        if(this.compassMode != mode) {
            this.compassMode = mode;
            this.calibrate();
        }
    }

    private void calibrate() {
        this.isWrite = true;
        int var1 = this.compassMode == CompassMode.CALIBRATION_MODE?67:0;
        this.lModule.enableI2cWriteMode(this.port, 2, 65, 1);

        try {
            this.writeLock.lock();
            this.writeCache[3] = (byte)var1;
        } finally {
            this.writeLock.unlock();
        }

    }

    private void startMeasurment() {
        if(this.compassMode == CompassMode.MEASUREMENT_MODE) {
            this.lModule.enableI2cReadMode(this.port, 2, 65, 5);
        }

        this.isWrite = false;
    }

    public boolean calibrationFailed() {
        if(this.compassMode != CompassMode.CALIBRATION_MODE && !this.isWrite) {
            boolean isCalibrated = false;

            try {
                this.readLock.lock();
                isCalibrated = this.readCache[3] == 70;
            } finally {
                this.readLock.unlock();
            }

            return isCalibrated;
        } else {
            return false;
        }
    }

    public void portIsReady(int port) {
        this.lModule.setI2cPortActionFlag(this.port);
        this.lModule.readI2cCacheFromController(this.port);
        if(this.isWrite) {
            this.startMeasurment();
            this.lModule.writeI2cCacheToController(this.port);
        } else {
            this.lModule.writeI2cPortFlagOnlyToController(this.port);
        }

    }

    public String getDeviceName() {
        return "NXT Compass Sensor";
    }

    public String getConnectionInfo() {
        return this.lModule.getConnectionInfo() + "; port " + this.port;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }
}
