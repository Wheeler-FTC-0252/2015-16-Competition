package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;

/**
 * Created by lucien on 11/12/15.
 */
public class ultrasoundSensor extends OpMode {
    LegacyModuleI2cDevice ultrasoundSensor;
    public void init() {
        ultrasoundSensor=new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),1,0x70);
    }

    public void loop() {
        ultrasoundSensor.writeByte(224, (byte) 81);
        try {
            Thread.sleep(300);
        } catch(InterruptedException e){
            telemetry.addData("sleep", e.toString());
        }

        telemetry.addData("direct", ultrasoundSensor.read(225,2).length);
        try {
            telemetry.addData("distance", ultrasoundSensor.readShort(225));
        } catch (Exception e) {
            telemetry.addData("read", e.toString());
        }

    }
}
