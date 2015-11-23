package org.wheeler.robotics.compassSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;

/**
 * Created by lucien on 11/16/15.
 */
public class CompassSensor3Test extends OpMode {
    LegacyModuleI2cDevice cSensor;
    public void init() {
        cSensor=new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),1,2);
    }

    public void loop() {
        telemetry.addData("Value", (double)cSensor.readShort(0x44));
    }
}
