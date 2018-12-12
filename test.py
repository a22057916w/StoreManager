from multiprocessing import Process
import sys
sys.path.append("sells/script/")
from sells_TPE import SELLS_TPE_INIT
from sells_NTC import SELLS_NTC_INIT
from info_box_TPE import INFO_BOX_TPE_INIT
from info_box_NTC import INFO_BOX_NTC_INIT

if __name__ == '__main__':
    pl = [SELLS_TPE_INIT, SELLS_NTC_INIT]
    p1 = Process(target = pl[0])
    p1.start()
    #p1.join()
    p2 = Process(target = pl[1])
    p2.start()
