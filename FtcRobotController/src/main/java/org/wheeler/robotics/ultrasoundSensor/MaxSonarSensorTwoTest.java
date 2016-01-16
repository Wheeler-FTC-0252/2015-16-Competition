package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.library.ultrasoundSensor.MaxSonarSensor;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class MaxSonarSensorTwoTest extends OpMode {
    MaxSonarSensor ds1;
    MaxSonarSensor ds2;

    public void init() {
        ds1 = new MaxSonarSensor(hardwareMap,"ds1", 0xE0);
        ds1.log=true;
        ds1.logTag="distance 1";

        ds2 = new MaxSonarSensor(hardwareMap,"ds2", 0xE6);
        ds2.log=true;
        ds2.logTag="distance 2";
    }

    public void loop() {
        long pingTime = System.currentTimeMillis();
        telemetry.addData("cm1", ds1.getDistance());
        telemetry.addData("cm2", ds2.getDistance());
        telemetry.addData("time", System.currentTimeMillis()-pingTime);
    }

    public void stop(){
        ds1.close();
        ds2.close();
    }
}
