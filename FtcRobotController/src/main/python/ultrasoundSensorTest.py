import ultrasoundSensor

uSensor=ultrasoundSensor.ultrasoundSensor()

while(True):
    print "Raw: " + str(uSensor.readRaw()) + ", cm: " + str(uSensor.readDistance())
