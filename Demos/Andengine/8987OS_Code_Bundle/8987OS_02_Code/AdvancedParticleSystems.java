public class AdvancedParticleSystems extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	public static final float HEIGHT_OFFSET = (HEIGHT - 50) / 9;
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;

	// Particle texture region
	private ITextureRegion mParticleTextureRegion;

	// Modifier x coordinates
	private final float pointsX[] = { 
			WIDTH / 2 - 90, // x1
			WIDTH / 2 + 90, // x2
			WIDTH / 2 - 180, // x3
			WIDTH / 2 + 180, // x4
			WIDTH / 2 - 40, // x5
			WIDTH / 2 + 40, // x6
			WIDTH / 2 - 100, // x7
			WIDTH / 2 + 100  // x8
			};

	// Modifier y coordinates
	private final float pointsY[] = { 
			HEIGHT - (HEIGHT_OFFSET * 2), // y1
			HEIGHT - (HEIGHT_OFFSET * 3), // y2
			HEIGHT - (HEIGHT_OFFSET * 4), // y3
			HEIGHT - (HEIGHT_OFFSET * 5), // y4
			HEIGHT - (HEIGHT_OFFSET * 6), // y5
			HEIGHT - (HEIGHT_OFFSET * 7), // y6
			HEIGHT - (HEIGHT_OFFSET * 8), // y7
			HEIGHT - (HEIGHT_OFFSET * 9), // y8
			};

	// Create the cardinal spline modifier config
	final CardinalSplineMoveModifierConfig mConfig = new CardinalSplineMoveModifierConfig(pointsX.length, 0);

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

		BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 66, 66, TextureOptions.NEAREST);

		// 64x64 particle image
		mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory
		.createFromAsset(texture, getAssets(), "particle.png");

		try {
			texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
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

		// Apply the float array coordinates to the modifier config
		for (int i = 0; i < pointsX.length; i++) {
			mConfig.setControlPoint(i, pointsX[i], pointsY[i]);
		}

		// Create the particle emitter (bottom/center of the screen)
		PointParticleEmitter particleEmitter = new PointParticleEmitter(WIDTH / 2, HEIGHT);

		// Create a batched particle system for efficiency
		BatchedSpriteParticleSystem particleSystem = new BatchedSpriteParticleSystem(
				particleEmitter, 1, 2, 20, mParticleTextureRegion,
				mEngine.getVertexBufferObjectManager());

		// Initialize the sprite's color (random)
		particleSystem.addParticleInitializer(new ColorParticleInitializer<UncoloredSprite>(0, 1, 0, 1, 0, 1));
		
		// Add the expire modifier (particles expire after 10 seconds)
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(10));

		// Add 4 sequential scale modifiers
		particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(
				1, 2, 1.3f, 0.4f));
		particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(
				3, 4, 0.4f, 1.3f));
		particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(
				5, 6, 1.3f, .4f));
		particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(
				7, 9, 0.4f, 1.3f));

		// Add alpha ('fade out') modifier
		particleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(
				9, 10, 1, 0));

		// Create a custom particle modifier via the Particle Modifier interface
		particleSystem.addParticleModifier(new IParticleModifier<UncoloredSprite>() {
			
			// temporary particle color values
			float red;
			float green;
			float blue;

			// color check booleans
			boolean incrementRed = true;
			boolean incrementGreen = true;
			boolean incrementBlue = true;

			// Called when a particle is created
			@Override
			public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
				// Create our movement modifier
				CardinalSplineMoveModifier moveModifier = new CardinalSplineMoveModifier(10, mConfig);
				
				// Register our modifier to each individual particle
				pParticle.getEntity().registerEntityModifier(moveModifier);
			}

			// Called when a particle is updated (every frame)
			@Override
			public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
				// Get the particle's sprite/entity
				UncoloredSprite sprite = pParticle.getEntity();
				
				// Get the particle's current color values
				red = sprite.getRed();
				green = sprite.getGreen();
				blue = sprite.getBlue();
				
				// Red reversion checks
				if (red >= 0.75f)
					incrementRed = false;
				else if (red <= 0.3f)
					incrementRed = true;
				// Green reversion checks
				if (green >= 0.75f)
					incrementGreen = false;
				else if (green <= 0.3f)
					incrementGreen = true;
				// Blue reversion checks
				if (blue >= 0.75f)
					incrementBlue = false;
				else if (blue <= 0.3f)
					incrementBlue = true;

				// Inc/dec red value
				if (incrementRed)
							red += 0.075f;
				else
							red -= 0.075f;
				// Inc/dec green value
				if (incrementGreen)
							green += 0.075f;
				else
							green -= 0.075f;
				// Inc/dec blue value
				if (incrementBlue)
							blue += 0.075f;
				else
							blue -= 0.075f;

				// Set the new color values for the particle's sprite
				sprite.setColor(red, green, blue);
			}

		});

		// Attach our particle system to the scene
		mScene.attachChild(particleSystem);

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}