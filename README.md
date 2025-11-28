# Weather Map Viewer:

 TODO ADD IMAGES/VIDEOS

- Application Summary:
    <br> This application displays a weather overlay over an interactive map, allowing the user to view the weather 
    conditions of differnt parts of the world by navigating the map.
    <br> 
    - The map: 
      - The interactive map allows the user to navigate the map by dragging the mouse or using the mousewheel to zoom 
      in.
      - The basemap can be changed using the top dropdown menu on the right. This menu is only avalible if a 
      ThunderForest API key is set as an enviroment variable (THUNDERFOREST_KEY=[key here]), avalible for free. 
      https://thunderforest.com/
    <br> 
    - Weather Overlay:
      - The weather overlay displays the weather conditions of the visible areas as an overlay on top of the map.
      The color gradient legend at the top of the application shows the values that the colors correspond to, 
      provided by https://www.weatherapi.com/. 
      There are 4 types of weather data avalible, which can be selected using the dropdown menu on the right:
        - Temperature
        - Precipitation
        - Pressure
        - Wind Speed
      - The opacity slider below the drowdown menu allows the user to set the overlay opacity of each weather type.
    <br> 
    - Forecast:
      - The time slider at the bottom of the window allows the user to set the forecast time of the overlay, up to 3 
      days in the future, and the play button will automatically move the slider forward, showing the weather forecast 
      as an animated map. Note that due to internet speed and caching the weather overlay on the first cycle may be 
      laggy, but it will be much smoother once all tiles have been cached.
    <br> 
    - Info Panel:
    Zooming into an area will cause an info panel to pop up, showing the conditions and horly forecast of the visible 
    area.
    <br>

    - Bookmarks:

      - The bookmarks panel lets the user save specific locations on the map so they can quickly return to them later.
      - Clicking **Add** creates a new bookmark for the current map view, storing the location (latitude/longitude) together with a user-provided name.
      - All saved bookmarks are displayed in a list, where the user can:
        - **Select** a bookmark by clicking on it. 
        - Click **Visit** to move the map viewport to the bookmarked location.
        - Click **Remove** to delete the selected bookmark from the list.
      - Bookmarks are stored persistently: closing and reopening the application will keep all previously saved bookmarks, so the user can continue navigating using their saved locations.
    <br>
    
    - Searchbar:
        - TODO
    <br>
    
    - Map Settings:
    
      - While the application is running, any changes to the map settings (e.g., the basemap type, zoom level, and weather overlay options such as opacity) are saved.
  
      - When the app is launched, the previously saved map settings are automatically loaded, restoring the map layout, selected basemap, and overlay configuration.
  
      - This allows the user to continue from where they left off, without needing to reconfigure their preferred map view each time they open the application.
    <br>
      

- User Stories:
    - TODO (rest of the user stories)
    - As a user, I want to add and remove location bookmarks that persist across app sessions, and jump to a bookmarked location by pressing a “Visit” button.
    - As a user, I want the map layout and settings to be saved and automatically restored when I reopen the app. 

- API Information:
    - https://thunderforest.com/: This api is used by the JMapViewer module to display the maps. The default map type
      (Mapnik) does not require an API key, while the other options require one, avalible for free. 
      Set enviroment variable THUNDERFOREST_KEY=[key here] to enable the basemap dropdown menu.
    - https://www.weatherapi.com/docs/: We are using 2 services provided by this provider. 
      - weathermaps: returns an image based on the specified time, zoom level, and tile coordinates. Used to construct
      the weather overlay.
      - forecast: TODO


