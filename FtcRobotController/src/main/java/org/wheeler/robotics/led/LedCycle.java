package org.wheeler.robotics.led;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.i2c.LegacyModuleI2cDevice;

/**
 * Created by lucien on 11/19/15.
 */
public class LedCycle extends OpMode {
    LegacyModuleI2cDevice led;
    byte[] values;
    private final int I2C_ADDRESS=0x00;
    private final int RGB_ADDRESS=0x6e;

    public void init() {
        led = new LegacyModuleI2cDevice(hardwareMap.legacyModule.get("lModule"),4, I2C_ADDRESS);
        led.writeData(0x6f,new byte[] {});
    }

    public void loop() {
        led.writeData(RGB_ADDRESS,new byte[] {0x70,0,0});

        //values = led.readData(0x67,3);
        values = led.readCache;
        for (int ii=0; ii<values.length; ii++){
            telemetry.addData("value " + ii, values[ii]);
        }

        /*
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep1: ", "failed!");
        }

        led.writeData(RGB_ADDRESS,new byte[] {0,0x70,0});

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep2: ", "failed!");
        }

        led.writeData(RGB_ADDRESS,new byte[] {0,0,0x70});

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            telemetry.addData("sleep3: ", "failed!");
        }
        */
    }
}
