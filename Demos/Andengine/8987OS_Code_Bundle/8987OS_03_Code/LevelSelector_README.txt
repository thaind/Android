This particular LevelSelector class allows for easy implementation of a grid of selectable level tiles. The class includes mChapter member variables which allow us to create different LevelSelector classes for each chapter in an application. Upon pressing a button, the current implementation simply changes the color of the scene, applying a red background if locked levels are pressed, a green background if unlocked levels are pressed. The current implementation does not allow for loading a level, but all of the necessary components are there. In the onAreaTouched() method within the tile creation, we can simply include a method (relative to our own level loaders) which might look similar to:
LevelLoader.load(mChapter, levelNumber);

The tiles are placed on a screen using basic math, which applies them in a consistent manner regardless of tile scale, padding or sprite.

Installation:
1. Import the LevelSelector class to your project and Create a new LevelSelector object.
2. Call LevelSelector.register() to attach it to the scene and display it on screen.