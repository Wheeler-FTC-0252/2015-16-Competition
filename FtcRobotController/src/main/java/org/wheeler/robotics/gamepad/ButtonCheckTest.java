package org.wheeler.robotics.gamepad;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by lucien on 1/16/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class ButtonCheckTest extends OpMode {
    ButtonCheck button;
    public void init() {
        button = new ButtonCheck();
    }

    public void loop() {
        button.updateValue(gamepad1.a);

        boolean value = button.checkButton();
        telemetry.addData("value", value);
        if (value){
            telemetry.addData("valueTrue", "yes");
        }

        telemetry.addData("state", button.state);
        telemetry.addData("prevValue", button.previousValue);
    }
}
