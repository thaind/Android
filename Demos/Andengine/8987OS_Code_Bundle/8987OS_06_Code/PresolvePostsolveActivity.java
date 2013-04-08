public class PresolvePostsolveActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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


	Body dynamicBody;
	Body staticBody;
	FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(20f, 0.5f, 0.9f);
	Vector2 localMouseJointTarget = new Vector2();
	MouseJointDef mouseJointDef;
	MouseJoint mouseJoint;
	Body groundBody;

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


		Rectangle dynamicRect = new Rectangle(400f,60f,40f,40f,this.getEngine().getVertexBufferObjectManager());
		dynamicRect.setColor(0f, 0.6f, 0f);
		mScene.attachChild(dynamicRect);
		dynamicBody = PhysicsFactory.createBoxBody(mPhysicsWorld, dynamicRect, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dynamicRect, dynamicBody));

		Rectangle staticRect = new Rectangle(400f,240f,200f,10f,this.getEngine().getVertexBufferObjectManager());
		staticRect.setColor(0f, 0f, 0f);
		mScene.attachChild(staticRect);
		staticBody = PhysicsFactory.createBoxBody(mPhysicsWorld, staticRect, BodyType.StaticBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(staticRect, staticBody));

		mPhysicsWorld.setContactListener(new ContactListener(){
			float maxImpulse;
			@Override
			public void beginContact(Contact contact) {}

			@Override
			public void endContact(Contact contact) {}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				if(areBodiesContacted(dynamicBody, staticBody, contact))
					if(dynamicBody.getWorldCenter().y < staticBody.getWorldCenter().y)
						contact.setEnabled(false);
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				if(areBodiesContacted(dynamicBody, staticBody, contact)) {
					maxImpulse = impulse.getNormalImpulses()[0];
					for(int i = 1; i < impulse.getNormalImpulses().length; i++)
						maxImpulse = Math.max(impulse.getNormalImpulses()[i], maxImpulse);
					if(maxImpulse>400f)
						dynamicBody.setAngularVelocity(30f);
				}
			}
		});

		groundBody = mPhysicsWorld.createBody(new BodyDef());
		mouseJointDef = new MouseJointDef();
		mouseJointDef.bodyA = groundBody;
		mouseJointDef.bodyB = dynamicBody;
		mouseJointDef.dampingRatio = 0.5f;
		mouseJointDef.frequencyHz = 1f;
		mouseJointDef.maxForce = (40.0f * dynamicBody.getMass());
		mouseJointDef.collideConnected = false;


		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			mouseJointDef.target.set(dynamicBody.getWorldCenter());
			mouseJoint = (MouseJoint)mPhysicsWorld.createJoint(mouseJointDef);
			final Vector2 vec = Vector2Pool.obtain(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			mouseJoint.setTarget(vec);
			Vector2Pool.recycle(vec);
		} else if(pSceneTouchEvent.isActionMove()) {
			final Vector2 vec = Vector2Pool.obtain(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			mouseJoint.setTarget(vec);
			Vector2Pool.recycle(vec);
			return true;
		} else if(pSceneTouchEvent.isActionCancel() || pSceneTouchEvent.isActionOutside() || pSceneTouchEvent.isActionUp()) {
			mPhysicsWorld.destroyJoint(mouseJoint);
		}
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