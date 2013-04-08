1. The RelativeRotation class makes use of the IOnSceneTouchListener class. We use the touch listener in order to apply movement to a marble
which will be automatically pointed at by an arrow found in the center of the display region. 
2. Both the marble and arrow require Sprite objects along with ITextureRegion objects. The texture region's are loaded through the onCreateResources() method of the activity.
3. After allowing the activity to implement the touch listener, in onCreateScene() we must set our scene to listen for touch events through "this" activity.
4. Apply our objects to the scene in the onPopulateScene() method.
5. In the touch listener, we're executing updates of the marble's position along with the arrows rotation (based on the marbles position) if 
a finger is moved around on the touch screen.

Installation:
1. Add the RelativeRotation class to your project, including "arrow.png" and "marble.png" assets in the "gfx/" folder of the project. If this folder does not currently exist in the project's eclipse-generated assets folder, create it and add the two images. (The arrow is 59x30 pixels, the marble is 31x30).
2. The arrow's point should be facing to the left in the image.
2. Replace your AndroidManifest.xml's startup activity with ".RelativeRotation".