1. The game manager class is based on the singleton design pattern. 

2. The class contains a few variables that are in place to keep track of game status. These variables can be accessed via setter/getter methods. The incrementScore method also updates the score text on the screen accordingly.

3. The game manager contains a method called resetGame which simply reverts game data back to their original values.

Methods:
initializeGameManager - to be called whenever a level is loaded. 

incrementScore & decrementEnemyCount - When collision is detected between a bird and an enemy, the box2d contact listener should call these methods

decrementBirdCount: We can assume that our game has a class such as 'BirdLauncher' which will handle the calls to this method.

Installation:
Copy the code into your project and import the required classes. 