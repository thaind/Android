public class ApplyingSpriteGroups extends BaseGameActivity {

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
	
	private SpriteGroup mSpriteGroup;
	
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	
	private ITextureRegion mTextureRegion;
	
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
		
		// Create texture atlas
		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 32, 32, TextureOptions.BILINEAR);

		// Create texture region
		mTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, getAssets(), "marble.png");
		
		// Build/load texture atlas
		mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		mBitmapTextureAtlas.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	//====================================================
	// CREATE SCENE
	//====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();
		
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	//====================================================
	// POPULATE SCENE
	//====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		// Create a new sprite group with a maximum sprite capacity of 500
		mSpriteGroup = new SpriteGroup(0, 0, mBitmapTextureAtlas, 500, mEngine.getVertexBufferObjectManager());
		
		// Attach the sprite group to the scene
		mScene.attachChild(mSpriteGroup);
		
		// Store our texture's width and height values
		final float spriteWidth = mTextureRegion.getWidth();
		final float spriteHeight = mTextureRegion.getHeight();
		
		// We'll use temp x/y values in order to setup our sprite positions
		float tempX = 0;
		float tempY = 0;
		
		// Determine how many sprites are needed to fill our rows and columns
		int spritesPerRow = (int) (WIDTH / mTextureRegion.getWidth());
		int spritesPerColumn = (int) (HEIGHT / mTextureRegion.getHeight());
		
		// Loop incrementing our tempY position
		for(int i = 0; i < spritesPerColumn; i++){

			// Loop incrementing our tempX position
			for(int o = 0; o < spritesPerRow; o++){

				// Set our new tempX position to its next position
				tempX = mTextureRegion.getWidth() * o;
				
				// Create new sprite
				Sprite sprite = new Sprite(tempX, tempY, spriteWidth, spriteHeight, mTextureRegion, mEngine.getVertexBufferObjectManager());

				// Attach our sprite to the sprite group
				mSpriteGroup.attachChild(sprite);
			}
			
			// Reset tempX for a new row
			tempX = 0;
			// Increment tempY position 
			tempY = tempY + mTextureRegion.getHeight();
		}
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}