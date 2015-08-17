package org.wheeler.robotics;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.wheeler.robotics.MMUX;
//import com.qualcomm.robotcore.hardware.Gamepad;
//import com.qualcomm.robotcore.eventloop.EventLoopManager;
//import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;


/**
 * Created by lucien on 6/10/15.
 */

public class MMUXTest extends OpMode {
    boolean bar;

    public void poo(){
        MMUX MMux = new MMUX(hardwareMap,"MMux",1);
        MMux.motorA.setPower((byte)100);
    }

    @Override
    public void start() {
        //telemetry.addData("1 Start", "Luc's App Boldly Starts");
        DbgLog.msg("Luc's App Boldly Starts");
    }

    @Override
    public void loop() {
        bar = gamepad1.a;
        telemetry.addData("2 Status", "Joystick 1: " + bar);
        DbgLog.msg("Joystick 1: " + bar);

        /*bar = myGamepad.a;
        telemetry.addData("2 Status", "Joystick 1: " + bar);*/

    }

    @Override
    public void stop() {
        //telemetry.addData("3 Stop", "Luc's App is Terminated *Sad Music Plays*");
        DbgLog.msg("Luc's App is Terminated *Sad Music Plays*");
    }
}
