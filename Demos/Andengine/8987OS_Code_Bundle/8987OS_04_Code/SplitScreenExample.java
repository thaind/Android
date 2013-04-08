public class SplitScreenExample extends BaseGameActivity{

	/* Since each half of the screen has its own camera,
	 * we will reduce the typical size of 800 pixels down to
	 * 400 for the WIDTH constant. HEIGHT remains the same*/
	public static final int WIDTH = 400;
	public static final int HEIGHT = 480;

	/* min/max zoom factors for the cameras auto-zooming functionality */
	private static final float MIN_ZOOM_FACTOR = 0.2f;
	private static final float MAX_ZOOM_FACTOR = 1.8f;
	
	/* We'll need two Scene's for the DoubleSceneSplitScreenEngine */
	private Scene mSceneOne;
	private Scene mSceneTwo;
	
	/* We'll also need two Camera's for the DoubleSceneSplitScreenEngine */
	private SmoothCamera mCameraOne;
	private SmoothCamera mCameraTwo;
	
	@Override
	public EngineOptions onCreateEngineOptions() {

		/* Create the first camera (Left half of the display) */
		mCameraOne = new SmoothCamera(0, 0, WIDTH, HEIGHT, 0, 0, 0.4f){
			
			/* During each update to the camera, we will determine whether
			 * or not to set a new zoom factor for this camera */
			@Override
			public void onUpdate(float pSecondsElapsed) {
				final float currentZoomFactor = this.getZoomFactor();
				
				if(currentZoomFactor >= MAX_ZOOM_FACTOR){
					this.setZoomFactor(MIN_ZOOM_FACTOR);
				}
				else if(currentZoomFactor <= MIN_ZOOM_FACTOR){
					this.setZoomFactor(MAX_ZOOM_FACTOR);
				}
				
				super.onUpdate(pSecondsElapsed);
			}
			
		};
		
		/* Set the initial zoom factor for camera one*/
		mCameraOne.setZoomFactor(MAX_ZOOM_FACTOR);
		
		/* Create the second camera (Right half of the display) */
		mCameraTwo = new SmoothCamera(0, 0, WIDTH, HEIGHT, 0, 0, 1.2f){
			
			/* During each update to the camera, we will determine whether
			 * or not to set a new zoom factor for this camera */
			@Override
			public void onUpdate(float pSecondsElapsed) {
				final float currentZoomFactor = this.getZoomFactor();
				
				if(currentZoomFactor >= MAX_ZOOM_FACTOR){
					this.setZoomFactor(MIN_ZOOM_FACTOR);
				}
				else if(currentZoomFactor <= MIN_ZOOM_FACTOR){
					this.setZoomFactor(MAX_ZOOM_FACTOR);
				}
				
				super.onUpdate(pSecondsElapsed);
			}
			
		};
		
		/* Set the initial zoom factor for camera two */
		mCameraTwo.setZoomFactor(MIN_ZOOM_FACTOR);
		
		/* The first camera is set via the EngineOptions creation, as usual */
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCameraOne);

		/* If users should be able to control each have of the display
		 *  simultaneously with touch events, we'll need to enable 
		 *  multi-touch in the engine options */
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		
		return engineOptions;
	}

	/* We must override the onCreateEngine() method of this activity in order
	 * to create the DoubleSceneSplitScreenEngine */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		
		/* Return the DoubleSceneSplitScreenEngine, passing the pEngineOptions
		 * as well as the second camera object. Remember, the first camera has
		 * already been applied to the engineOptions which in-turn applies the
		 * camera to the engine. */
		return new DoubleSceneSplitScreenEngine(pEngineOptions, mCameraTwo);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {

		/* Create and setup the first scene */
		mSceneOne = new Scene();
		mSceneOne.setBackground(new Background(0.5f, 0, 0));
		
		/* In order to keep our camera's and scenes organized, we can
		 * set the Scene's user data to store its own camera */
		mSceneOne.setUserData(mCameraOne);
		
		/* Create and setup the second scene */
		mSceneTwo = new Scene();
		mSceneTwo.setBackground(new Background(0,0,0.5f));
		
		/* Same as the first Scene, we set the second scene's user data
		 * to hold its own camera */
		mSceneTwo.setUserData(mCameraTwo);
		
		/* We must set the second scene within mEngine object manually.
		 * This does NOT need to be done with the first scene as we will
		 * be passing it to the onCreateSceneCallback, which passes it
		 * to the Engine object for us at the end of onCreateScene()*/
		((DoubleSceneSplitScreenEngine) mEngine).setSecondScene(mSceneTwo);

		/* Pass the first Scene to the engine */
		pOnCreateSceneCallback.onCreateSceneFinished(mSceneOne);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		final int rectangleDimensions = 100;
		
		/* Apply a rectangle to the center of the first scene */
		Rectangle rectangleOne = new Rectangle(WIDTH * 0.5f, HEIGHT * 0.5f, rectangleDimensions, rectangleDimensions, mEngine.getVertexBufferObjectManager());
		rectangleOne.setColor(org.andengine.util.adt.color.Color.BLUE);
		mSceneOne.attachChild(rectangleOne);
		
		/* Apply a rectangle to the center of the second scene */
		Rectangle rectangleTwo = new Rectangle(WIDTH * 0.5f, HEIGHT * 0.5f, rectangleDimensions, rectangleDimensions, mEngine.getVertexBufferObjectManager());
		rectangleTwo.setColor(org.andengine.util.adt.color.Color.RED);
		mSceneTwo.attachChild(rectangleTwo);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}