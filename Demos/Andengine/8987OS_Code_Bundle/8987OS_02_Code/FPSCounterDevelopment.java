public class FPSCounterDevelopment extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	// FPS display time limit
	public static final int FPS_TIMER = 5;
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;
	
	// Create our FPS Counter which keeps the devices framerate
	FPSCounter mFpsCounter = new FPSCounter();
	
	// Float which will keep track of our log timer
	private float mCurrentTime = 0;
	
	//====================================================
	// CREATE ENGINE OPTIONS
	//====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);

		return engineOptions;
	}

	//====================================================
	// CREATE RESOURCES
	//====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	//====================================================
	// CREATE SCENE
	//====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		// Register our handler to the update thread
		mEngine.registerUpdateHandler(mFpsCounter);
		
		mScene = new Scene(){
			// Override our scene's update handler to include additional functionality
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				
				// Accumulate current time passed
				mCurrentTime += pSecondsElapsed;
				
				// If current time passed greater or equal to timer limit (5 seconds)
				if(mCurrentTime >= FPS_TIMER){
					// Display our FPS in logcat every 5 seconds
					Log.i("FPS", String.valueOf(mFpsCounter.getFPS()));
					
					// Reset our current time passed variable
					mCurrentTime = 0;
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
			
		};
		
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	//====================================================
	// POPULATE SCENE
	//====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}