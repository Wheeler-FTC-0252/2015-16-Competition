package org.wheeler.robotics.gamepad;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class ButtonCheck {
    private boolean value;
    private boolean previousValue;
    private boolean state;

    public void updateValue(boolean value) {
        this.value = value;
    }

    public boolean checkButton(){
        boolean returnVal = false;
        if (value != previousValue && value) {
            returnVal = true;

            //Change state
            if (state){
                state = false;
            }
            else {
                state = true;
            }
        }
        previousValue = value;
        return value;
    }

    public boolean getState(){
        return state;
    }
}
