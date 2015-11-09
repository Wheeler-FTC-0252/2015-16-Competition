package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;
/**
 * Created by lucien on 11/6/15.
 * 4theWheelerRoBoTicks
 */

public class MMUXDrive extends OpMode {
    int I2CADDRESS=0x06;
    LegacyModuleI2cDevice legacyModule;
    public void init() {
        legacyModule= new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),1,I2CADDRESS);
    }

    public void loop() {
        telemetry.addData("Firmware: ", legacyModule.readByte(0x00));
        telemetry.addData("test", "test");
    }
}
