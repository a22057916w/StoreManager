from multiprocessing import Process
import sys
sys.path.append("sells/script/")
from sells_TPE import SELLS_TPE_INIT
from sells_NTC import SELLS_NTC_INIT

if __name__ == '__main__':
    p1 = Process(target = SELLS_TPE_INIT)
    p1.start()
    p1.join()
    p2 = Process(target = SELLS_NTC_INIT)
    p2.start()
