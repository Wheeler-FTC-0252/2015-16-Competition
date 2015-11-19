package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;

/**
 * Created by lucien on 11/19/15.
 */
public class UltrasoundSensor3 extends OpMode {
    LegacyModuleI2cDevice uSensor;
    private final int PORT=4;
    private final int I2C_ADDRESS=0x70;
    private final int WRITE_ADDRESS=224;
    private final int READ_ADDRESS=225;
    private final byte WRITE_VALUE=81;

    public void init() {
        uSensor = new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"), PORT, I2C_ADDRESS);
    }

    public void loop() {
        uSensor.writeByte(WRITE_ADDRESS,WRITE_VALUE);

        try {
            Thread.sleep(300);
        } catch(InterruptedException e){
            telemetry.addData("sleep","failed");
        }

        telemetry.addData("reading", uSensor.readShort(READ_ADDRESS));
    }
}
