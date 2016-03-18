package org.wheeler.robotics.MRColorSensor;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Created by lucieng on 3/17/16.
 */

@TeleOp
public class MRColorSensorTest extends LinearOpMode {
    ColorSensor cs;
    public void runOpMode() throws InterruptedException {
        waitForStart();
        cs = hardwareMap.colorSensor.get("cs");
        cs.setI2cAddress(66);
        cs.enableLed(true);
        waitOneFullHardwareCycle();
        waitOneFullHardwareCycle();
        Log.d("colorSensor", String.valueOf(cs.red()));
        cs.close();
    }
}
