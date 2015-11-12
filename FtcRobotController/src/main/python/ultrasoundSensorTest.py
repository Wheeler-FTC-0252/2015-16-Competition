import ultrasoundSensor

uSensor=ultrasoundSensor.ultrasoundSensor()

while(True):
    value=uSensor.readRaw()
    print "Raw: " + str(value) + ", cm: " + str(uSensor.rawToDistance(value))
