package org.wheeler.robotics.library.ultrasoundSensor;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class MaxSonarSensor extends Thread {
    private Wire ds;
    private int readCount=0;
    private int distance;
    private int returnDistance=0;
    private int previousDistance=0;
    private long pingTime;
    public Thread thread;
    public boolean running=true;
    public boolean log=false;
    public String logTag = "distance";

    public MaxSonarSensor(HardwareMap hardwareMap, String name, int address){
        ds = new Wire(hardwareMap, name, address);
        this.start();
     }

    public MaxSonarSensor(HardwareMap hardwareMap, String name){
        this(hardwareMap, name, 0xE0);
    }

    public void run() {
        ds.beginWrite(224);
        ds.write(81);
        ds.endWrite();
        pingTime = System.currentTimeMillis();

        while (running) {
            if ((System.currentTimeMillis() - pingTime) > 100) {
                ds.requestFrom(225, 2);
                ds.beginWrite(224);
                ds.write(81);
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
                        returnDistance=distance;

                        if (log){
                            Log.d(logTag, String.valueOf(distance));

                            int change = Math.abs(previousDistance-returnDistance);
                            if (change>20) {
                                Log.d("distanceWarn", "Thats a big change! (" + String.valueOf(change) + ")");
                            }
                        }
                        previousDistance=returnDistance;
                    }
                }
            }
        }
    }

    public int getDistance(){
        return returnDistance;
    }

    public void close(){
        running=false;
        ds.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
