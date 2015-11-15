package org.wheeler.robotics.compassSensor;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.hardware.HiTechnicNxtCompassSensor;
/**
 * Created by lucien on 11/14/15.
 */
public class CompassSensor2Test extends OpMode {
    CompassSensor cSensor;
    public void init() {
        cSensor= new HiTechnicNxtCompassSensor((ModernRoboticsUsbLegacyModule)hardwareMap.legacyModule.get("lModule"), 1);
    }

    public void loop() {
        telemetry.addData("bearing",cSensor.getDirection());
    }
}
