import smbus
import time

DEFAULT={'busNumb':1, 'address':70}
ADDRESSES={'write':224,'read':225}

class ultrasoundSensor:
    def __init__(self, address=DEFAULT['address'], busNumb=DEFAULT['busNumb']):
        self.BUS = smbus.SMBus(busNumb)
        self.ADDRESS=address

    def readDistance():
        self.BUS.write_byte_data(self.ADDRESS, ADDRESSES['write'], 81)
        time.sleep(0.1)
        return self.BUS.read_byte_data(self.ADDRESS, ADDRESSES['read'])
