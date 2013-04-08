public class ApplyingPinchToZoom extends BaseGameActivity implements
		IOnSceneTouchListener, IPinchZoomDetectorListener {

	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	
	private static final int RECTANGLE_DIMENSIONS = 200;
	
	// It is a good idea to place limits on zoom functionality
	private static final float MIN_ZOOM_FACTOR = 0.5f;
	private static final float MAX_ZOOM_FACTOR = 1.5f;

	private Scene mScene;
	private ZoomCamera mCamera;

	// This object will handle the zooming pending touch
	private PinchZoomDetector mPinchZoomDetector;
	
	private float mInitialTouchZoomFactor;

	@Override
	public EngineOptions onCreateEngineOptions() {

		// We need to use zoom or smooth camera
		mCamera = new ZoomCamera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		/* Set the scene to listen for touch events using
		 * this activity's listener */
		mScene.setOnSceneTouchListener(this);

		/* Create and set the zoom detector to listen for 
		 * touch events using this activity's listener */
		mPinchZoomDetector = new PinchZoomDetector(this);
		
		// Enable the zoom detector
		mPinchZoomDetector.setEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		/* A rectangle is set on the scene in order to help us visualize
		 * the zoom factor changes upon pinching the display */
		final float x = WIDTH * 0.5f;
		final float y = HEIGHT * 0.5f;

		Rectangle rectangle = new Rectangle(x, y, RECTANGLE_DIMENSIONS, RECTANGLE_DIMENSIONS,
				mEngine.getVertexBufferObjectManager());
		mScene.attachChild(rectangle);

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// Pass scene touch events to the pinch zoom detector
		mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);
		return true;
	}
	
	/* This method is fired when two fingers press down
	 * on the display */
	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		// On first detection of pinch zooming, obtain the initial zoom factor
		mInitialTouchZoomFactor = mCamera.getZoomFactor();
	}
	
	/* This method is fired when two fingers are being moved
	 * around on the display, ie. in a pinching motion */
	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		
		/* On every sub-sequent touch event (after the initial touch) we offset
		 * the initial camera zoom factor by the zoom factor calculated by
		 * pinch-zooming */
		final float newZoomFactor = mInitialTouchZoomFactor * pZoomFactor;
		
		// If the camera is within zooming bounds
		if(newZoomFactor < MAX_ZOOM_FACTOR && newZoomFactor > MIN_ZOOM_FACTOR){
			// Set the new zoom factor
			mCamera.setZoomFactor(newZoomFactor);
		}
	}
	
	/* This method is fired when fingers are lifted from the screen */
	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		
		// Set the zoom factor one last time upon ending the pinch-to-zoom functionality
		final float newZoomFactor = mInitialTouchZoomFactor * pZoomFactor;
		
		// If the camera is within zooming bounds
		if(newZoomFactor < MAX_ZOOM_FACTOR && newZoomFactor > MIN_ZOOM_FACTOR){
			// Set the new zoom factor
			mCamera.setZoomFactor(newZoomFactor);
		}
	}
}