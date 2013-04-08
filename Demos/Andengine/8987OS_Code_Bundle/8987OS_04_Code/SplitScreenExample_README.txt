This activity is making use of the DoubleSceneSplitScreenEngine in order to apply two separate scenes on each half of the device's display. The Engine requires two cameras and two scenes to be applied to it.
Once we've setup the Engine with both cameras and scenes, we apply a rectangle to the center of each scene. From this point we can setup the scenes however we'd like, including separate touch listeners, entities, controllers, or whatever else we'd like each scene to display.

Installation:
1. Import the SplitScreenExample class to your project.
2. Replace your AndroidManifest.xml's startup activity with ".SplitScreenExample"
