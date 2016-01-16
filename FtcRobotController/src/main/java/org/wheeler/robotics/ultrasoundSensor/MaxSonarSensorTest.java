package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.library.ultrasoundSensor.MaxSonarSensor;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class MaxSonarSensorTest extends OpMode {
    MaxSonarSensor ds;

    public void init() {
        ds = new MaxSonarSensor(hardwareMap,"ds");
        ds.log=true;
    }

    public void loop() {
        long pingTime = System.currentTimeMillis();
        telemetry.addData("cm", ds.getDistance());
        telemetry.addData("time", System.currentTimeMillis()-pingTime);
    }

    public void stop(){
        ds.close();
    }
}
