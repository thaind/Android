The OverridingDeviceKeys activity applies four layers (full-screen rectangles) to the scene. Upon pressing the device's physical 'back' key, one per press will be removed from the scene via the onKeyDown() overridden Activity method. Once all layers are removed, a dialog box prompts the user to either exit the application or keep the application open.

Installation:
1. Import the OverridingDeviceKeys class to your project.
2. Replace your AndroidManifest.xml's startup activity with ".OverridingDeviceKeys".