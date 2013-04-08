The LiveWallpaperSettings activity is the activity that handles user-input to modify values in the LiveWallpaperPreferences. In this activity, we are creating a basic android layout consisting of a text view and a seekbar. The user is able to touch&drag the seekbar while the text view updates with a value in relation to the position of the seekbar's progress. The value of the seekbar is then saved to the preference file once the settings activity's onPause() lifecycle method is called.

Installation:
1. Include this class in the LiveWallpaperExtensionService project.