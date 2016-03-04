package org.wheeler.robotics.library.dcmotor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by lucien on 12/22/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class DcMotorSync extends Thread {
    private DcMotor motorMaster;
    private DcMotor motorSlave;

    private boolean running=true;
    private double targetPosition;
    private double currentPosition;
    private double speed;
    private double speedMultiplier=1/100;

    public DcMotorSync(DcMotor motorMaster, DcMotor motorSlave, boolean reversed) {
        this.motorMaster = motorMaster;
        this.motorSlave = motorSlave;
        if (reversed) {
            this.speedMultiplier = this.speedMultiplier * -1;
        }

        this.start();
    }

    public DcMotorSync(DcMotor motorMaster, DcMotor motorSlave){
        this(motorMaster, motorSlave, false);
    }

    /*public DcMotorSync setup(DcMotor motorA, DcMotor motorB, boolean reversed){
        DcMotorSync sync = new DcMotorSync(motorA, motorB, reversed);
        sync.start();
        return sync;
    }

    public DcMotorSync setup(DcMotor motorA, DcMotor motorB){
        return setup(motorA, motorB, false);
    }*/

    public void run() {
        while (running){
            targetPosition = motorMaster.getCurrentPosition();
            currentPosition = motorSlave.getCurrentPosition();
            speed = (targetPosition - currentPosition)*speedMultiplier;
            speed=Range.clip(speed, -1, 1);
            motorSlave.setPower(speed);
        }
    }

    public void close() {
        this.running = false;
    }
}
