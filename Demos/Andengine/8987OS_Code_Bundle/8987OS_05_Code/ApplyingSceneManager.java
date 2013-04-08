public class ApplyingSceneManager extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================
	// We define these constants to setup the game to use an
	// appropriate camera resolution independent of the actual
	// end-user's screen resolution.
	
	// The resolution of the screen with which you are developing.
	static float DESIGN_SCREEN_WIDTH_PIXELS = 800f;
	static float DESIGN_SCREEN_HEIGHT_PIXELS = 480f;
	// The physical size of the screen with which you are developing.
	static float DESIGN_SCREEN_WIDTH_INCHES = 4.472441f;
	static float DESIGN_SCREEN_HEIGHT_INCHES = 2.805118f;
	// Define a minimum and maximum screen resolution (to prevent
	// cramped or overlapping screen elements).
	static float MIN_WIDTH_PIXELS = 320f, MIN_HEIGHT_PIXELS = 240f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	// These variables will be set in onCreateEngineOptions().
	public float cameraWidth;
	public float cameraHeight;
	public float actualScreenWidthInches;
	public float actualScreenHeightInches;
	
	// If a Layer is open when the Back button is pressed, hide the layer.
	// If a Game scene or non-MainMenu is active, go back to the MainMenu.
	// Otherwise, exit the game.
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(ResourceManager.getInstance().engine!=null){
				if(SceneManager.getInstance().isLayerShown)
					SceneManager.getInstance().currentLayer.onHideLayer();
				else if(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class) || 
						(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedMenuScene.class) &!
							SceneManager.getInstance().mCurrentScene.getClass().equals(MainMenu.class)))
					SceneManager.getInstance().showMainMenu();
				else
					System.exit(0);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		// Determine the device's physical screen size.
		actualScreenWidthInches = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().xdpi;
		actualScreenHeightInches = getResources().getDisplayMetrics().heightPixels / getResources().getDisplayMetrics().ydpi;
		// Set the Camera's Width & Height according to the device with which you design the game.
		cameraWidth = Math.round(Math.max(Math.min(DESIGN_SCREEN_WIDTH_PIXELS * (actualScreenWidthInches / DESIGN_SCREEN_WIDTH_INCHES),MAX_WIDTH_PIXELS),MIN_WIDTH_PIXELS));
		cameraHeight = Math.round(Math.max(Math.min(DESIGN_SCREEN_HEIGHT_PIXELS * (actualScreenHeightInches / DESIGN_SCREEN_HEIGHT_INCHES),MAX_HEIGHT_PIXELS),MIN_HEIGHT_PIXELS));
		// Create the EngineOptions.
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), new Camera(0, 0, cameraWidth, cameraHeight));
		// Enable sounds.
		engineOptions.getAudioOptions().setNeedsSound(true);
		// Enable music.
		engineOptions.getAudioOptions().setNeedsMusic(true);
		// Turn on Dithering to smooth texture gradients.
		engineOptions.getRenderOptions().setDithering(true);
		// Turn on MultiSampling to smooth the alias of hard-edge elements.
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		// Set the Wake Lock options to prevent the engine from dumping textures when focus changes.
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}
	
	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// Setup the ResourceManager.
		ResourceManager.getInstance().setup(this.getEngine(), this.getApplicationContext(), cameraWidth, cameraHeight, cameraWidth/DESIGN_SCREEN_WIDTH_PIXELS, cameraHeight/DESIGN_SCREEN_HEIGHT_PIXELS);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		// Register an FPSLogger to output the game's FPS during development.
		mEngine.registerUpdateHandler(new FPSLogger());
		// Tell the SceneManager to show the MainMenu.
		SceneManager.getInstance().showMainMenu();
		// Set the MainMenu to the Engine's scene.
		pOnCreateSceneCallback.onCreateSceneFinished(MainMenu.getInstance());
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		// Our SceneManager will handle the population of the scenes, so we do nothing here.
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}