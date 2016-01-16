package org.wheeler.robotics.ollie;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class UltrasoundSensorOllieDouble extends OpMode {
    //private DataLogger dl;
    private Wire ds1;
    private Wire ds2;

    private int readCount=0;
    private int distance1;
    private int distance2;
    private long pingTime;


    public void init() {
        //dl = new DataLogger("US Test");
        ds1 = new Wire(hardwareMap, "ds1", 0xE0);
        ds2 = new Wire(hardwareMap, "ds2", 0xE6);
        Log.d("USStatus", "Made Instances");

        /*dl.addField("Micros");
        dl.addField("Dist");
        dl.newLine();*/
    }

    public void start() {
        Log.d("USStatus", "running start()");
        ds1.beginWrite(224);
        ds1.write(81);
        ds1.endWrite();
        ds2.beginWrite(224);
        ds2.write(81);
        ds2.endWrite();
        pingTime = System.currentTimeMillis();
    }

    public void loop() {
        if((System.currentTimeMillis() - pingTime) > 100) {
            Log.d("USStatus", "Request from ds");
            ds1.requestFrom(225, 2);
            ds1.beginWrite(224);
            ds1.write(81);
            ds1.endWrite();
            ds2.requestFrom(225, 2);
            ds2.beginWrite(224);
            ds2.write(81);
            ds2.endWrite();
            pingTime = System.currentTimeMillis();
        }

        Log.d("USStatus", "ds1.responseCount()=" + String.valueOf(ds1.responseCount()));
        Log.d("USStatus", "ds2.responseCount()=" + String.valueOf(ds2.responseCount()));

        if (ds1.responseCount() > 0){
            Log.d("USStatus", "ds1 ResponseCount>0");
            ds1.getResponse();
            if (ds1.isRead()){
                Log.d("USStatus", "ds1 isRead() true");
                long micros = ds1.micros();
                distance1 = ds1.readHL();
                if (distance1 < 760) {

                    /*dl.addField(micros);
                    dl.addField(distance);
                    dl.newLine();*/
                    Log.d("USStatus", "Read from ds1");
                    readCount++;
                    //telemetry.addData("Count1", readCount);
                    //telemetry.addData("Time1", micros/1000);
                    telemetry.addData("cm1", distance1);
                }
            }
        }

        if (ds2.responseCount() > 0){
            Log.d("USStatus", "ds2 ResponseCount>0");
            ds2.getResponse();
            if (ds2.isRead()){
                Log.d("USStatus", "ds2 isRead() true");
                long micros = ds2.micros();
                distance2 = ds2.readHL();
                if (distance2 < 760) {
                    //dl.addField(micros);
                    //dl.addField(distance);
                    //dl.newLine();
                    Log.d("USStatus", "Read from ds2");
                    readCount++;
                    //telemetry.addData("Count2", readCount);
                    //telemetry.addData("Time2", micros/1000);
                    telemetry.addData("cm2", distance2);
                }
            }
        }
    }

    public void stop() {
//        dl.closeDataLogger();
        ds1.close();
        ds2.close();
    }
}
