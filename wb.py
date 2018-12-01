import requests
import time

def get_web_page(url, urlJump):
    time.sleep(0.5)

    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": str(urlJump)})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text
