public class ContactActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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

	public Rectangle dynamicRect;
	public Rectangle staticRect;
	public Body dynamicBody;
	public Body staticBody;
	public boolean setFullAlphaForDynamicBody = false;
	public boolean setHalfAlphaForDynamicBody = false;
	public boolean setFullAlphaForStaticBody = false;
	public boolean setHalfAlphaForStaticBody = false;
	final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(2f, 0f, 0.9f);

	public boolean isBodyContacted(Body pBody, Contact pContact)
	{
		if(pContact.getFixtureA().getBody().equals(pBody) ||
				pContact.getFixtureB().getBody().equals(pBody))
			return true;
		return false;
	}

	public boolean areBodiesContacted(Body pBody1, Body pBody2, Contact pContact)
	{
		if(pContact.getFixtureA().getBody().equals(pBody1) ||
				pContact.getFixtureB().getBody().equals(pBody1))
			if(pContact.getFixtureA().getBody().equals(pBody2) ||
					pContact.getFixtureB().getBody().equals(pBody2))
				return true;
		return false;
	}



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

		dynamicRect = new Rectangle(300f,240f,100f,100f,this.getEngine().getVertexBufferObjectManager());
		dynamicRect.setColor(0f, 0.7f, 0f);
		dynamicRect.setAlpha(0.5f);
		mScene.attachChild(dynamicRect);
		dynamicBody = PhysicsFactory.createBoxBody(mPhysicsWorld, dynamicRect, BodyType.DynamicBody, boxFixtureDef);
		dynamicBody.setLinearDamping(0.4f);
		dynamicBody.setAngularDamping(0.6f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dynamicRect, dynamicBody));


		staticRect = new Rectangle(500f,240f,100f,100f,this.getEngine().getVertexBufferObjectManager());
		staticRect.setColor(0f, 0f, 0.7f);
		staticRect.setAlpha(0.5f);
		mScene.attachChild(staticRect);
		staticBody = PhysicsFactory.createBoxBody(mPhysicsWorld, staticRect, BodyType.StaticBody, boxFixtureDef);

		mPhysicsWorld.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				if(contact.isTouching())
					if(areBodiesContacted(staticBody,dynamicBody,contact))
						setFullAlphaForStaticBody = true;
				if(isBodyContacted(dynamicBody,contact))
					setFullAlphaForDynamicBody = true;
			}

			@Override
			public void endContact(Contact contact) {
				if(areBodiesContacted(staticBody,dynamicBody,contact))
					setHalfAlphaForStaticBody = true;
				if(isBodyContacted(dynamicBody,contact))
					setHalfAlphaForDynamicBody = true;
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});

		mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(setFullAlphaForDynamicBody) {
					dynamicRect.setAlpha(1f);
					setFullAlphaForDynamicBody = false;
				} else if(setHalfAlphaForDynamicBody) {
					dynamicRect.setAlpha(0.5f);
					setHalfAlphaForDynamicBody = false;
				}
				if(setFullAlphaForStaticBody) {
					staticRect.setAlpha(1f);
					setFullAlphaForStaticBody = false;
				} else if(setHalfAlphaForStaticBody) {
					staticRect.setAlpha(0.5f);
					setHalfAlphaForStaticBody = false;
				}
			}
			@Override public void reset() {}
		});

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