package org.wheeler.robotics.ultrasoundSensor;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.wheeler.robotics.ollie.Wire;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class MaxSonarSensor {
    private Wire ds;
    private int readCount=0;
    private int distance;
    private long pingTime;
    public boolean log=false;

    public MaxSonarSensor(HardwareMap hardwareMap, String name, int address){
        ds = new Wire(hardwareMap, name, address);
    }

    public MaxSonarSensor(HardwareMap hardwareMap, String name){
        this(hardwareMap, name, 0xE0);
    }

    public int read(){
        ds.beginWrite(0x51);
        ds.write(0);
        ds.endWrite();
        pingTime = System.currentTimeMillis();

        while (true) {
            if ((System.currentTimeMillis() - pingTime) > 100) {
                ds.requestFrom(0, 2);
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
                    if (distance < 760) {
                        readCount++;
                        if (log){
                            Log.d("distance", String.valueOf(distance));
                        }
                        return distance;
                    }
                }
            }
        }
    }

    public void close(){
        ds.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
