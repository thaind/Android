public class DestructibleObjectsActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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

	public Body box1Body;
	public Body box2Body;
	public Body box3Body;
	public boolean breakOffBox1 = false;
	public boolean breakOffBox2 = false;
	public boolean breakOffBox3 = false;
	public Joint box1And2Joint;
	public Joint box2And3Joint;
	public Joint box3And1Joint;
	public boolean box1And2JointActive = true;
	public boolean box2And3JointActive = true;
	public boolean box3And1JointActive = true;
	public final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(20f, 0.0f, 0.9f);

	public boolean isBodyContacted(Body pBody, Contact pContact)
	{
		if(pContact.getFixtureA().getBody().equals(pBody) ||
				pContact.getFixtureB().getBody().equals(pBody))
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


		Rectangle box1Rect = new Rectangle(400f,260f,40f,40f,this.getEngine().getVertexBufferObjectManager());
		box1Rect.setColor(0.75f, 0f, 0f);
		mScene.attachChild(box1Rect);
		box1Body = PhysicsFactory.createBoxBody(mPhysicsWorld, box1Rect, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box1Rect, box1Body));

		Rectangle box2Rect = new Rectangle(380f,220f,40f,40f,this.getEngine().getVertexBufferObjectManager());
		box2Rect.setColor(0f, 0.75f, 0f);
		mScene.attachChild(box2Rect);
		box2Body = PhysicsFactory.createBoxBody(mPhysicsWorld, box2Rect, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box2Rect, box2Body));

		Rectangle box3Rect = new Rectangle(420f,220f,40f,40f,this.getEngine().getVertexBufferObjectManager());
		box3Rect.setColor(0f, 0f, 0.75f);
		mScene.attachChild(box3Rect);
		box3Body = PhysicsFactory.createBoxBody(mPhysicsWorld, box3Rect, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box3Rect, box3Body));

		final WeldJointDef box1and2JointDef = new WeldJointDef();
		box1and2JointDef.initialize(box1Body, box2Body, box1Body.getWorldCenter());
		box1And2Joint = mPhysicsWorld.createJoint(box1and2JointDef);

		final WeldJointDef box2and3JointDef = new WeldJointDef();
		box2and3JointDef.initialize(box2Body, box3Body, box2Body.getWorldCenter());
		box2And3Joint = mPhysicsWorld.createJoint(box2and3JointDef);

		final WeldJointDef box3and1JointDef = new WeldJointDef();
		box3and1JointDef.initialize(box3Body, box1Body, box3Body.getWorldCenter());
		box3And1Joint = mPhysicsWorld.createJoint(box3and1JointDef);

		mPhysicsWorld.setContactListener(new ContactListener(){
			float maxImpulse;
			@Override
			public void beginContact(Contact contact) {}

			@Override
			public void endContact(Contact contact) {}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				maxImpulse = impulse.getNormalImpulses()[0];
				for(int i = 1; i < impulse.getNormalImpulses().length; i++) {
					maxImpulse = Math.max(impulse.getNormalImpulses()[i], maxImpulse);
				}
				if(maxImpulse>800f) {
					if(isBodyContacted(box1Body,contact))
						breakOffBox1 = true;
					else if(isBodyContacted(box2Body,contact))
						breakOffBox2 = true;
					else if(isBodyContacted(box3Body,contact))
						breakOffBox3 = true;
				}
			}
		});

		mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(breakOffBox1) {
					if(box1And2JointActive)
						mPhysicsWorld.destroyJoint(box1And2Joint);
					if(box3And1JointActive)
						mPhysicsWorld.destroyJoint(box3And1Joint);
					box1And2JointActive = false;
					box3And1JointActive = false;
					breakOffBox1 = false;
				}
				if(breakOffBox2) {
					if(box1And2JointActive)
						mPhysicsWorld.destroyJoint(box1And2Joint);
					if(box2And3JointActive)
						mPhysicsWorld.destroyJoint(box2And3Joint);
					box1And2JointActive = false;
					box2And3JointActive = false;
					breakOffBox1 = false;
				}
				if(breakOffBox3) {
					if(box2And3JointActive)
						mPhysicsWorld.destroyJoint(box2And3Joint);
					if(box3And1JointActive)
						mPhysicsWorld.destroyJoint(box3And1Joint);
					box2And3JointActive = false;
					box3And1JointActive = false;
					breakOffBox1 = false;
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
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {}

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