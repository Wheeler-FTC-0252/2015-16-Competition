package org.wheeler.robotics.library.rgbSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.wheeler.robotics.library.ollie.Wire;

/**
 * Created by lucien on 1/23/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class AdafruitTCS34725 implements Runnable {
	private Wire cs;
	private int RED_REGISTER=0x17;
	private int redValue;
	private int GREEN_REGISTER=0x19;
	private int greenValue;
	private int BLUE_REGISTER=0x1B;
	private int blueValue;

	public AdafruitTCS34725(OpMode context, HardwareMap hardwareMap, String name, int address) {
		cs = new Wire(hardwareMap, name, address);
	}

	public AdafruitTCS34725(OpMode context, HardwareMap hardwareMap, String name) {
		this(context, hardwareMap, name, 0x29);
	}

	public void run() {
		cs.requestFrom(RED_REGISTER, 2);
		redValue = cs.readHL();

		cs.requestFrom(GREEN_REGISTER, 2);
		greenValue = cs.readHL();

		cs.requestFrom(BLUE_REGISTER, 2);
		blueValue = cs.readHL();
	}

	public int getRed() {return redValue;}
	public int getGreen() {return greenValue;}
	public int getBlue() {return blueValue;}

	public int[] getRGB() {return new int[] {redValue,greenValue,blueValue};}
}