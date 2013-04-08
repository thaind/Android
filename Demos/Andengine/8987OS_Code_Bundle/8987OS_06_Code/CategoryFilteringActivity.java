public class CategoryFilteringActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

	// ====================================================
	// CONSTANTS
	// ====================================================
	public static int cameraWidth = 800;
	public static int cameraHeight = 480;

	// ====================================================
	// VARIABLES
	// ====================================================
	public Scene mScene;
	public FixedStepPhysicsWorld mPhysicsWorld;
	public Body groundWallBody;
	public Body roofWallBody;
	public Body leftWallBody;
	public Body rightWallBody;

	private int mBodyCount = 0;
	public static final short CATEGORYBIT_DEFAULT = 1;
	public static final short CATEGORYBIT_RED_BOX = 2;
	public static final short CATEGORYBIT_GREEN_BOX = 4;
	public static final short MASKBITS_RED_BOX = CATEGORYBIT_DEFAULT + CATEGORYBIT_RED_BOX;
	public static final short MASKBITS_GREEN_BOX = CATEGORYBIT_DEFAULT + CATEGORYBIT_GREEN_BOX;
	public static final FixtureDef RED_BOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_RED_BOX, MASKBITS_RED_BOX, (short)0);
	public static final FixtureDef GREEN_BOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_GREEN_BOX, MASKBITS_GREEN_BOX, (short)0);


	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 60);
	}

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), new Camera(0, 0, cameraWidth, cameraHeight));
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// Setup the ResourceManager.
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();
		mScene.setBackground(new Background(0.9f,0.9f,0.9f));
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0f,-SensorManager.GRAVITY_EARTH*2), false, 8, 3);
		mScene.registerUpdateHandler(mPhysicsWorld);
		final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		final Rectangle ground = new Rectangle(cameraWidth / 2f, 6f, cameraWidth - 4f, 8f, this.getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(cameraWidth / 2f, cameraHeight - 6f, cameraWidth - 4f, 8f, this.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(6f, cameraHeight / 2f, 8f, cameraHeight - 4f, this.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(cameraWidth - 6f, cameraHeight / 2f, 8f, cameraHeight - 4f, this.getVertexBufferObjectManager());
		ground.setColor(0f, 0f, 0f);
		roof.setColor(0f, 0f, 0f);
		left.setColor(0f, 0f, 0f);
		right.setColor(0f, 0f, 0f);
		groundWallBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, WALL_FIXTURE_DEF);
		roofWallBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, WALL_FIXTURE_DEF);
		leftWallBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, WALL_FIXTURE_DEF);
		rightWallBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, WALL_FIXTURE_DEF);
		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(this.mPhysicsWorld != null)
			if(pSceneTouchEvent.isActionDown())
				this.addBody(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		return true;
	}

	private void addBody(final float pX, final float pY) {
		this.mBodyCount++;
		final Rectangle rectangle = new Rectangle(pX, pY, 50f, 50f, this.getVertexBufferObjectManager());
		rectangle.setAlpha(0.5f);
		final Body body;
		if(this.mBodyCount % 2 == 0) {
			rectangle.setColor(1f, 0f, 0f);
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, rectangle, BodyType.DynamicBody, RED_BOX_FIXTURE_DEF);
		} else {
			rectangle.setColor(0f, 1f, 0f);
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, rectangle, BodyType.DynamicBody, GREEN_BOX_FIXTURE_DEF);
		}
		this.mScene.attachChild(rectangle);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rectangle, body, true, true));
	}

	@Override
	public void onAccelerationAccuracyChanged(
			AccelerationData pAccelerationData) {}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();
		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();
		this.disableAccelerationSensor();
	}
}