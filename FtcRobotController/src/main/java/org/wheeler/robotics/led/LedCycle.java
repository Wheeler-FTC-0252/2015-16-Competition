package org.wheeler.robotics.led;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;

/**
 * Created by lucien on 11/19/15.
 */
public class LedCycle extends OpMode {
    LegacyModuleI2cDevice led;
    private final int I2C_ADDRESS=0x09;
    private final int RGB_ADDRESS=0x6e;

    public void init() {
        led = new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),4, I2C_ADDRESS);
    }

    public void loop() {
        led.writeData(RGB_ADDRESS,new byte[] {0x10,0,0});

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep1: ", "failed!");
        }

        led.writeData(RGB_ADDRESS,new byte[] {0,0x10,0});

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep2: ", "failed!");
        }

        led.writeData(RGB_ADDRESS,new byte[] {0,0,0x10});

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep3: ", "failed!");
        }
    }
}
