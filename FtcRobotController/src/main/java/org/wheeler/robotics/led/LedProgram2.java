package org.wheeler.robotics.led;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.ollie.Wire;

/**
 * Created by lucien on 11/27/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class LedProgram2 extends OpMode {
    private BlinkMLed led;

    public void init() {
        led = new BlinkMLed(hardwareMap, "led", 0x00);
    }

    public void start() {
        led.stopScript();
        led.startScript(18, 0);
    }

    public void loop() {
        /*int[] color = led.getColor();
        telemetry.addData("Red", color[0]);
        telemetry.addData("Green", color[1]);
        telemetry.addData("Blue", color[2]);*/
    }

    public void stop() {
        led.stopScript();
        led.close();
    }
}
