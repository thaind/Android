public class SoundsAndMusic {
	
	//====================================================
	// VARIABLES
	//====================================================
	public static Sound mButtonClickSound;
	
	public static Music mMenuThemeMusic;
	
	//====================================================
	// CONSTRUCTOR
	//====================================================
	SoundsAndMusic(){
	}

	//====================================================
	// METHODS
	//====================================================
	public static void loadSounds(Engine engine, Context context){
		// Set the base path for music files in the assets/sounds source folder
		SoundFactory.setAssetBasePath("sounds/");
		try {
			// Here we will create our sound file through the use of AndEngine's SoundFactory
			mButtonClickSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "button_click.mp3");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set the base path for music files in the assets/music source folder
		MusicFactory.setAssetBasePath("music/");
		try {
			// Here we will create our sound file through the use of AndEngine's MusicFactory
			mMenuThemeMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "theme_music.mp3");
			
			// Set theme music looping to true so it starts over when the mp3 reaches its duration
			mMenuThemeMusic.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}