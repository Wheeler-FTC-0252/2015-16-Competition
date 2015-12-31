package org.wheeler.robotics.led;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.wheeler.robotics.ollie.Wire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucien on 12/30/15.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class BlinkMLed {
    Wire led;

    public BlinkMLed(HardwareMap hardwareMap, String ledName, int i2cAddress){
        this.led = new Wire(hardwareMap, ledName, i2cAddress);
    }

    public BlinkMLed(HardwareMap hardwareMap, String ledName){
        this(hardwareMap, ledName, 0x09);
    }

    //------------WRITE------------\\
    public void setColor(int red, int green, int blue){
        write(0x6e, new int[]{red,green,blue});
    }

    public void startScript(int id, int loops){
        write(0x70, new int[]{id,loops,0x00});
    }

    public void stopScript(){
        write(0x6f, new int[]{0});
    }

    private void write(int register, int[] values){
        led.beginWrite(register);
        for(int ii : values){
            led.write(ii);
        }
        led.endWrite();
    }

    /*//------------READ-------------\\
    public int[] getColor(){
        return read(0x67,3);
    }

    private int[] read(int register, int length){
        led.requestFrom(register, length);
        led.getResponse();
        int[] values = new int[length];
        for(int ii=0; ii>length; ii++){
            values[ii]=led.read();
        }
        return values;
    }*/

    //------------OTHER------------\\
    public void close(){
        led.close();
    }
}
