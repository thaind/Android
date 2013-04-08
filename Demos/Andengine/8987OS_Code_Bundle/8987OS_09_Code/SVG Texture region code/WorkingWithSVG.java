public class WorkingWithSVG extends SimpleBaseGameActivity{

	private static int WIDTH = 800;
	private static int HEIGHT = 480;
	
	private static final int SPRITE_SIZE = 256;
	private static final int SPRITE_COUNT = 3;
	
	private Scene mScene;
	private Camera mCamera;
	
	// Low resolution SVG texture region @ 32x32 pixels
	private ITextureRegion mLowResTextureRegion;
	// Medium resolution SVG texture region @ 128x128 pixels
	private ITextureRegion mMedResTextureRegion;
	// High resolution SVG texture region @ 256x256 pixels
	private ITextureRegion mHiResTextureRegion;
	
	@Override
	public EngineOptions onCreateEngineOptions() {

		float mScaleFactor = 1;
		
		// Obtain the device display metrics (dpi)
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		
		int deviceDpi = displayMetrics.densityDpi;
		
		switch(deviceDpi){
		case DisplayMetrics.DENSITY_LOW:
			// Scale factor already set to 1
			break;
			
		case DisplayMetrics.DENSITY_MEDIUM:
			// Increase scale to a suitable value for mid-size displays
			mScaleFactor = 1.5f;
			break;
			
		case DisplayMetrics.DENSITY_HIGH:
			// Increase scale to a suitable value for larger displays
			mScaleFactor = 2;
			break;
			
		case DisplayMetrics.DENSITY_XHIGH:
			// Increase scale to suitable value for largest displays
			mScaleFactor = 2.5f;
			break;
		
		default:
			// Scale factor already set to 1
			break;
		}
		
		SVGBitmapTextureAtlasTextureRegionFactory.setScaleFactor(mScaleFactor);
		
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() throws IOException {
		// Set the base path, where the SVG extension will search for graphics by default
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		//SVGBitmapTextureAtlasTextureRegionFactory.setScaleFactor(2);
		
		// Create a new buildable bitmap texture atlas to build and contain texture regions
		BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		
		// Create a low-res (32x32) texture region of svg_image.svg
		mLowResTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "svg_image.svg", 32,32);

		// Create a med-res (128x128) texture region of svg_image.svg
		mMedResTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "svg_image.svg", 128, 128);
		
		// Create a high-res (256x256) texture region of svg_image.svg
		mHiResTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "svg_image.svg", 256,256);

		try {
			// Build and load the bitmapTextureAtlas
			bitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,1));
			bitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected Scene onCreateScene() {
		mScene = new Scene();

		// Obtain the initial sprite's position
		float currentSpritePosition = (WIDTH / ((SPRITE_SIZE) * SPRITE_COUNT)) + SPRITE_SIZE * 0.5f;
		
		// Create & attach the low-res sprite to the scene (left side)
		Sprite lowResSprite = new Sprite(currentSpritePosition, HEIGHT / 2, SPRITE_SIZE, SPRITE_SIZE, mLowResTextureRegion, mEngine.getVertexBufferObjectManager());
		mScene.attachChild(lowResSprite);

		currentSpritePosition += SPRITE_SIZE;
		// Create & attach the med-res sprite to the scene (mid)
		Sprite medResSprite = new Sprite(currentSpritePosition, HEIGHT / 2, SPRITE_SIZE, SPRITE_SIZE, mMedResTextureRegion, mEngine.getVertexBufferObjectManager());
		mScene.attachChild(medResSprite);
		
		currentSpritePosition += SPRITE_SIZE;
		// Create & attach the high-res sprite to the scene (right side)
		Sprite hiResSprite = new Sprite(currentSpritePosition, HEIGHT / 2, SPRITE_SIZE, SPRITE_SIZE, mHiResTextureRegion, mEngine.getVertexBufferObjectManager()){

			// For added affect to show a true HD sprite, we'll enable dithering
			// on the high-res sprite to smooth out the colors
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				pGLState.enableDither();
				super.preDraw(pGLState, pCamera);
			}
			
			// As always, disable gl states when we're finished with them
			@Override
			protected void postDraw(GLState pGLState, Camera pCamera){
				pGLState.disableDither();
				super.postDraw(pGLState, pCamera);
			}
		};
		mScene.attachChild(hiResSprite);

		return mScene;
	}
}