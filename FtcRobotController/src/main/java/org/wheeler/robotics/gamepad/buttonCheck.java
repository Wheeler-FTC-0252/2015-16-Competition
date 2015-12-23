package org.wheeler.robotics.gamepad;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class buttonCheck {

    boolean previousValue;
    public buttonCheck(){}

    public boolean checkButton(boolean buttonValue){
         boolean returnVal = false;
         if (buttonValue != previousValue && buttonValue) {
             returnVal = true;
         }
         previousValue = buttonValue;

         return buttonValue;
    }
}
