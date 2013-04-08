public class WorkingWithParticles extends BaseGameActivity {

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
	
	// Particle texture region
	private ITextureRegion mParticleTextureRegion;
	
	//====================================================
	// CREATE ENGINE OPTIONS
	//====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), mCamera);

		return engineOptions;
	}

	//====================================================
	// CREATE RESOURCES
	//====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		
		mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, getAssets(), "marble.png");
		
		texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		texture.load();
		
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

		// Create the particle emitter
		PointParticleEmitter particleEmitter = new PointParticleEmitter(0, 0);
		
		// Create the particle system
		SpriteParticleSystem particleSystem = new SpriteParticleSystem(particleEmitter, 10, 20, 200, mParticleTextureRegion, mEngine.getVertexBufferObjectManager());
		
		// Setup particle initializers
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-50, 50, -10, -400));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(10, 15));
		
		// Setup particle modifiers
		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(3, 6, 0.2f, 2f, 0.2f, 2f));
		
		// Set the position in which particles will spawn
		particleEmitter.setCenter(WIDTH / 2, HEIGHT);
		
		// Attach our particle system to the scene
		mScene.attachChild(particleSystem);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}