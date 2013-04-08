In this class we are setting up a time limit (FPS_TIMER), an elapsed time counter (mCurrentTime), and an FPSCounter (mFpsCounter). These three objects are used within the overriden onManagedUpdate() method for the scene. mCurrentTime accumulates time passed until it reaches 5 seconds, printing a log of the current FPS, then resetting to 0 to start the counting over again.

Installation:
1. Add the FPSCounterDevelopment class to your project.
2. Replace your AndroidManifest.xml's startup activity with ".FPSCounterDevelopment".