package org.wheeler.robotics.ollie;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class UltrasoundSensorOllie extends OpMode {
    //private DataLogger dl;
    private Wire ds;
    private int readCount=0;
    private int distance;
    private long pingTime;

    public void init() {
        //dl = new DataLogger("US Test");
        ds = new Wire(hardwareMap, "ds", 0xE0);

        /*dl.addField("Micros");
        dl.addField("Dist");
        dl.newLine();*/
    }

    public void start() {
        ds.beginWrite(0x51);
        ds.write(0);
        ds.endWrite();
        pingTime = System.currentTimeMillis();
    }

    public void loop() {
        if((System.currentTimeMillis() - pingTime) > 100) {
            ds.requestFrom(0,2);
            ds.beginWrite(0x51);
            ds.write(0);
            ds.endWrite();
            pingTime = System.currentTimeMillis();
        }

        if (ds.responseCount() > 0){
            ds.getResponse();
            if (ds.isRead()){
                long micros = ds.micros();
                distance = ds.readHL();
                if (distance < 760) {
                    /*dl.addField(micros);
                    dl.addField(distance);
                    dl.newLine();*/

                    readCount++;
                    telemetry.addData("Count", readCount);
                    telemetry.addData("Time", micros/1000);
                    telemetry.addData("cm", distance);
                }
            }
        }
    }

    public void stop() {
//        dl.closeDataLogger();
        ds.close();
    }
}
