public class ScreenCap extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final String TAG = "ScreenCap";
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;
	
	// Create our ScreenCapture entity
	ScreenCapture mScreenCapture = new ScreenCapture();
	
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
		mScene = new Scene();
		
		// Create a rectangle in the middle of our scene
		Rectangle rectangle = new Rectangle(WIDTH / 2 - 100, HEIGHT / 2 - 100, 200, 200, mEngine.getVertexBufferObjectManager());
		rectangle.setColor(Color.RED);
		
		mScene.attachChild(rectangle);
		
		// Our ScreenCapture object must be attached to the scene (it is an entity!)
		mScene.attachChild(mScreenCapture);

		// Create our app directory if it does not already exist
		FileUtils.ensureDirectoriesExistOnExternalStorage(this, "");
		
		// Capture the screen
		mScreenCapture.capture(800, 480, FileUtils.getAbsolutePathOnExternalStorage(this, "name" + ".png"),new IScreenCaptureCallback(){

			@Override
			public void onScreenCaptured(String pFilePath) {
				
				// ScreenCap Successful!
				Log.i(TAG, "Successfully saved to: " + pFilePath);
			}

			@Override
			public void onScreenCaptureFailed(String pFilePath,
					Exception pException) {
				
				// ScreenCap Failed!
				Log.e(TAG, pFilePath + " : " + pException.getLocalizedMessage());
			}
		});
		
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