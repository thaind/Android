public class SplashSceneActivity extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	
	// Instead of graphics, we'll be using these strings which will
	// represent our splash scene and menu scene
	public static final String SPLASH_STRING = "HELLO SPLASH SCREEN!";
	public static final String MENU_STRING = "HELLO MENU SCREEN!";
	
	//====================================================
	// VARIABLES
	//====================================================
	// We'll be creating 1 scene for our main menu and one for our splash image
	private Scene mMenuScene;
	private Scene mSplashScene;
	
	private Camera mCamera;
	
	// These two text objects will represent each of our two scenes to be displayed
	Text mSplashSceneText;
	Text mMenuSceneText;
	
	//====================================================
	// CREATE ENGINE OPTIONS
	//====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		mEngine = new FixedStepEngine(engineOptions, 60);
		
		return engineOptions;
	}

	//====================================================
	// CREATE RESOURCES
	//====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		
		// Load our fonts.
		ResourceManager.getInstance().loadFonts(mEngine, getAssets());

		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}


	//====================================================
	// CREATE SCENE
	//====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		
		// Retrieve our font from the resource manager
		Font font = ResourceManager.getInstance().mFont;
		
		// Set the location of our splash 'image' (text object in this case).
		// We can use FontUtils.measureText to retrieve the width of our text
		// object in order to properly format its position
		float x = WIDTH / 2 - FontUtils.measureText(font, SPLASH_STRING) / 2;
		float y = HEIGHT / 2 - font.getLineHeight() / 2;
		
		// Create our splash scene object
		mSplashScene = new Scene();
		// Create our splash screen text object
		mSplashSceneText = new Text(x, y, font, SPLASH_STRING, SPLASH_STRING.length(), mEngine.getVertexBufferObjectManager());
		// Attach the text object to our splash scene
		mSplashScene.attachChild(mSplashSceneText);

		// We must change the value of x depending on the length of our menu
		// string now in order to keep the text centered on-screen
		x = WIDTH / 2 - FontUtils.measureText(font, MENU_STRING) / 2;
		
		// Create our main menu scene
		mMenuScene = new Scene();
		// Create our menu screen text object
		mMenuSceneText = new Text(x, y, font, MENU_STRING, MENU_STRING.length(), mEngine.getVertexBufferObjectManager());
		// Attach the text object to our menu scene
		mMenuScene.attachChild(mMenuSceneText);
		
		// Finally, we must callback the initial scene to be loaded (splash scene)
		pOnCreateSceneCallback.onCreateSceneFinished(mSplashScene);
	}

	//====================================================
	// POPULATE SCENE
	//====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		
		// We will create a timer-handler to handle the duration
		// in which the splash screen is shown
		mEngine.registerUpdateHandler(new TimerHandler(4, new ITimerCallback(){
		
		// Override ITimerCallback's 'onTimePassed' method to allow
		// us to control what happens after the timer duration ends
		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			// When 4 seconds is up, switch to our menu scene
			mEngine.setScene(mMenuScene);
			}
		}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();	
	}
}