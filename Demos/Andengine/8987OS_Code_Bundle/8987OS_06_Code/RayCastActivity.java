public class RayCastActivity extends BaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

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


	Body BoxBody;
	Line RayCastLine;
	Line RayCastLineHitNormal;
	Line RayCastLineHitBounce;
	float[] RayCastStart = {cameraWidth/2f,15f};
	float RayCastAngle = 0f;
	float RayCastNormalAngle = 0f;
	float RayCastBounceAngle = 0f;
	float RaycastBounceLineLength = 200f;
	RayCastCallback rayCastCallBack = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction)
		{
			float[] linePos = {
					point.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					point.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					(point.x + (normal.x)) * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					(point.y + (normal.y)) * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT};
			RayCastLineHitNormal.setPosition(linePos[0],linePos[1],linePos[2],linePos[3]);
			RayCastLineHitNormal.setVisible(true);
			RayCastNormalAngle = MathUtils.radToDeg((float) Math.atan2(linePos[3]-linePos[1], linePos[2]-linePos[0]));
			RayCastBounceAngle = (2*RayCastNormalAngle)-RayCastAngle;
			RayCastLineHitBounce.setPosition(linePos[0], linePos[1], (linePos[0] + FloatMath.cos((RayCastBounceAngle + 180f) * MathConstants.DEG_TO_RAD)*RaycastBounceLineLength),
					(linePos[1] + FloatMath.sin((RayCastBounceAngle + 180f) * MathConstants.DEG_TO_RAD)*RaycastBounceLineLength));
			RayCastLineHitBounce.setVisible(true);
			return 0f;
		}
	};


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


		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(1f, 0.5f, 0.9f);
		Rectangle Box1 = new Rectangle(400f,350f,200f,200f,this.getEngine().getVertexBufferObjectManager());
		Box1.setColor(0.3f, 0.3f, 0.3f);
		BoxBody = PhysicsFactory.createBoxBody(mPhysicsWorld, Box1, BodyType.StaticBody, boxFixtureDef);
		BoxBody.setTransform(BoxBody.getWorldCenter(), MathUtils.random(20f, 70f));
		mScene.attachChild(Box1);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(Box1, BoxBody));

		RayCastLine = new Line(0f,0f,0f,0f,mEngine.getVertexBufferObjectManager());
		RayCastLine.setColor(0f, 1f, 0f);
		RayCastLine.setLineWidth(8f);
		mScene.attachChild(RayCastLine);

		RayCastLineHitNormal = new Line(0f,0f,0f,0f,mEngine.getVertexBufferObjectManager());
		RayCastLineHitNormal.setColor(1f, 0f, 0f);
		RayCastLineHitNormal.setLineWidth(8f);
		mScene.attachChild(RayCastLineHitNormal);

		RayCastLineHitBounce = new Line(0f,0f,0f,0f,mEngine.getVertexBufferObjectManager());
		RayCastLineHitBounce.setColor(0f, 0f, 1f);
		RayCastLineHitBounce.setLineWidth(8f);
		mScene.attachChild(RayCastLineHitBounce);


		mScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}



	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionMove()||pSceneTouchEvent.isActionDown()) {
			RayCastAngle = MathUtils.radToDeg((float) Math.atan2(pSceneTouchEvent.getY() - RayCastStart[1], pSceneTouchEvent.getX() - RayCastStart[0]));
			RayCastLine.setPosition(RayCastStart[0], RayCastStart[1], pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			RayCastLine.setVisible(true);
			RayCastLineHitNormal.setVisible(false);
			RayCastLineHitBounce.setVisible(false);
			mPhysicsWorld.rayCast(rayCastCallBack,
					new Vector2(
							RayCastStart[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
							RayCastStart[1]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
							new Vector2(
									pSceneTouchEvent.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
									pSceneTouchEvent.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		}
		if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionOutside()||pSceneTouchEvent.isActionCancel()) {
			RayCastLine.setVisible(false);
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