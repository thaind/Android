public class ForcesVelocitiesTorqueActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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


	Body LinearForceBody;
	Body LinearImpulseBody;
	Body LinearVelocityBody;

	Body AngularTorqueBody;
	Body AngularImpulseBody;
	Body AngularVelocityBody;


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



		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(2f, 1f, 0.9f);

		Rectangle LinearForceRect = new Rectangle(114f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					LinearForceBody.applyForce(0f, 2000f, LinearForceBody.getWorldCenter().x, LinearForceBody.getWorldCenter().y);
				return true;
			}
		};
		LinearForceRect.setColor(0.7f, 0f, 0f);
		mScene.attachChild(LinearForceRect);
		mScene.registerTouchArea(LinearForceRect);
		LinearForceBody = PhysicsFactory.createBoxBody(mPhysicsWorld, LinearForceRect, BodyType.DynamicBody, boxFixtureDef);
		LinearForceBody.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(LinearForceRect, LinearForceBody));


		Rectangle LinearImpulseRect = new Rectangle(228f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					LinearImpulseBody.applyLinearImpulse(0f, 200f, LinearImpulseBody.getWorldCenter().x, LinearImpulseBody.getWorldCenter().y);
				return true;
			}
		};
		LinearImpulseRect.setColor(0f, 0.7f, 0f);
		mScene.attachChild(LinearImpulseRect);
		mScene.registerTouchArea(LinearImpulseRect);
		LinearImpulseBody = PhysicsFactory.createBoxBody(mPhysicsWorld, LinearImpulseRect, BodyType.DynamicBody, boxFixtureDef);
		LinearImpulseBody.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(LinearImpulseRect, LinearImpulseBody));


		Rectangle LinearVelocityRect = new Rectangle(342f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					LinearVelocityBody.setLinearVelocity(0f, 20f);
				return true;
			}
		};
		LinearVelocityRect.setColor(0f, 0f, 0.7f);
		mScene.attachChild(LinearVelocityRect);
		mScene.registerTouchArea(LinearVelocityRect);
		LinearVelocityBody = PhysicsFactory.createBoxBody(mPhysicsWorld, LinearVelocityRect, BodyType.DynamicBody, boxFixtureDef);
		LinearVelocityBody.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(LinearVelocityRect, LinearVelocityBody));


		Rectangle AngularTorqueRect = new Rectangle(456f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					AngularTorqueBody.applyTorque(2000f);
				return true;
			}
		};
		AngularTorqueRect.setColor(0.7f, 0.7f, 0f);
		mScene.attachChild(AngularTorqueRect);
		mScene.registerTouchArea(AngularTorqueRect);
		AngularTorqueBody = PhysicsFactory.createBoxBody(mPhysicsWorld, AngularTorqueRect, BodyType.DynamicBody, boxFixtureDef);
		AngularTorqueBody.setAngularDamping(1f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(AngularTorqueRect, AngularTorqueBody));


		Rectangle AngularImpulseRect = new Rectangle(570f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					AngularImpulseBody.applyAngularImpulse(20f);
				return true;
			}
		};
		AngularImpulseRect.setColor(0f, 0.7f, 0.7f);
		mScene.attachChild(AngularImpulseRect);
		mScene.registerTouchArea(AngularImpulseRect);
		AngularImpulseBody = PhysicsFactory.createBoxBody(mPhysicsWorld, AngularImpulseRect, BodyType.DynamicBody, boxFixtureDef);
		AngularImpulseBody.setAngularDamping(1f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(AngularImpulseRect, AngularImpulseBody));


		Rectangle AngularVelocityRect = new Rectangle(684f,240f,60f,60f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					AngularVelocityBody.setAngularVelocity(10f);
				return true;
			}
		};
		AngularVelocityRect.setColor(0.7f, 0f, 0.7f);
		mScene.attachChild(AngularVelocityRect);
		mScene.registerTouchArea(AngularVelocityRect);
		AngularVelocityBody = PhysicsFactory.createBoxBody(mPhysicsWorld, AngularVelocityRect, BodyType.DynamicBody, boxFixtureDef);
		AngularVelocityBody.setAngularDamping(1f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(AngularVelocityRect, AngularVelocityBody));


		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return true;
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