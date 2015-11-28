package org.wheeler.robotics.led;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.ollie.Wire;

/**
 * Created by lucien on 11/27/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class LedProgram extends OpMode {
    private Wire ds;

    public void init() {
        ds = new Wire(hardwareMap,"led", 0x00);
    }

    public void start(){
        ds.write(0x70,18);
        ds.write(0x71,0);
    }

    public void loop() {
        ds.requestFrom(0x67,3);
        telemetry.addData("Red", ds.read());
        telemetry.addData("Green", ds.read());
        telemetry.addData("Blue", ds.read());
    }
}
