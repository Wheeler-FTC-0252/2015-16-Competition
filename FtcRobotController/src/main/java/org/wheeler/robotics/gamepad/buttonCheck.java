package org.wheeler.robotics.gamepad;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class buttonCheck {
    public boolean value;
    private boolean previousValue;

    public void updateValue(boolean value) {
        this.value = value;
    }

    public boolean checkButton(){
        boolean returnVal = false;
        if (value != previousValue && value) {
            returnVal = true;
        }
        previousValue = value;
        return value;
    }
}
