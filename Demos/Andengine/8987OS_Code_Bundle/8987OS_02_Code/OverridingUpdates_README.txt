This class includes two rectangles whose onManagedUpdate methods are overriden in order to inject code into the update thread the engine object.
The first rectangle is centered in the display region, rotating. The second rectangle moves across the screen until it reaches the opposite side, being relocated back to its original position (plus a few pixels in the y-axis). Eventually the two rectangles will collide, causing the collidesWith() method to fire as it is being called on every update of the game. When this occurs, one of the rectangles color changes temporarily.

Installation:
1. Add the OverridingUpdates class to your project. 
2. Replace your AndroidManifest.xml's startup activity with ".OverridingUpdates".