package org.wheeler.robotics.library.rgbSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.TypeConversion;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.II2cDeviceClient;

/**
 * Created by lucien on 1/23/16.
 *
 * @author Lucien Gaitskell
 * @version 0.1
 */
public class AdafruitTCS34725 implements Runnable {
	private II2cDeviceClient cs;
	private int RED_REGISTER=0x17;
	private int redValue;
	private int GREEN_REGISTER=0x19;
	private int greenValue;
	private int BLUE_REGISTER=0x1B;
	private int blueValue;

	public AdafruitTCS34725(OpMode context, HardwareMap hardwareMap, String name, int address) {
		cs = ClassFactory.createI2cDeviceClient(context, hardwareMap.i2cDevice.get(name), address, true);
	}

	public AdafruitTCS34725(OpMode context, HardwareMap hardwareMap, String name) {
		this(context, hardwareMap, name, 0x29);
	}

	public void run() {
		redValue = TypeConversion.byteArrayToShort(cs.read(RED_REGISTER, 2));
		greenValue = TypeConversion.byteArrayToShort(cs.read(GREEN_REGISTER, 2));
		blueValue = TypeConversion.byteArrayToShort(cs.read(BLUE_REGISTER, 2));
	}

	public int getRed() {return redValue;}
	public int getGreen() {return greenValue;}
	public int getBlue() {return blueValue;}

	public int[] getRGB() {return new int[] {redValue,greenValue,blueValue};}
}
