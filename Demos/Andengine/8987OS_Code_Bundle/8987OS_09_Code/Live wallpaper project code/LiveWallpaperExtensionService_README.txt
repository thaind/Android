The LiveWallpaperExtensionService is the class that handles the live wallpaper scene. This class is very-much similar to a BaseGameActivity class, except there's an additional step involved since we're using shared preferences to keep track of the custom live wallpaper values (user editable). onResume() of the AndEngine lifecycle (overridden in the class), we must load the particle speed preference value that we save via the settings activity. This value is used to adjust the speed of the live wallpaper's particles.

Installation:
1. Import the LiveWallpaperExtensionServer class and all associated class files into a new project.
2. Copy all the associated XML files into the new project.