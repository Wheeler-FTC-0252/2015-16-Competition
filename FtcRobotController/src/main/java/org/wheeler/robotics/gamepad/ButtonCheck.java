package org.wheeler.robotics.gamepad;

import android.util.Log;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class ButtonCheck {
    public boolean state=false;
    public boolean previousValue=false;
    public boolean debug=false;

    private boolean value;

    public void updateValue(boolean value) {
        this.value = value;
    }

    public boolean checkButton(){
        boolean returnVal = false;
        if (value != previousValue && value) {
            returnVal = true;

            if (debug) {
                Log.d("CheckButtonStatus", "value != previousValue");
                Log.d("CheckButtonStatus", "value == " + String.valueOf(value));
                Log.d("CheckButtonStatus", "previousValue == " + String.valueOf(previousValue));
            }

            //Change state
            if (state){
                state = false;
            }
            else {
                state = true;
            }
        }
        previousValue = value;

        if (debug) Log.d("CheckButtonStatus", "Returning: " + String.valueOf(previousValue));
        
        return returnVal;
    }
}