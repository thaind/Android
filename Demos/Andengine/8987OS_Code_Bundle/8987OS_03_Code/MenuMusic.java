public class MenuMusic extends BaseGameActivity {

	public static int WIDTH = 800;
	public static int HEIGHT = 480;

	public static final int MUTE = 0;
	public static final int UNMUTE = 1;

	private Scene mScene;
	private Camera mCamera;

	private TiledSprite mMuteButton;

	/* Music object containing a sound file of the music to be played */
	private Music mMenuMusic;

	private ITiledTextureRegion mButtonTextureRegion;

	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		/* Tell the engineOptions that we want to play music */
		engineOptions.getAudioOptions().setNeedsMusic(true);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		/*
		 * Create the bitmap texture atlas.
		 * 
		 * The bitmap texture atlas is created to fit a texture region of 100x50
		 * pixels
		 */
		BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 100, 50);

		/* Create the buttons texture region with 2 columns, 1 row */
		mButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(bitmapTextureAtlas, getAssets(),
						"sound_button_tiles.png", 2, 1);

		/* Build the bitmap texture atlas */
		try {
			bitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		/* Load the bitmap texture atlas */
		bitmapTextureAtlas.load();

		/* Set the asset path for our sound files to "assets/sfx/" */
		MusicFactory.setAssetBasePath("sfx/");

		/* Create the mMenuMusic object from the specified mp3 file */
		try {
			mMenuMusic = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "menu_music.mp3");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		final float buttonX = (int) ((int) mButtonTextureRegion.getWidth() * 0.5f);
		final float buttonY = buttonX;

		/* Create the music mute/unmute button */
		mMuteButton = new TiledSprite(buttonX, buttonY, mButtonTextureRegion,
				mEngine.getVertexBufferObjectManager()) {

			/*
			 * Override the onAreaTouched() method allowing us to define custom
			 * actions
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				/* In the event the mute button is pressed down on... */
				if (pSceneTouchEvent.isActionDown()) {
					if (mMenuMusic.isPlaying()) {
						/*
						 * If music is playing, pause it and set tile index to
						 * MUTE
						 */
						this.setCurrentTileIndex(MUTE);
						mMenuMusic.pause();
					} else {
						/*
						 * If music is paused, play it and set tile index to
						 * UNMUTE
						 */
						this.setCurrentTileIndex(UNMUTE);
						mMenuMusic.play();
					}
					return true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};

		/* Set the current tile index to unmuted on application startup */
		mMuteButton.setCurrentTileIndex(UNMUTE);

		/* Register and attach the mMuteButton to the Scene */
		mScene.registerTouchArea(mMuteButton);
		mScene.attachChild(mMuteButton);

		/* Set the mMenuMusic object to loop once it reaches the track's end */
		mMenuMusic.setLooping(true);
		/* Play the mMenuMusic object */
		mMenuMusic.play();

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		
		/* If the music and button have been created */
		if (mMenuMusic != null && mMuteButton != null) {
			/* If the mMuteButton is set to unmuted on resume... */
			if(mMuteButton.getCurrentTileIndex() == UNMUTE){
				/* Play the menu music */
				mMenuMusic.play();
			}
		}
	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		
		/* Always pause the music on pause */
		if(mMenuMusic != null && mMenuMusic.isPlaying()){
			mMenuMusic.pause();
		}
	}
}