public class StitchedBackground extends BaseGameActivity implements
		IOnSceneTouchListener, IPinchZoomDetectorListener {

	public static int WIDTH = 800;
	public static int HEIGHT = 480;

	private Scene mScene;
	private ZoomCamera mCamera;

	// Background texture regions
	private ITextureRegion mBackgroundLeftTextureRegion;
	private ITextureRegion mBackgroundRightTextureRegion;
	
	// Background sprites
	private Sprite mBackgroundLeftSprite;
	private Sprite mBackgroundRightSprite;

	// Zoom detector
	private PinchZoomDetector mPinchZoomDetector;
	
	private float mInitialTouchZoomFactor;

	// Initial scene touch coordinates on ACTION_DOWN
	private float mInitialTouchX;
	private float mInitialTouchY;

	@Override
	public EngineOptions onCreateEngineOptions() {

		// Create the zoom camera
		mCamera = new ZoomCamera(0, 0, WIDTH, HEIGHT);

		// Enable our level bounds so that we cant scroll too far
		mCamera.setBounds(0, 0, 1600, 480);
		mCamera.setBoundsEnabled(true);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// Set the base path
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		/* Create the background left texture atlas */
		BuildableBitmapTextureAtlas backgroundTextureLeft = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
		
		/* Create the background left texture region */
		mBackgroundLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureLeft, getAssets(),
						"background_left.png");
		
		/* Build and load the background left texture atlas */
		try {
			backgroundTextureLeft
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			backgroundTextureLeft.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		/* Create the background right texture atlas */
		BuildableBitmapTextureAtlas backgroundTextureRight = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 800, 480, TextureOptions.BILINEAR);

		/* Create the background right texture region */
		mBackgroundRightTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureRight, getAssets(),
						"background_right.png");

		/* Build and load the background right texture atlas */
		try {
			backgroundTextureRight
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		backgroundTextureRight.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		// Register this activity as our touch listener
		mScene.setOnSceneTouchListener(this);
		
		// Register this activity as our zoom detector listener and enable it
		mPinchZoomDetector = new PinchZoomDetector(this);
		mPinchZoomDetector.setEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		final int halfTextureWidth = (int) (mBackgroundLeftTextureRegion.getWidth() * 0.5f);
		final int halfTextureHeight = (int) (mBackgroundLeftTextureRegion.getHeight() * 0.5f);
		
		// Create left background sprite
		mBackgroundLeftSprite = new Sprite(halfTextureWidth, halfTextureHeight, mBackgroundLeftTextureRegion,
				mEngine.getVertexBufferObjectManager())
		;
		// Attach left background sprite to the background scene
		mScene.attachChild(mBackgroundLeftSprite);

		// Create the right background sprite, positioned directly to the right of the first segment
		mBackgroundRightSprite = new Sprite(mBackgroundLeftSprite.getX() + mBackgroundLeftTextureRegion.getWidth(),
				halfTextureHeight, mBackgroundRightTextureRegion,
				mEngine.getVertexBufferObjectManager());
		
		// Attach right background sprite to the background scene
		mScene.attachChild(mBackgroundRightSprite);

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		// Obtain the initial zoom factor on pinch detection
		mInitialTouchZoomFactor = mCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		// Calculate the zoom offset
		final float newZoomFactor = mInitialTouchZoomFactor * pZoomFactor;
		
		// Apply the zoom offset to the camera, allowing zooming of between (default) 1x and 2x
		if (newZoomFactor < 2f && newZoomFactor > 1f)
			mCamera.setZoomFactor(newZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		// Calculate the zoom offset
		final float newZoomFactor = mInitialTouchZoomFactor * pZoomFactor;
		
		// Apply the zoom offset to the camera, allowing zooming of between (default) 1x and 2x
		if (newZoomFactor < 2f && newZoomFactor > 1f)
			mCamera.setZoomFactor(newZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// Pass the touch events to the zoom detector
		mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if (pSceneTouchEvent.isActionDown()) {
			// Obtain the initial touch coordinates when the scene is first pressed
			mInitialTouchX = pSceneTouchEvent.getX();
			mInitialTouchY = pSceneTouchEvent.getY();
		}
		
		if(pSceneTouchEvent.isActionMove()){
		// Calculate the offset touch coordinates
		final float touchOffsetX = mInitialTouchX - pSceneTouchEvent.getX();
		final float touchOffsetY = mInitialTouchY - pSceneTouchEvent.getY();
		
		// Apply the offset touch coordinates to the current camera coordinates
		mCamera.setCenter(mCamera.getCenterX() + touchOffsetX,
				mCamera.getCenterY() + touchOffsetY);
		}
		return true;
	}
}