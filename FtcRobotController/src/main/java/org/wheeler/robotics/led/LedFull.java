package org.wheeler.robotics.led;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.ollie.Wire;

/**
 * Created by lucien on 12/23/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class LedFull extends OpMode {
    Wire led;
    public void init() {
        led = new Wire(hardwareMap,"led", 0x00);
    }

    public void start() {
        led.write(0x6f,0);

        led.beginWrite(0x6e);
        led.write(20);
        led.write(0);
        led.write(20);
        led.endWrite();
    }

    public void loop(){
        /*led.requestFrom(0x67, 3);
        telemetry.addData("Red", led.read());
        telemetry.addData("Green", led.read());
        telemetry.addData("Blue", led.read());*/
    }

    public void stop(){
        led.write(0x6f,0);
        led.close();
    }
}
