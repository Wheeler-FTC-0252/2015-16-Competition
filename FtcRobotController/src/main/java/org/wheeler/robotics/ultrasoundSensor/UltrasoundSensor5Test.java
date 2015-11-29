package org.wheeler.robotics.ultrasoundSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.wheeler.robotics.ollie.Wire;

/**
 * Created by lucien on 11/25/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class UltrasoundSensor5Test extends OpMode {
    private Wire ds;
    private int readCount = 0;
    private int distance;
    private long pingTime;

    public void init() {
        ds = new Wire(hardwareMap,"uSensor", 0x70);
    }

    public void start() {
        ds.beginWrite(0x51);
        ds.write(0);
        ds.endWrite();
        pingTime = System.currentTimeMillis();
    }

    public void loop() {
        if ((System.currentTimeMillis() - pingTime) > 100){
            ds.requestFrom(225,2);
            long micros = ds.micros();
            distance = ds.readHL();
            if (distance < 760){
                readCount++;
                telemetry.addData("Count", readCount);
                telemetry.addData("Time", micros);
                telemetry.addData("cm", distance);
            }

            ds.beginWrite(224);
            ds.write(81);
            ds.endWrite();
            pingTime = System.currentTimeMillis();
        }
    }

    public void stop(){
        ds.close();
    }
}
