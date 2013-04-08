public class JointsActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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


	Body DistanceJointBodyA;
	Body DistanceJointBodyB;
	Body LineJointBodyA;
	Body LineJointBodyB;

	Vector2 localMouseJointTarget = new Vector2();
	MouseJointDef mouseJointDef;
	MouseJoint mouseJoint;
	Body MouseJointBodyA;
	Body MouseJointBodyB;

	Body PrismaticJointBodyA;
	Body PrismaticJointBodyB;
	Body PulleyJointBodyA;
	Body PulleyJointBodyB;
	Body RevoluteJointBodyA;
	Body RevoluteJointBodyB;
	Body WeldJointBodyA;
	Body WeldJointBodyB;

	final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(20f, 0.2f, 0.9f);



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

		// Distance
		Rectangle DistanceJointRectA = new Rectangle(114f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		DistanceJointRectA.setColor(0.5f, 0f, 0f);
		mScene.attachChild(DistanceJointRectA);
		DistanceJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, DistanceJointRectA, BodyType.KinematicBody, boxFixtureDef);

		Rectangle DistanceJointRectB = new Rectangle(114f,200f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		DistanceJointRectB.setColor(0.75f, 0f, 0f);
		mScene.attachChild(DistanceJointRectB);
		DistanceJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, DistanceJointRectB, BodyType.DynamicBody, boxFixtureDef);
		DistanceJointBodyB.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(DistanceJointRectB, DistanceJointBodyB));

		final DistanceJointDef distanceJointDef = new DistanceJointDef();
		distanceJointDef.initialize(DistanceJointBodyA, DistanceJointBodyB, DistanceJointBodyA.getWorldCenter(), DistanceJointBodyB.getWorldCenter());
		distanceJointDef.length = 3.0f;
		distanceJointDef.collideConnected = true;
		distanceJointDef.frequencyHz = 1f;
		distanceJointDef.dampingRatio = 0.001f;
		mPhysicsWorld.createJoint(distanceJointDef);

		// Line
		Rectangle LineJointRectA = new Rectangle(228f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		LineJointRectA.setColor(0.5f, 0.25f, 0f);
		mScene.attachChild(LineJointRectA);
		LineJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, LineJointRectA, BodyType.KinematicBody, boxFixtureDef);

		Rectangle LineJointRectB = new Rectangle(228f,200f,30f,30f,this.getEngine().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				LineJointBodyB.setAngularVelocity(2f);
			}
		};
		LineJointRectB.setColor(0.75f, 0.375f, 0f);
		mScene.attachChild(LineJointRectB);
		LineJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, LineJointRectB, BodyType.DynamicBody, boxFixtureDef);
		LineJointBodyB.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(LineJointRectB, LineJointBodyB));

		final LineJointDef lineJointDef = new LineJointDef();
		lineJointDef.initialize(LineJointBodyA, LineJointBodyB, LineJointBodyB.getWorldCenter(), new Vector2(0f,1f));
		lineJointDef.collideConnected = true;
		lineJointDef.enableLimit = true;
		lineJointDef.lowerTranslation = -220f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		lineJointDef.upperTranslation = 0f;
		lineJointDef.enableMotor = true;
		lineJointDef.motorSpeed = -20f;
		lineJointDef.maxMotorForce = 120f;
		mPhysicsWorld.createJoint(lineJointDef);

		// Mouse
		MouseJointBodyA = mPhysicsWorld.createBody(new BodyDef());

		Rectangle MouseJointRect = new Rectangle(45f,45f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		MouseJointRect.setColor(0f, 0f, 0f);
		mScene.attachChild(MouseJointRect);
		MouseJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, MouseJointRect, BodyType.DynamicBody, boxFixtureDef);
		MouseJointBodyB.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(MouseJointRect, MouseJointBodyB));

		mouseJointDef = new MouseJointDef();
		mouseJointDef.bodyA = MouseJointBodyA;
		mouseJointDef.bodyB = MouseJointBodyB;
		mouseJointDef.dampingRatio = 0.0f;
		mouseJointDef.frequencyHz = 1f;
		mouseJointDef.maxForce = (100.0f * MouseJointBodyB.getMass());
		mouseJointDef.collideConnected = false;

		// Prismatic
		Rectangle PrismaticJointRectA = new Rectangle(342f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		PrismaticJointRectA.setColor(0f, 0.5f, 0f);
		mScene.attachChild(PrismaticJointRectA);
		PrismaticJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, PrismaticJointRectA, BodyType.KinematicBody, boxFixtureDef);

		Rectangle PrismaticJointRectB = new Rectangle(342f,280f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		PrismaticJointRectB.setColor(0f, 0.75f, 0f);
		mScene.attachChild(PrismaticJointRectB);
		PrismaticJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, PrismaticJointRectB, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(PrismaticJointRectB, PrismaticJointBodyB));

		final PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
		prismaticJointDef.initialize(PrismaticJointBodyA, PrismaticJointBodyB, PrismaticJointBodyA.getWorldCenter(), new Vector2(0f,1f));
		prismaticJointDef.collideConnected = false;
		prismaticJointDef.enableLimit = true;
		prismaticJointDef.lowerTranslation = -80f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		prismaticJointDef.upperTranslation = 80f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		prismaticJointDef.enableMotor = true;
		prismaticJointDef.maxMotorForce = 400f;
		prismaticJointDef.motorSpeed = 500f;
		mPhysicsWorld.createJoint(prismaticJointDef);

		// Pulley
		Rectangle PulleyJointRectA = new Rectangle(436f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		PulleyJointRectA.setColor(0f, 0.5f, 0.5f);
		mScene.attachChild(PulleyJointRectA);
		PulleyJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, PulleyJointRectA, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(PulleyJointRectA, PulleyJointBodyA));

		Rectangle PulleyJointRectB = new Rectangle(476f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		PulleyJointRectB.setColor(0f, 0.75f, 0.75f);
		mScene.attachChild(PulleyJointRectB);
		PulleyJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, PulleyJointRectB, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(PulleyJointRectB, PulleyJointBodyB));

		final PulleyJointDef pulleyJointDef = new PulleyJointDef();
		pulleyJointDef.initialize(
				PulleyJointBodyA,
				PulleyJointBodyB,
				PulleyJointBodyA.getWorldPoint(new Vector2(0f, 80f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)),
				PulleyJointBodyB.getWorldPoint(new Vector2(0f, 80f / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT)),
				PulleyJointBodyA.getWorldCenter(),
				PulleyJointBodyB.getWorldCenter(),
				1f);
		mPhysicsWorld.createJoint(pulleyJointDef);

		// Revolute
		Rectangle RevoluteJointRectA = new Rectangle(570f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		RevoluteJointRectA.setColor(0f, 0f, 0.65f);
		mScene.attachChild(RevoluteJointRectA);
		RevoluteJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, RevoluteJointRectA, BodyType.KinematicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(RevoluteJointRectA, RevoluteJointBodyA));

		Rectangle RevoluteJointRectB = new Rectangle(570f,280f,10f,90f,this.getEngine().getVertexBufferObjectManager());
		RevoluteJointRectB.setColor(0f, 0f, 0.8f);
		mScene.attachChild(RevoluteJointRectB);
		RevoluteJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, RevoluteJointRectB, BodyType.DynamicBody, boxFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(RevoluteJointRectB, RevoluteJointBodyB));

		final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(
				RevoluteJointBodyA,
				RevoluteJointBodyB,
				RevoluteJointBodyA.getWorldCenter());
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.enableMotor = true;
		revoluteJointDef.maxMotorTorque = 5000f;
		revoluteJointDef.motorSpeed = -1f;
		mPhysicsWorld.createJoint(revoluteJointDef);

		// Weld
		Rectangle WeldJointRectA = new Rectangle(684f,240f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		WeldJointRectA.setColor(0.5f, 0f, 0.5f);
		mScene.attachChild(WeldJointRectA);
		WeldJointBodyA = PhysicsFactory.createBoxBody(mPhysicsWorld, WeldJointRectA, BodyType.KinematicBody, boxFixtureDef);

		Rectangle WeldJointRectB = new Rectangle(684f,200f,30f,30f,this.getEngine().getVertexBufferObjectManager());
		WeldJointRectB.setColor(0.75f, 0f, 0.75f);
		mScene.attachChild(WeldJointRectB);
		WeldJointBodyB = PhysicsFactory.createBoxBody(mPhysicsWorld, WeldJointRectB, BodyType.DynamicBody, boxFixtureDef);
		WeldJointBodyB.setLinearDamping(2f);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(WeldJointRectB, WeldJointBodyB));

		final WeldJointDef weldJointDef = new WeldJointDef();
		weldJointDef.initialize(WeldJointBodyA, WeldJointBodyB, WeldJointBodyA.getWorldCenter());
		weldJointDef.collideConnected = true;
		mPhysicsWorld.createJoint(weldJointDef);


		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			mouseJointDef.target.set(MouseJointBodyB.getWorldCenter());
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