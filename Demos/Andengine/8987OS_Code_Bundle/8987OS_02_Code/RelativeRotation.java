public class RelativeRotation extends BaseGameActivity implements IOnSceneTouchListener{
	/* This activity takes advantage of being a touch listener
	   by adding 'implements IOnSceneTouchListener' to our class above */

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;

	private Sprite mArrowSprite;
	private Sprite mMarbleSprite;

	private ITextureRegion mArrowTextureRegion;
	private ITextureRegion mMarbleTextureRegion;
	
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
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		
		mArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "arrow.png");
		mMarbleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "marble.png");
		
		mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
		mBitmapTextureAtlas.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	//====================================================
	// CREATE SCENE
	//====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();
		
		// Allow our scene to register this activities touch events
		mScene.setOnSceneTouchListener(this);
		
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	//====================================================
	// POPULATE SCENE
	//====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		// Create our marble sprite in the top left corner of the screen initially.
		mMarbleSprite = new Sprite(0, 0, mMarbleTextureRegion, mEngine.getVertexBufferObjectManager());
		mScene.attachChild(mMarbleSprite);
		
		// Set our arrow sprite's position to the center of the screen
		final float x = (WIDTH / 2) - (mArrowTextureRegion.getWidth() / 2);
		final float y = (HEIGHT / 2) - (mArrowTextureRegion.getHeight() / 2);
		
		// Create and attach our arrow sprite to the scene
		mArrowSprite = new Sprite(x, y, mArrowTextureRegion, mEngine.getVertexBufferObjectManager());
		mScene.attachChild(mArrowSprite);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// We can override the onSceneTouchEvent() method since our activity
	// now implements the IOnSceneTouchListener
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// If a user moves their finger on the device
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE){
			
			// Set our marble's position to the touched area on the screen
			mMarbleSprite.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			
			// Calculate the difference between the sprites x and y values.
			final float dX = (mArrowSprite.getX() + (mArrowSprite.getWidth() / 2)) - (mMarbleSprite.getX() + (mMarbleSprite.getWidth() / 2));
			final float dY = (mArrowSprite.getY() + (mArrowSprite.getHeight() / 2)) - (mMarbleSprite.getY() + (mMarbleSprite.getHeight() / 2)) ;
			
			// We can use the atan2 function to find the angle
			// Additionally, OpenGL works with degrees so we must convert
			// from radians
			final float rotation = MathUtils.radToDeg((float) Math.atan2(dY, dX));
			
			// Set the new rotation for the arrow
			mArrowSprite.setRotation(rotation);
		}
		return false;
	}
}