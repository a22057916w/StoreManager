import multiprocessing as mp
import sys
sys.path.append("sells/")
sys.path.append("lease/")
from SELLS_INIT import SELLS_INIT
from LEASE_INIT import LEASE_INIT

if __name__ == "__main__":
    ps = mp.Process(target = SELLS_INIT)
    pl = mp.Process(target = LEASE_INIT)
    ps.start()
    pl.start()
    ps.join()
    pl.join()
    ps.close()
    pl.close()
