package org.wheeler.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.wheeler.robotics.I2c.LegacyModuleI2cDevice;
/**
 * Created by lucien on 11/9/15.
 */
public class CompassSensorTest extends OpMode {
    LegacyModuleI2cDevice compassModule;
    byte oneDeg;
    byte twoDeg;
    public void init() {
        compassModule =new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),1,0x2);
    }

    public void loop() {
        oneDeg=compassModule.readByte(43);
        twoDeg=compassModule.readByte(42);
        telemetry.addData("1 Deg: ", oneDeg);
        telemetry.addData("2 Deg: ", twoDeg);
        telemetry.addData("1+2 Deg: ", oneDeg+twoDeg);
    }
}
