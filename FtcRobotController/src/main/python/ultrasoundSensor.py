import smbus
import time

DEFAULT={'busNumb':1, 'address':0x70}
ADDRESSES={'write':224,'read':225}
VALUES={'readStart':81}
TEST_TIME=0.3

class ultrasoundSensor:
    def __init__(self, address=DEFAULT['address'], busNumb=DEFAULT['busNumb']):
        self.BUS = smbus.SMBus(busNumb)
        self.ADDRESS=address

    def rawToDistance(int value):
        return (value/256)+5

    def readRaw(self):
        self.BUS.write_byte_data(self.ADDRESS, ADDRESSES['write'], VALUES['readStart'])
        time.sleep(TEST_TIME)
        return self.BUS.read_word_data(self.ADDRESS, ADDRESSES['read'])

    def readDistance(self):
        return self.rawToDistance(self.readRaw())
