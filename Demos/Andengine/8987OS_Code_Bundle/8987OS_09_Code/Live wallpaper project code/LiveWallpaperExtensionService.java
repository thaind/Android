public class LiveWallpaperExtensionService extends BaseLiveWallpaperService{

	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;
	
	// Particle system constant values
	private static final int PARTICLE_RATE_MINIMUM = 5;
	private static final int PARTICLE_RATE_MAXIMUM = 15;
	private static final int PARTICLE_MAXIMUM_COUNT = 35;
	private static final int PARTICLE_EXPIRE_TIME_MIN = 2;
	private static final int PARTICLE_EXPIRE_TIME_MAX = 5;
	
	private Camera mCamera;
	
	private Scene mScene;
	
	// Particle texture region
	private ITextureRegion mTextureRegion;
	
	// Particle system
	private SpriteParticleSystem mParticleSystem;
	private RectangleParticleEmitter mParticleEmitter;
	
	// This variable will be obtained via the preference file for this wallpaper
	private int mParticleSpeed;
	
	// These ratio variables will be used to keep proper scaling of entities
	// regardless of the screen orientation
	private float mRatioX;
	private float mRatioY;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		
		// Initialize the preference file, setting default values if the wallpaper
		// is executed for the first time
		LiveWallpaperPreferences.getInstance().initPreferences(this);
		
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		final BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 32, 32);
		
		// Create the texture region
		mTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "marble.png");
		
		try {
			// Build/load the bitmap texture atlas
			bitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,0,0));
			bitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene arg0, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		// Setup the particle emitter and the particle system
		mParticleEmitter = new RectangleParticleEmitter(WIDTH * 0.5f, HEIGHT, WIDTH, 0);
		mParticleSystem = new SpriteParticleSystem(mParticleEmitter, PARTICLE_RATE_MINIMUM, PARTICLE_RATE_MAXIMUM, PARTICLE_MAXIMUM_COUNT, mTextureRegion, mEngine.getVertexBufferObjectManager());

		// Add a few particle modifiers to the live wallpaper in order to
		// display the "live" effect of the wallpaper
		mParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(PARTICLE_EXPIRE_TIME_MIN, PARTICLE_EXPIRE_TIME_MAX));
		mParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-3, -3));
		
		// We are creating an IParticleModifier to handle the "adjustments" needed for
		// each individual particle. This includes setting their speed and scale
		mParticleSystem.addParticleModifier(new IParticleModifier<Sprite>(){

			@Override
			public void onInitializeParticle(Particle pParticle) {
				// Do nothing...
			}

			@Override
			public void onUpdateParticle(Particle pParticle) {
				
				// Get the particle's entity
				Entity entity = (Entity) pParticle.getEntity();
				
				// Get the particles physics handler
				final PhysicsHandler particlePhysicsHandler = pParticle.getPhysicsHandler();
				
				// Get the current Y velocity of the particle
				final float currentVelocityY = particlePhysicsHandler.getVelocityY();
				
				// If the particle's current Y velocity is not equal to our pre-determined particle
				// speed set by the preferences...
				if(currentVelocityY != mParticleSpeed){
					// Adjust the particle's velocity to the proper value
					particlePhysicsHandler.setVelocityY(mParticleSpeed);
				}
				
				// If the particle's scale is not equal to the current ratio
				if(entity.getScaleX() != mRatioX){
					// Re-scale the particle to better suit the current screen ratio
					entity.setScale(mRatioX, mRatioY);
				}
			}

		});
		
		// Attach the particle system to the scene
		mScene.attachChild(mParticleSystem);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected synchronized void onResume() {
		// Anytime the live wallpaper is resumed, we obtain the particle speed from
		// the preferences in case it has changed via the settings activity
		mParticleSpeed = LiveWallpaperPreferences.getInstance().getParticleSpeed();
		super.onResume();
	}

	// In the event that the surface changes, ie. user changes device orientation...
	@Override
	public void onSurfaceChanged(GLState pGLState, int pWidth, int pHeight) {

		// If the width is greater than the height (landscape mode)
		if(pWidth > pHeight){
			// The scaling should be equal to 1:1 since our camera's width and height
			// are based on landscape mode
			mRatioX = 1;
			mRatioY = 1;
		}
		
		// If the height is greater than the width (portrait mode) 
		else{
			// We must calculate a new scaling ratio for the particle's entities
			// in order to make up for the 'swapping' of camera width and height
			mRatioX = ((float)pHeight) / pWidth;
			mRatioY = ((float)pWidth) / pHeight;
		}
		
		super.onSurfaceChanged(pGLState, pWidth, pHeight);
	}
}
