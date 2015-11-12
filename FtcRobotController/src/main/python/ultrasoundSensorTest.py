import ultrasoundSensor

uSensor=ultrasoundSensor.ultrasoundSensor()

print "Raw: " + str(uSensor.readRaw())
print "cm: " + str(uSensor.readDistance())
