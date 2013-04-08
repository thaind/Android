public class RagdollActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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


	Body headBody;
	Body torsoBody;
	Body leftUpperarmBody;
	Body leftForearmBody;
	Body rightUpperarmBody;
	Body rightForearmBody;
	Body leftThighBody;
	Body leftCalfBody;
	Body rightThighBody;
	Body rightCalfBody;
	Vector2 localMouseJointTarget = new Vector2();
	MouseJointDef mouseJointDef;
	MouseJoint mouseJoint;
	Body MouseJointGround;
	final FixtureDef headFixtureDef = PhysicsFactory.createFixtureDef(30f, 0.1f, 0.9f);
	final FixtureDef torsoFixtureDef = PhysicsFactory.createFixtureDef(100f, 0.7f, 0.9f);
	final FixtureDef armsFixtureDef = PhysicsFactory.createFixtureDef(20f, 0.5f, 0.9f);
	final FixtureDef legsFixtureDef = PhysicsFactory.createFixtureDef(30f, 0.5f, 0.9f);


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


		Rectangle headRect = new Rectangle(400f,360f,40f,40f,this.getEngine().getVertexBufferObjectManager());
		headRect.setColor(0f, 0f, 0f);
		mScene.attachChild(headRect);
		headBody = PhysicsFactory.createBoxBody(mPhysicsWorld, headRect, BodyType.DynamicBody, headFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(headRect, headBody));

		Rectangle torsoRect = new Rectangle(400f,260f,60f,140f,this.getEngine().getVertexBufferObjectManager());
		torsoRect.setColor(0f, 0f, 0f);
		mScene.attachChild(torsoRect);
		torsoBody = PhysicsFactory.createBoxBody(mPhysicsWorld, torsoRect, BodyType.DynamicBody, torsoFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(torsoRect, torsoBody));

		Rectangle leftUpperarmRect = new Rectangle(360f,300f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		leftUpperarmRect.setColor(0f, 0f, 0f);
		mScene.attachChild(leftUpperarmRect);
		leftUpperarmBody = PhysicsFactory.createBoxBody(mPhysicsWorld, leftUpperarmRect, BodyType.DynamicBody, armsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(leftUpperarmRect, leftUpperarmBody));

		Rectangle leftForearmRect = new Rectangle(360f,230f,20f,80f,this.getEngine().getVertexBufferObjectManager());
		leftForearmRect.setColor(0f, 0f, 0f);
		mScene.attachChild(leftForearmRect);
		leftForearmBody = PhysicsFactory.createBoxBody(mPhysicsWorld, leftForearmRect, BodyType.DynamicBody, armsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(leftForearmRect, leftForearmBody));

		Rectangle rightUpperarmRect = new Rectangle(440f,300f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		rightUpperarmRect.setColor(0f, 0f, 0f);
		mScene.attachChild(rightUpperarmRect);
		rightUpperarmBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rightUpperarmRect, BodyType.DynamicBody, armsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rightUpperarmRect, rightUpperarmBody));

		Rectangle rightForearmRect = new Rectangle(440f,230f,20f,80f,this.getEngine().getVertexBufferObjectManager());
		rightForearmRect.setColor(0f, 0f, 0f);
		mScene.attachChild(rightForearmRect);
		rightForearmBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rightForearmRect, BodyType.DynamicBody, armsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rightForearmRect, rightForearmBody));

		Rectangle leftThighRect = new Rectangle(380f,160f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		leftThighRect.setColor(0f, 0f, 0f);
		mScene.attachChild(leftThighRect);
		leftThighBody = PhysicsFactory.createBoxBody(mPhysicsWorld, leftThighRect, BodyType.DynamicBody, legsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(leftThighRect, leftThighBody));

		Rectangle leftCalfRect = new Rectangle(380f,100f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		leftCalfRect.setColor(0f, 0f, 0f);
		mScene.attachChild(leftCalfRect);
		leftCalfBody = PhysicsFactory.createBoxBody(mPhysicsWorld, leftCalfRect, BodyType.DynamicBody, legsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(leftCalfRect, leftCalfBody));

		Rectangle rightThighRect = new Rectangle(420f,160f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		rightThighRect.setColor(0f, 0f, 0f);
		mScene.attachChild(rightThighRect);
		rightThighBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rightThighRect, BodyType.DynamicBody, legsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rightThighRect, rightThighBody));

		Rectangle rightCalfRect = new Rectangle(420f,100f,20f,60f,this.getEngine().getVertexBufferObjectManager());
		rightCalfRect.setColor(0f, 0f, 0f);
		mScene.attachChild(rightCalfRect);
		rightCalfBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rightCalfRect, BodyType.DynamicBody, legsFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rightCalfRect, rightCalfBody));

		final RevoluteJointDef neckJointDef = new RevoluteJointDef();
		neckJointDef.initialize( headBody, torsoBody,
				new Vector2(400f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 340f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		neckJointDef.collideConnected = false;
		neckJointDef.enableLimit = true;
		neckJointDef.lowerAngle = MathUtils.degToRad(-30f);
		neckJointDef.upperAngle = MathUtils.degToRad(30f);
		mPhysicsWorld.createJoint(neckJointDef);

		final RevoluteJointDef leftShoulderJointDef = new RevoluteJointDef();
		leftShoulderJointDef.initialize( leftUpperarmBody, torsoBody,
				new Vector2(360f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 320f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		leftShoulderJointDef.collideConnected = false;
		leftShoulderJointDef.enableLimit = true;
		leftShoulderJointDef.upperAngle = MathUtils.degToRad(186f);
		mPhysicsWorld.createJoint(leftShoulderJointDef);

		final RevoluteJointDef leftElbowJointDef = new RevoluteJointDef();
		leftElbowJointDef.initialize( leftForearmBody, leftUpperarmBody,
				new Vector2(360f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 260f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		leftElbowJointDef.collideConnected = false;
		leftElbowJointDef.enableLimit = true;
		leftElbowJointDef.upperAngle = MathUtils.degToRad(150f);
		mPhysicsWorld.createJoint(leftElbowJointDef);

		final RevoluteJointDef rightShoulderJointDef = new RevoluteJointDef();
		rightShoulderJointDef.initialize( rightUpperarmBody, torsoBody,
				new Vector2(440f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 320f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		rightShoulderJointDef.collideConnected = false;
		rightShoulderJointDef.enableLimit = true;
		rightShoulderJointDef.lowerAngle = MathUtils.degToRad(-186f);
		mPhysicsWorld.createJoint(rightShoulderJointDef);

		final RevoluteJointDef rightElbowJointDef = new RevoluteJointDef();
		rightElbowJointDef.initialize( rightForearmBody, rightUpperarmBody,
				new Vector2(440f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 260f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		rightElbowJointDef.collideConnected = false;
		rightElbowJointDef.enableLimit = true;
		rightElbowJointDef.lowerAngle = MathUtils.degToRad(-150f);
		mPhysicsWorld.createJoint(rightElbowJointDef);

		final RevoluteJointDef leftThighJointDef = new RevoluteJointDef();
		leftThighJointDef.initialize( leftThighBody, torsoBody,
				new Vector2(380f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 180f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		leftThighJointDef.collideConnected = false;
		leftThighJointDef.enableLimit = true;
		leftThighJointDef.upperAngle = MathUtils.degToRad(110f);
		leftThighJointDef.lowerAngle = MathUtils.degToRad(-40f);
		mPhysicsWorld.createJoint(leftThighJointDef);

		final RevoluteJointDef leftKneeJointDef = new RevoluteJointDef();
		leftKneeJointDef.initialize( leftCalfBody, leftThighBody,
				new Vector2(380f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 120f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		leftKneeJointDef.collideConnected = false;
		leftKneeJointDef.enableLimit = true;
		leftKneeJointDef.lowerAngle = MathUtils.degToRad(-112f);
		mPhysicsWorld.createJoint(leftKneeJointDef);

		final RevoluteJointDef rightThighJointDef = new RevoluteJointDef();
		rightThighJointDef.initialize( rightThighBody, torsoBody,
				new Vector2(420f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 180f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		rightThighJointDef.collideConnected = false;
		rightThighJointDef.enableLimit = true;
		rightThighJointDef.lowerAngle = MathUtils.degToRad(-110f);
		rightThighJointDef.upperAngle = MathUtils.degToRad(40f);
		mPhysicsWorld.createJoint(rightThighJointDef);

		final RevoluteJointDef rightKneeJointDef = new RevoluteJointDef();
		rightKneeJointDef.initialize( rightCalfBody, rightThighBody,
				new Vector2(420f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 120f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		rightKneeJointDef.collideConnected = false;
		rightKneeJointDef.enableLimit = true;
		rightKneeJointDef.upperAngle = MathUtils.degToRad(112f);
		mPhysicsWorld.createJoint(rightKneeJointDef);

		MouseJointGround = mPhysicsWorld.createBody(new BodyDef());
		mouseJointDef = new MouseJointDef();
		mouseJointDef.bodyA = MouseJointGround;
		mouseJointDef.bodyB = headBody;
		mouseJointDef.dampingRatio = 1f;
		mouseJointDef.frequencyHz = 10f;
		mouseJointDef.maxForce = (100.0f * torsoBody.getMass());
		mouseJointDef.collideConnected = false;


		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			mouseJointDef.target.set(headBody.getWorldCenter());
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