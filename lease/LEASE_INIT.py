from multiprocessing import Process
import sys
sys.path.append("lease/script/")
from lease_TPE import LEASE_TPE_INIT
from lease_NTC import LEASE_NTC_INIT
from info_box_TPE import INFO_BOX_TPE_INIT
from info_box_NTC import INFO_BOX_NTC_INIT
from house_box_TPE import HOUSE_BOX_TPE_INIT
from house_box_NTC import HOUSE_BOX_NTC_INIT
from lease_img_TPE import IMG_TPE_INIT
from lease_img_NTC import IMG_NTC_INIT


def LEASE_INIT():
    pl = [INFO_BOX_TPE_INIT, INFO_BOX_NTC_INIT, HOUSE_BOX_TPE_INIT, HOUSE_BOX_NTC_INIT, IMG_TPE_INIT, IMG_NTC_INIT]
    p = [None] * 6

    p1 = Process(target = LEASE_TPE_INIT)
    p2 = Process(target = LEASE_NTC_INIT)
    # start colleting data
    p1.start()
    p2.start()
    # p1, p2 須執行完才能往下進行
    p1.join()
    p2.join()
    p1.close()
    p2.close()

    try:
        for i in range(0, 6):
            p[i] = Process(target = pl[i])
            p[i].start()
        for i in range(0, 6):
            p[i].join()
        for i in range(0, 6):
            p[i].close()
    except Exception as e:
        print(e)
    finally:
        print("Lease data collection complete")
