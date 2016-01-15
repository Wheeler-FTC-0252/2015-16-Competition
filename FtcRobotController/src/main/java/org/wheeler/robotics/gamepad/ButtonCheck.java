package org.wheeler.robotics.gamepad;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class ButtonCheck {
    public boolean state=false;

    private boolean value;
    private boolean previousValue=false;

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
}
