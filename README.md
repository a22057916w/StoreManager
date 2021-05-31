## Overview
StoreManger is a shop-rental-and-sell platform for those who want to start a business. The app ranks the real estate for sale or rents according to the area, price, population, etc. Of course, users can choose the specific location, square meter, and price margin.

The App organizes the data crawled from other webs or platforms and analyzes the basic info(area, price, etc.) and surroundings(MRT stations, population, etc.) to help users pick the best place, starting their businesses.

<br><br>

:warning: Warning: The App can not be used for commercial purposes.

## Detail
### Data
StoreManager collects the data and organizes it as the following pictures.
<p align="left">
  <img src="/.meta/housebox.PNG"><br>
  <img src="/.meta/infobox.PNG"><br>
  These data correspond to the Activity that display the shop info. <br><br><br>
  <img src="/.meta/location.PNG"><br>
  The data correspond to the same Activity mentioned above for the shop's mini-map that links to google-map. <br><br><br>
  <img src="/.meta/totalrows.PNG"><br>
  And this corresponds to the Activity that shows the search result.
</p>

### Implementation
All this is done by the python scripts, the crawler goes to the websiets for data every 24 hours then update the data for server. At first, the data needs to manually update every 3 to 5 days. However, we realized it's not faster enough for getting first-hand info and the data was growing larger every day. Therefore, there was more scripts but hard to run manually. In this case, we must design the multi-processing and self-updating program. Now, simply run the [init.py](https://github.com/a22057916w/StoreManager/blob/master/init.py), the scripts runs every subroutines and update data every day.
