1. In the ScreenCap activity, we're using a ScreenCapture entity object in order to capture the devices display. In this particular instance,
we're setting up a single rectangle (red), then attaching it to the scene. The screen capture object is always the final object attached
to a scene.
2. We proceed to "ensure" that our activity directory exists in the data folder of the android storage, creating it if it doesn't already exist.
3. Final step is to call the capture() method on the ScreenCapture object. We specify the area to capture along with the name of the file. We
must include a screen capture callback as well, which allows us to print logs if the capture is successful or if it has failed.

Installation:
1. Add the ScreenCap class to your project. 
2. We must allow the project access to write to external storage. Copy the following code into the AndroidManifest.xml directly above the "<application"
line:
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
3. Replace your AndroidManifest.xml's startup activity with ".ScreenCap".