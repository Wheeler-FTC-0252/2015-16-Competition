package org.wheeler.robotics.gamepad;

/**
 * Created by lucien on 12/3/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public final class buttonCheck {
    private buttonCheck(){} //don't want instantiation

     public static boolean checkButton(boolean buttonValue, Boolean previousValue){
         boolean returnVal = false;
         if (buttonValue!=previousValue && buttonValue) {
             returnVal = true;
         }
         previousValue = buttonValue;

         return buttonValue;
     }
}
