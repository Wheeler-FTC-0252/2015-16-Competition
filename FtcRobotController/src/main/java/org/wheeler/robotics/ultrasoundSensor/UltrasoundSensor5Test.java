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
            ds.requestFrom(0,2);
            ds.beginWrite(0x51);
            ds.write(0);
            ds.endWrite();
            pingTime = System.currentTimeMillis();
        }

        if (ds.responseCount() > 0) {
            ds.getResponse();
            if (ds.isRead()) {
                long micros = ds.micros();
                distance = ds.readHL();
                if (distance < 760){
                    readCount++;
                    telemetry.addData("Count", readCount);
                    telemetry.addData("Time", micros/1000);
                    telemetry.addData("cm", distance);
                }
            }
        }
    }

    public void stop(){
        ds.close();
    }
}
