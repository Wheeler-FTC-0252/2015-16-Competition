package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by lucien on 11/14/15.
 */
public class UltrasoundSensor2Test extends OpMode {
    UltrasoundSensor2 uSensor;
    short readData;
    public void init() {
        uSensor= new UltrasoundSensor2(hardwareMap.legacyModule.get("lModule"),4);
    }

    public void loop() {
        try {
            readData = uSensor.readRaw();
            telemetry.addData("raw data", readData);
        } catch (Exception e){
            telemetry.addData("sleep stage", "failed!");
        }
    }
}
