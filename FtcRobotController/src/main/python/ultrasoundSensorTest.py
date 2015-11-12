import ultrasoundSensor

uSensor=ultrasoundSensor.ultrasoundSensor()

while(true):
    print "Raw: " + str(uSensor.readRaw()) + ", cm: " + str(uSensor.readDistance())
