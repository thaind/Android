This activity applies a play/pause button to the scene. The activity includes a sound file which plays in a loop (menu music) until the activity is closed. Upon pressing the play/pause button, depending on the state of the music file, playback will either start or stop. The button will react to the touch event, displaying either a 'sound playing' image or a 'sound muted' image.

The music object is created using the MusicFactory class, easily allowing the creation of music objects to be used at any stage of an application.

Installation:
1. Import the MenuMusic class to your project and include a 100x50 "sound_button_tiles.png" image to the "assets/gfx/" folder of your project. The image should include a 50x50 square on each side of the sound_button_tiles.png, representing sound on/off buttons.
2. Create a "sfx" folder inside the project's automatically generated assets folder. Include an mp3 of your choice and rename "midnight-ride.mp3" in the activity to the file name of your music object. 
2. Replace your AndroidManifest.xml's startup activity with ".MenuMusic".