## Overview
StoreManger is a shop-rental-and-sell platform for those who want to start a business. The app ranks the real estate for sale or rents according to the area, price, population, etc. Of course, users can choose the specific location, square meter, and price margin.

The App organizes the data crawled from other webs or platforms and analyzes the basic info(area, price, etc.) and surroundings(MRT stations, population, etc.) to help users pick the best place, starting their businesses.

<br><br>

:warning: Warning: This App can not be used for commercial purposes.

## Guide
<div align="center">
  <img src="/.meta/mainpage.PNG" width="40%" height="40%">
</div>
The main page offers the user to select the specific conditions, such as region, for sale or rents, the business type, price, square meter. The user must notice that the first two slots must be decided for searching. Otherwise, there will be an error message.
<br><br>

<div align="center">
  <img src="/.meta/search_result.png" width="40%" height="40%">
</div>
After you hit the search button. The App goes to the page that shows a list of ranked shops. The score is estimated by the population of each MRT station,  transport stations, and the region; the surrounding environment of the shop, such as school, park, parking lot, night market, etc.
<br><br>

<div align="center">
  <img src="/.meta/shop_info.png" width="40%" height="40%">
</div>
The picture above shows the shop's detail after choosing one of the slots on the list. It also shows the three substantial scores, which we calculated out the total score shown in the previous list by weighted average. The photos on the top of the page are rollable, depending on how many pictures we got; The map marking the location can be browsed on the mini-map or the google map.

## Detail
### Data
StoreManager collects the data and organizes it as the following pictures.
<p align="left">
  <img src="/.meta/housebox.PNG"><br>
  <img src="/.meta/infobox.PNG"><br>
  These data correspond to the Activity that display the shop info. <br><br><br>
  <img src="/.meta/location.PNG"><br>
  The data correspond to the same Activity mentioned above for the shop's mini-map that links to google map. <br><br><br>
  <img src="/.meta/totalrows.PNG"><br>
  And this corresponds to the Activity that shows the search result.
</p>

### Implementation
All about this was done by the python scripts. The crawler goes to the websites for data every 24 hours then update the data for the server. At first, the data was manually updated every 3 to 5 days. However, we realized it's not faster enough for getting first-hand info and the data was growing larger every day. Therefore, there were more scripts but hard to run manually. In this case, we must design the multi-processing and self-updating program. Now, simply run the [init.py](https://github.com/a22057916w/StoreManager/blob/master/init.py). The script runs every subroutine and updates data every day.

## License
Avaliable under [GPL License](https://github.com/a22057916w/StoreManager/blob/master/LICENSE)
